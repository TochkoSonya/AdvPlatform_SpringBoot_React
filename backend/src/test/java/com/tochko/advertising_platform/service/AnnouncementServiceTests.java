package com.tochko.advertising_platform.service;

import com.tochko.advertising_platform.model.Announcement;
import com.tochko.advertising_platform.model.User;
import com.tochko.advertising_platform.model.enums.AnnouncementStatus;
import com.tochko.advertising_platform.model.enums.AnnouncementType;
import com.tochko.advertising_platform.repository.AnnouncementRepository;
import com.tochko.advertising_platform.service.impl.AnnouncementServiceImpl;
import com.tochko.advertising_platform.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class AnnouncementServiceTests {

    private static final String USERNAME = "testUser";

    @InjectMocks
    private AnnouncementServiceImpl announcementService;
    @Mock
    private AnnouncementRepository announcementRepository;
    @Mock
    private UserServiceImpl userServiceImpl;

    @Captor
    private ArgumentCaptor<AnnouncementType> typeCaptor;
    @Captor
    private ArgumentCaptor<User> userCaptor;
    @Captor
    private ArgumentCaptor<AnnouncementStatus> statusCaptor;
    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    private Announcement announcement;
    private User user;
    private Pageable pageable;
    private Page<Announcement> pageAnnouncement;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        setUpMockedData();
    }

    private void setUpMockedData() {
        user = new User();
        user.setId(1L);

        announcement = new Announcement();
        announcement.setId(1L);
        announcement.setStatus(AnnouncementStatus.ACTIVE);

        pageable = PageRequest.of(0, 2);
        pageAnnouncement = new PageImpl<>(List.of(announcement));
    }

    @Test
    void shouldReturnAnnouncementPageByStatus() {
        //given
        given(userServiceImpl.findByUsername(USERNAME)).willReturn(Optional.of(user));
        given(announcementRepository.findByTypeAndUserAndStatus(any(), any(), any(), any()))
                .willReturn(pageAnnouncement);

        //when
        Page<Announcement> actual = announcementService.getByTypeAndUsername(announcement.getType(),
                announcement.getStatus(),
                USERNAME,
                pageable);
        //then
        then(announcementRepository).should().findByTypeAndUserAndStatus(
                typeCaptor.capture(),
                userCaptor.capture(),
                statusCaptor.capture(),
                pageableCaptor.capture());

        assertEquals(announcement.getType(), typeCaptor.getValue());
        assertEquals(user, userCaptor.getValue());
        assertEquals(announcement.getStatus(), statusCaptor.getValue());
        assertEquals(pageable, pageableCaptor.getValue());
        assertEquals(pageAnnouncement, actual);
    }

    @Test
    void shouldReturnAnnouncementPageWithoutStatus() {
        //given
        given(userServiceImpl.findByUsername(USERNAME)).willReturn(Optional.of(user));
        given(announcementRepository.findByTypeAndUser(any(), any(), any()))
                .willReturn(pageAnnouncement);

        //when
        Page<Announcement> actual = announcementService.getByTypeAndUsername(announcement.getType(),
                null,
                USERNAME,
                pageable);
        //then
        then(announcementRepository).should().findByTypeAndUser(
                typeCaptor.capture(),
                userCaptor.capture(),
                pageableCaptor.capture());

        assertEquals(announcement.getType(), typeCaptor.getValue());
        assertEquals(user, userCaptor.getValue());
        assertEquals(pageable, pageableCaptor.getValue());
        assertEquals(pageAnnouncement, actual);
    }
}

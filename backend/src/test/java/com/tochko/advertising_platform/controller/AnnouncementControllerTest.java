package com.tochko.advertising_platform.controller;

import com.tochko.advertising_platform.model.Announcement;
import com.tochko.advertising_platform.service.AnnouncementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AnnouncementControllerTest {

    @Mock
    private AnnouncementService announcementService;

    @InjectMocks
    private AnnouncementController controller;

    @Autowired
    private MockMvc mockMvc;

    private Announcement announcement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
        setUpMockedData();
    }

    private void setUpMockedData() {
        announcement = new Announcement();
        announcement.setId(1L);
    }

    @Test
    void shouldReturnAnnouncementWhenDataIsValid() throws Exception {
        //given
        given(announcementService.get(anyLong())).willReturn(announcement);

        //when
        //then
        mockMvc.perform(get("/announcement/" + announcement.getId()))
                .andExpect(status().isOk());
    }
}

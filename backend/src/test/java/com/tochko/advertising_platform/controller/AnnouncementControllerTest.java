package com.tochko.advertising_platform.controller;

import com.tochko.advertising_platform.model.Announcement;
import com.tochko.advertising_platform.service.AnnouncementService;
import com.tochko.advertising_platform.service.impl.AnnouncementServiceImpl;
import com.tochko.advertising_platform.service.impl.FavoriteServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@WebMvcTest({AnnouncementController.class})
//@ExtendWith(MockitoExtension.class)
public class AnnouncementControllerTest {

    @Mock
    private AnnouncementService announcementService;
    @Mock
    private FavoriteServiceImpl favoriteService;
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

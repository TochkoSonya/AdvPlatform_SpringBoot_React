package com.tochko.advertising_platform.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_platform_details")
public class UserPlatformDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int followers;
    private int femaleFollowersNumber;
    private int maleFollowersNumber;

    private int youngFollowersNumber;   // <30
    private int middleFollowersNumber;  // 30-60
    private int adultFollowersNumber;   // >60
}

package com.tochko.advertising_platform.model;

import com.tochko.advertising_platform.model.enums.MessageType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "messages")
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String sender;
    private String receiver;
    private MessageType type;
    private LocalDateTime dateTime = LocalDateTime.now();
}

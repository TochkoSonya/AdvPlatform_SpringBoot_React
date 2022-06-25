package com.tochko.advertising_platform.controller;

import com.tochko.advertising_platform.model.*;
import com.tochko.advertising_platform.model.enums.ERole;
import com.tochko.advertising_platform.model.enums.MessageType;
import com.tochko.advertising_platform.model.enums.UserStatus;
import com.tochko.advertising_platform.service.MessageService;
import com.tochko.advertising_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final MessageService messageService;

    @Autowired
    public UserController(UserService useService,
                          MessageService messageService) {
        this.userService = useService;
        this.messageService = messageService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> list() {
        try {
            List<User> userList = userService.getAll();
            return new ResponseEntity<>(userList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public ResponseEntity<User> getByUsername(@RequestParam("username") String username) {
        try {
            Optional<User> user = userService.findByUsername(username);
            return new ResponseEntity<>(user.get(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") Long userId) {
        try {
            Optional<User> user = userService.get(userId);
            return new ResponseEntity<>(user.get(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("")
    public ResponseEntity<User> update(@RequestBody User user) {
        try {
            userService.update(user);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/sendDefaultMessage")
    public ResponseEntity<?> sendDefaultMessage(@RequestParam("sender") String senderName,
                                                @RequestParam("receiver") String receiverName) {
        try {
            Message message = new Message();
            message.setContent("Привет! Предлагаю сотрудничать");
            message.setSender(senderName);
            message.setReceiver(receiverName);
            message.setType(MessageType.CHAT);
            messageService.save(message);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteChat")
    public ResponseEntity<?> removeChat(@RequestParam("otherUser") String otherUserUsername,
                                        @RequestParam("currentUser") String currentUserUsername) {
        try {
            messageService.removeChat(otherUserUsername, currentUserUsername);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/platformDetails")
    public ResponseEntity<User> addUserDetails(@RequestParam("userId") Long
                                                       userId, @RequestBody UserPlatformDetails platformDetails) {
        try {
            userService.addPlatformDetails(userId, platformDetails);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/changePassword")
    public ResponseEntity<User> changePassword(@RequestBody User user) {
        try {
            userService.changePassword(user);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/status")
    public ResponseEntity<?> changeUserStatus(@RequestBody UserStatus status,
                                              @RequestParam("userId") Long userId) {
        try {
            userService.changeUserStatus(userId, status);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/role")
    public ResponseEntity<?> changeUserRole(@RequestBody ERole roleName,
                                            @RequestParam("userId") Long userId) {
        try {
            userService.changeUserRole(userId, roleName);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@PathVariable("id") Long id) {
        try {
            userService.delete(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<List<Message>> getAllUserMessages(@PathVariable("id") Long userId) {
        try {
            List<Message> messages = messageService.getAllUserMessages(userId);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<?> uploadFile(@PathVariable("id") Long userId,
                                        @RequestParam("file") MultipartFile file) {
        try {
            User user = userService.get(userId).get();
            if (user != null) {
                userService.uploadFile(userId, file);
            }
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}

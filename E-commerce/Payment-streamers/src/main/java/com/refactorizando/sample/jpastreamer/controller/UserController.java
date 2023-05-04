package com.refactorizando.sample.jpastreamer.controller;

import com.refactorizando.sample.jpastreamer.model.User;
import com.refactorizando.sample.jpastreamer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers(@RequestParam(value="name") String name) {

        if (null != name) {

            return ResponseEntity.ok(userService.findByName(name));

        }
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {

        return ResponseEntity.ok(userService.findById(id));

    }


}

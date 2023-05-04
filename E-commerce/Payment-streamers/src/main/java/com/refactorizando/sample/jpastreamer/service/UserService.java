package com.refactorizando.sample.jpastreamer.service;

import com.refactorizando.sample.jpastreamer.model.User;
import com.speedment.jpastreamer.application.JPAStreamer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final JPAStreamer jpaStreamer;

    public List<User> findAll() {

        return jpaStreamer.stream(User.class).collect(Collectors.toList());

    }

    public User findById(Long id) {

        return jpaStreamer.stream(User.class)
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }

    public List<User> findByName(String name) {

        return jpaStreamer.stream(User.class)
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

}

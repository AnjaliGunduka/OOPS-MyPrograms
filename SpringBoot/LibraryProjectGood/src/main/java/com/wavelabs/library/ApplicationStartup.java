package com.wavelabs.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.wavelabs.library.common.Constants;
import com.wavelabs.library.model.User;
import com.wavelabs.library.service.UserService;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        initDatabaseEntities();
    }


    private void initDatabaseEntities() {

        if( userService.getAllUsers().size() == 0) {
            userService.addNew(new User("Mr. Admin", "admin", "admin", Constants.ROLE_ADMIN));
            userService.addNew(new User("Mr. Librarian", "librarian", "librarian", Constants.ROLE_LIBRARIAN));
        }

    }
}
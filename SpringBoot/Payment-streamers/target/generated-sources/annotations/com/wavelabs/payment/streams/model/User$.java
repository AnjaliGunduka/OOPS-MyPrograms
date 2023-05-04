package com.wavelabs.payment.streams.model;

import com.speedment.jpastreamer.field.ComparableField;
import com.speedment.jpastreamer.field.StringField;
import com.wavelabs.payment.streams.model.User;

/**
 * The generated base for entity {@link User} representing entities of the
 * {@code user}-table in the database.
 * <p> This file has been automatically generated by JPAStreamer.
 * 
 * @author JPAStreamer
 */
public final class User$ {
    
    /**
     * This Field corresponds to the {@link User} field surname.
     */
    public static final StringField<User> surname = StringField.create(
        User.class,
        "surname",
        User::getSurname,
        false
    );
    /**
     * This Field corresponds to the {@link User} field email.
     */
    public static final StringField<User> email = StringField.create(
        User.class,
        "email",
        User::getEmail,
        false
    );
    /**
     * This Field corresponds to the {@link User} field id.
     */
    public static final ComparableField<User, Long> id = ComparableField.create(
        User.class,
        "id",
        User::getId,
        false
    );
    /**
     * This Field corresponds to the {@link User} field name.
     */
    public static final StringField<User> name = StringField.create(
        User.class,
        "name",
        User::getName,
        false
    );
}
package com.college.studentservices.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.springframework.beans.factory.annotation.Qualifier;


@Qualifier
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
		ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidatorImpl.class)
public @interface EnumValidator {

	Class<? extends Enum<?>> enumType();

	String message() default "Invalid value given in request";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
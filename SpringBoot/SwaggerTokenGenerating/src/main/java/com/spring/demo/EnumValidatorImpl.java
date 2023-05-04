package com.spring.demo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 
 * @author Anjali
 *
 */
public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {

	private List<String> acceptedValues;

	@Override
	public void initialize(EnumValidator annotation) {
		acceptedValues = Stream.of(annotation.enumType().getEnumConstants()).map(Enum::name)
				.collect(Collectors.toList());
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isNotBlank(value)) {
			return acceptedValues.contains(value);
		} else {
			return true;
		}
	}

}
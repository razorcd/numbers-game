package com.challenge.application.gameofthree.game.validator;

import com.challenge.application.gameofthree.exception.ValidationException;

import java.util.List;

/**
 * Interface to create validators that can be injected in any class that implements {@link CanValidate}
 * @param <T> the type of the object that will be validated.
 */
public interface Validator<T> {

    /**
     * Validates object and returns boolean.
     *
     * @param obj object to validate.
     * @return [boolean] if object is valid
     */
    boolean validate(T obj);

    /**
     * Validates or throws exception
     *
     * @param obj the object to validate
     * @throws ValidationException if object is invalid
     */
    void validateOrThrow(T obj) throws ValidationException;

    /**
     * Get all validation messages after validating.
     *
     * @return {@code [List<String>]} list of validation messages.
     */
    List<String> getValidationMessages();
}

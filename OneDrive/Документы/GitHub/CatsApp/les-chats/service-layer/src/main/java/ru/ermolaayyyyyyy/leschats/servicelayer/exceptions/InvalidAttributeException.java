package ru.ermolaayyyyyyy.leschats.servicelayer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidAttributeException extends ChatsException{
    protected InvalidAttributeException(String message) {
        super(message);
    }

    public static InvalidAttributeException colorDoesNotExist(String color){
        return new InvalidAttributeException("Color " + color + " does not exist");
    }

    public static InvalidAttributeException friendAlreadyExist(int id1, int id2){
        return new InvalidAttributeException("Cat with id " + id1 + "and cat with id " + id2 + " are already friends");
    }

    public static InvalidAttributeException loginAlreadyExist(String login){
        return new InvalidAttributeException("User with login " + login + " already exists");
    }

    public static InvalidAttributeException roleDoesNotExist(String role){
        return new InvalidAttributeException("Role " + role + " does not exist");
    }
}

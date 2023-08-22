package ru.ermolaayyyyyyy.leschats.servicelayer.exceptions;

public class EntityNotFoundException extends ChatsException{
    protected EntityNotFoundException(String message) {
        super(message);
    }

    public static EntityNotFoundException catDoesNotExist(int id){
        throw new EntityNotFoundException("Cat with id " + id + " does not exist");
    }
    public static EntityNotFoundException ownerDoesNotExist(int id){
        return new EntityNotFoundException("Owner with id " + id + " does not exist");
    }

    public static EntityNotFoundException userDoesNotExist(int id){
        return new EntityNotFoundException("User with id " + id + " does not exist");
    }

    public static EntityNotFoundException userDoesNotExist(String login){
        return new EntityNotFoundException("User with login " + login + " does not exist");
    }
}

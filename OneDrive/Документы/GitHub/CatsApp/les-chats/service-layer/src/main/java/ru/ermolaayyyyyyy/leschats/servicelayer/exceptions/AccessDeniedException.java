package ru.ermolaayyyyyyy.leschats.servicelayer.exceptions;

public class AccessDeniedException extends ChatsException{
    protected AccessDeniedException(String message) {
        super(message);
    }

    public static AccessDeniedException noAccessForUserException(String username){
        return new AccessDeniedException("You have no access for user with login " + username);
    }

    public static AccessDeniedException noAccessForCatException(int id){
        return new AccessDeniedException("You have no access for cat with id " + id);
    }
    public static AccessDeniedException notAuthorizedException(){
        return new AccessDeniedException("You are not authorized");
    }
}

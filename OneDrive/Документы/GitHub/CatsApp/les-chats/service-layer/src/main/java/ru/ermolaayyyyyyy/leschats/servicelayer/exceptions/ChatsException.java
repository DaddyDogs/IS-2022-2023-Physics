package ru.ermolaayyyyyyy.leschats.servicelayer.exceptions;

public class ChatsException extends RuntimeException{
    protected ChatsException(String message){
        super(message);
    }
}

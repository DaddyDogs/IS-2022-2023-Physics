package ru.ermolaayyyyyyy.leschats.servicelayer.dto;

import org.springframework.beans.PropertyValues;

import java.time.LocalDate;

public interface IOwnerDto {
    String name();

    LocalDate birthDate();
    int id();
}

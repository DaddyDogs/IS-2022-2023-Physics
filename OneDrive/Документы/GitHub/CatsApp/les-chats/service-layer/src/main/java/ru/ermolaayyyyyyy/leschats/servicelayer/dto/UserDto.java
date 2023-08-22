package ru.ermolaayyyyyyy.leschats.servicelayer.dto;

import java.time.LocalDate;

public record UserDto(int id, int ownerId, String name, LocalDate birthdate) { }

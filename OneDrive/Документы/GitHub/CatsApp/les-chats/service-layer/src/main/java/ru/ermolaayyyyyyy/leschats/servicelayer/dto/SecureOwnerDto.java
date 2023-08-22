package ru.ermolaayyyyyyy.leschats.servicelayer.dto;

import ru.ermolaayyyyyyy.leschats.servicelayer.dto.CatDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.IOwnerDto;

import java.time.LocalDate;
import java.util.List;

public record SecureOwnerDto(String name, LocalDate birthDate, List<Integer> cats, int id) implements IOwnerDto {
}

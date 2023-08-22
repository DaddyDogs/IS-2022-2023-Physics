package ru.ermolaayyyyyyy.leschats.servicelayer.dto;

import java.time.LocalDate;
import java.util.List;

public record OwnerDto(String name, LocalDate birthDate, List<CatDto> cats, int id) implements IOwnerDto{
}

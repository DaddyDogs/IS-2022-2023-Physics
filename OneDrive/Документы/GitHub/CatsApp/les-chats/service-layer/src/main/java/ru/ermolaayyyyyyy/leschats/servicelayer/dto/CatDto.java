package ru.ermolaayyyyyyy.leschats.servicelayer.dto;

import java.time.LocalDate;
import java.util.List;

public record CatDto(String name, LocalDate birthDate, String breed, String color, int ownerId, List<Integer> friends, int id) {
}

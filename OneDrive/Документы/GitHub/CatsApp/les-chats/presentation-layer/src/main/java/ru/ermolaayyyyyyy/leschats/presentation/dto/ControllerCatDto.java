package ru.ermolaayyyyyyy.leschats.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

public record ControllerCatDto(
                     @NotBlank(message = "Name cannot be blank")
                     String name,
                     @PastOrPresent(message = "Date must be in past or present")
                     LocalDate birthDate,
                     @NotBlank(message = "Breed cannot be blank")
                     String breed,
                     @NotBlank(message = "Color cannot be blank")
                     String color,
                     @Positive(message = "Id must be bigger than 0")
                     int ownerId){
}

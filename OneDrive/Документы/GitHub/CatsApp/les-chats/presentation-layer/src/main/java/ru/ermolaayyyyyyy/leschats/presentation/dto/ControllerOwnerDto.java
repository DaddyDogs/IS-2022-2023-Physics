package ru.ermolaayyyyyyy.leschats.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.CatDto;

import java.time.LocalDate;
import java.util.List;

public record ControllerOwnerDto(
                       @NotBlank(message = "Name cannot be blank")
                       String name,
                       @PastOrPresent(message = "Date must be in past or present")
                       LocalDate birthDate) {
}

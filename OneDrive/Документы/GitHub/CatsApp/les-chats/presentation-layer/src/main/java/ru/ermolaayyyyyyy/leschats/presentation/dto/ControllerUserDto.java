package ru.ermolaayyyyyyy.leschats.presentation.dto;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import ru.ermolaayyyyyyy.leschats.presentation.validator.ValidRole;

import java.time.LocalDate;

public record ControllerUserDto(
        @NotBlank(message = "Login cannot be blank")
        String login,
        @NotBlank(message = "Password cannot be blank")
        String password,
        @NotBlank(message = "Name cannot be blank")
        String name,
        @PastOrPresent(message = "Date must be in past or present")
        LocalDate birthDate,
        @ValidRole(message = "Role can be only ROLE_USER or ROLER_ADMIN")
        String role)
{
}

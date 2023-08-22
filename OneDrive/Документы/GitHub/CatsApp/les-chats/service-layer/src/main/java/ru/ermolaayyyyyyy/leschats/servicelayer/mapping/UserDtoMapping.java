package ru.ermolaayyyyyyy.leschats.servicelayer.mapping;

import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import org.springframework.cglib.core.Local;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities.User;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.UserDto;

import java.time.LocalDate;

@UtilityClass
@ExtensionMethod(UserDtoMapping.class)
public class UserDtoMapping {
    public static UserDto asDto(User user, String name, LocalDate birthdate){
        return new UserDto(user.getId(), user.getOwner().getId(), name, birthdate);
    }
}

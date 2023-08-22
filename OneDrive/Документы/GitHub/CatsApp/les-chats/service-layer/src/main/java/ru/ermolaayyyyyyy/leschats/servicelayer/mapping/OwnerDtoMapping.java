package ru.ermolaayyyyyyy.leschats.servicelayer.mapping;

import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities.Owner;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.CatDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.IOwnerDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.OwnerDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.SecureOwnerDto;

@UtilityClass
@ExtensionMethod(CatDtoMapping.class)
public class OwnerDtoMapping {
    public static OwnerDto asDto(Owner owner){
        return new OwnerDto(owner.getName(), owner.getBirthDate(), owner.getCats().stream().map(CatDtoMapping::asDto).toList(), owner.getId());
    }

    public static SecureOwnerDto asSecureDto(OwnerDto owner){
        return new SecureOwnerDto(owner.name(), owner.birthDate(), owner.cats().stream().map(CatDto::id).toList(), owner.id());
    }
}

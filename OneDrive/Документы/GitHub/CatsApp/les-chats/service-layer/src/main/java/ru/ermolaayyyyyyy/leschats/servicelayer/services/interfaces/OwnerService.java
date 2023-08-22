package ru.ermolaayyyyyyy.leschats.servicelayer.services.interfaces;

import ru.ermolaayyyyyyy.leschats.servicelayer.dto.CatDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.OwnerDto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface OwnerService {
    OwnerDto findOwnerById(int id);
    OwnerDto saveOwner(String name, LocalDate birthDate);
    void deleteOwner(int id);
    OwnerDto updateOwner(String name, LocalDate birthDate, int id);
    List<OwnerDto> findAllOwners();
}

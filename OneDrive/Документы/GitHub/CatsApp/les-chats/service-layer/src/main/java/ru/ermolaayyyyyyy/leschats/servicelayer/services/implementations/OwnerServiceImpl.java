package ru.ermolaayyyyyyy.leschats.servicelayer.services.implementations;

import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities.Owner;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.repositories.OwnerRepository;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.OwnerDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.exceptions.EntityNotFoundException;
import ru.ermolaayyyyyyy.leschats.servicelayer.mapping.OwnerDtoMapping;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.interfaces.OwnerService;

import java.time.LocalDate;
import java.util.List;

@ExtensionMethod(OwnerDtoMapping.class)
@Service
public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;

    @Autowired
    public OwnerServiceImpl(OwnerRepository ownerRepository){
        this.ownerRepository = ownerRepository;
    }
    @Override
    public OwnerDto findOwnerById(int id) {
        return getOwnerById(id).asDto();
    }

    @Override
    public OwnerDto saveOwner(String name, LocalDate birthDate) {
        Owner owner = new Owner(name, birthDate);
        ownerRepository.saveAndFlush(owner);
        return owner.asDto();
    }

    @Override
    public void deleteOwner(int id) {
        Owner owner = getOwnerById(id);
        ownerRepository.delete(owner);
    }

    @Override
    public OwnerDto updateOwner(String name, LocalDate birthDate, int id) {
        Owner owner = getOwnerById(id);
        owner.update(name, birthDate);
        ownerRepository.save(owner);
        return owner.asDto();
    }

    @Override
    public List<OwnerDto> findAllOwners() {
        return ownerRepository.findAll().stream().map(x -> x.asDto()).toList();
    }

    Owner getOwnerById(int id){
        return ownerRepository.findById(id).orElseThrow(() -> EntityNotFoundException.ownerDoesNotExist(id));
    }
}

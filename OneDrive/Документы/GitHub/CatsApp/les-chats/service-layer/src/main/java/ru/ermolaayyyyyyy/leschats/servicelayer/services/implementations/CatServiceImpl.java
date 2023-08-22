package ru.ermolaayyyyyyy.leschats.servicelayer.services.implementations;

import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities.Cat;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities.Owner;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.models.Color;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.repositories.CatRepository;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.repositories.OwnerRepository;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.CatDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.exceptions.EntityNotFoundException;
import ru.ermolaayyyyyyy.leschats.servicelayer.exceptions.InvalidAttributeException;
import ru.ermolaayyyyyyy.leschats.servicelayer.mapping.CatDtoMapping;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.interfaces.CatService;


import java.time.LocalDate;
import java.util.List;
@ExtensionMethod(CatDtoMapping.class)
@Service
public class CatServiceImpl implements CatService {

    private final CatRepository catRepository;
    private final OwnerRepository ownerRepository;

    @Autowired
    public CatServiceImpl(CatRepository catRepository, OwnerRepository ownerRepository){
        this.catRepository = catRepository;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public CatDto findCatById(int id) {
        return getCatById(id).asDto();
    }

    @Override
    public CatDto saveCat(String name, LocalDate birthDate, String breed, String color, int ownerId) {
        Color colorEnum = getColor(color);
        Owner owner = getOwnerById(ownerId);
        Cat cat = new Cat(name, birthDate, breed, colorEnum, owner);
        catRepository.saveAndFlush(cat);
        owner.addCat(cat);
        ownerRepository.save(owner);
        return cat.asDto();
    }

    @Override
    public void deleteCat(int id) {
        Cat cat = getCatById(id);
        for (Cat friend : cat.getFriends()){
            friend.getFriends().remove(cat);
            catRepository.save(friend);
        }
        catRepository.deleteById(id);
    }

    @Override
    public CatDto updateCat(String name, LocalDate birthDate, String breed, String color, int ownerId, int id) {
        Owner owner = getOwnerById(ownerId);
        Color colorEnum = getColor(color);
        Cat cat = getCatById(id);
        cat.update(name, birthDate, breed, colorEnum, owner);
        catRepository.save(cat);
        return cat.asDto();
    }

    @Override
    public List<CatDto> findAllCats() {
        return catRepository.findAll().stream().map(x -> x.asDto()).toList();
    }

    @Override
    public List<CatDto> findFilteredCats(Specification<Cat> spec) {
        return catRepository.findAll(spec).stream().map(CatDtoMapping::asDto).toList();
    }

    @Override
    public void addFriend(int id1, int id2) {
        Cat cat1 = getCatById(id1);
        Cat cat2 = getCatById(id2);
        if(cat1.getFriends().contains(cat2)){
            throw InvalidAttributeException.friendAlreadyExist(id1, id2);
        }
        cat1.addFriend(cat2);
        cat2.addFriend(cat1);
        catRepository.save(cat1);
        catRepository.save(cat2);
    }

    private Color getColor(String color){
        Color colorEnum = Color.findColor(color);
        if (colorEnum == null){
            throw InvalidAttributeException.colorDoesNotExist(color);
        }
        return colorEnum;
    }

    private Cat getCatById(int id){
        return catRepository.findById(id).orElseThrow(() -> EntityNotFoundException.catDoesNotExist(id));
    }

    private Owner getOwnerById(int id){
        return ownerRepository.findById(id).orElseThrow(() -> EntityNotFoundException.ownerDoesNotExist(id));
    }
}

package ru.ermolaayyyyyyy.leschats.servicelayer.services.interfaces;

import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities.Cat;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.CatDto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CatService {
    CatDto findCatById(int id);
    CatDto saveCat(String name, LocalDate birthDate, String breed, String color, int ownerId);
    void deleteCat(int id);
    CatDto updateCat(String name, LocalDate birthDate, String breed, String color, int ownerId, int id);
    List<CatDto> findAllCats();
    List<CatDto> findFilteredCats(Specification<Cat> spec);
    void addFriend(int id1, int id2);
}

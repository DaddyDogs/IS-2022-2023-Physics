package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.repositories.CatRepository;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.repositories.OwnerRepository;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.repositories.UserRepository;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.CatDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.OwnerDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.exceptions.InvalidAttributeException;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.implementations.CatServiceImpl;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.implementations.OwnerServiceImpl;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.implementations.UserService;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.interfaces.CatService;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.interfaces.OwnerService;

import java.time.LocalDate;
import java.util.Calendar;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext
public class CatServicesTest {
    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private CatRepository catRepository;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    public BCryptPasswordEncoder bCryptPasswordEncoder;
    @Test
    void addOwnerAddCat() {
        OwnerService ownerService = new OwnerServiceImpl(ownerRepository);
        LocalDate birthDate = LocalDate.of(2000, Calendar.NOVEMBER, 15);
        OwnerDto ownerDto = ownerService.saveOwner("Roman", birthDate);
        OwnerDto owner = ownerService.findOwnerById(ownerDto.id());
        Assertions.assertEquals(owner.name(), "Roman");
        Assertions.assertEquals(owner.birthDate(), birthDate);
        CatService catService = new CatServiceImpl(catRepository, ownerRepository);
        CatDto catDto = catService.saveCat("Hasan", birthDate, "Evropeoid", "BLACK", owner.id());
        CatDto cat = catService.findCatById(catDto.id());
        Assertions.assertEquals(cat.name(), "Hasan");
        Assertions.assertEquals(cat.birthDate(), birthDate);
        Assertions.assertEquals(cat.breed(), "Evropeoid");
        Assertions.assertEquals(cat.color(), "BLACK");
    }

    @Test
    void addFriend_friendsListContainsCat() {
        OwnerService ownerService = new OwnerServiceImpl(ownerRepository);
        LocalDate birthDate = LocalDate.of(2000, Calendar.NOVEMBER, 15);
        OwnerDto ownerDto = ownerService.saveOwner("Roman", birthDate);
        CatService catService = new CatServiceImpl(catRepository, ownerRepository);
        CatDto catDto = catService.saveCat("Hasan", birthDate, "Evropeoid", "BLACK", ownerDto.id());
        CatDto catDto1 = catService.saveCat("Karim", birthDate, "Dvoroviy", "RED", ownerDto.id());
        catService.addFriend(catDto.id(), catDto1.id());
        Assertions.assertTrue(catService.findCatById(catDto.id()).friends().contains(catDto1.id()));
        Assertions.assertTrue(catService.findCatById(catDto1.id()).friends().contains(catDto.id()));
    }

    @Test
    void deleteCat_catDeletedFromFriendsAndOwnersList(){
        OwnerService ownerService = new OwnerServiceImpl(ownerRepository);
        LocalDate birthDate = LocalDate.of(2000, Calendar.NOVEMBER, 15);
        OwnerDto ownerDto = ownerService.saveOwner("Roman", birthDate);
        CatService catService = new CatServiceImpl(catRepository, ownerRepository);
        CatDto catDto = catService.saveCat("Hasan", birthDate, "Evropeoid", "BLACK", ownerDto.id());
        CatDto catDto2 = catService.saveCat("Karim", birthDate, "Dvoroviy", "RED", ownerDto.id());
        catService.addFriend(catDto.id(), catDto2.id());
        catService.deleteCat(catDto.id());
        Assertions.assertTrue(catService.findCatById(catDto2.id()).friends().isEmpty());
        Assertions.assertEquals(1, ownerService.findOwnerById(ownerDto.id()).cats().size());
    }

    @Test
    void addCatWithInvalidColor_ThrowsInvalidAttributeException(){
        OwnerService ownerService = new OwnerServiceImpl(ownerRepository);
        LocalDate birthDate = LocalDate.of(2000, Calendar.NOVEMBER, 15);
        OwnerDto ownerDto = ownerService.saveOwner("Roman", birthDate);
        CatService catService = new CatServiceImpl(catRepository, ownerRepository);
        Assertions.assertThrows(InvalidAttributeException.class, () -> catService.saveCat("Hasan", birthDate, "Evropeoid", "ORANGE", ownerDto.id()));
    }
}

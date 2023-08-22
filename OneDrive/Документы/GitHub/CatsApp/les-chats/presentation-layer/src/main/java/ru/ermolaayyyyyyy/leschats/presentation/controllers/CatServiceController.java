package ru.ermolaayyyyyyy.leschats.presentation.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import net.kaczmarzyk.spring.data.jpa.domain.*;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities.Cat;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities.User;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.models.Role;
import ru.ermolaayyyyyyy.leschats.presentation.dto.ControllerCatDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.CatDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.exceptions.AccessDeniedException;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.implementations.UserService;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.interfaces.CatService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@RestController
@RestControllerAdvice
@SecurityRequirement(name="cats-api")
public class CatServiceController {
    @Autowired
    private final CatService catService;
    @Autowired
    private final UserService userService;

    public CatServiceController(CatService catService, UserService userService) {
        this.catService = catService;
        this.userService = userService;
    }

    @PostMapping(value = "/cats", consumes = "application/json")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<CatDto> addCat(@Valid @RequestBody ControllerCatDto catDto){
        String username = userService.getUsernameByOwnerId(catDto.ownerId());
        if(!checkIdentity(catDto.ownerId())){
            throw AccessDeniedException.noAccessForUserException(username);
        }
        CatDto catDtoSaved = catService.saveCat(catDto.name(), catDto.birthDate(), catDto.breed(), catDto.color(), catDto.ownerId());
        return new ResponseEntity<>(catDtoSaved, HttpStatus.CREATED);
    }


    @DeleteMapping("/cats/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> deleteCat(@PathVariable("id") @Min(1) int id){
        if(!checkIdentity(catService.findCatById(id).ownerId())){
            throw AccessDeniedException.noAccessForCatException(id);
        }
        catService.deleteCat(id);
        return new ResponseEntity<>("Cat was deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/cats")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<CatDto> updateCat(@Valid @RequestBody CatDto catDto) {
        CatDto retrievedCat = catService.findCatById(catDto.id());
        int ownerId = catDto.ownerId();
        if (!checkIdentity(ownerId)){
            throw AccessDeniedException.noAccessForCatException(catDto.id());
        }
        if (ownerId == 0){
            ownerId = retrievedCat.ownerId();
        }
        String color = catDto.color();
        if (color == null){
            color = retrievedCat.color();
        }
        CatDto updatedCatDto = catService.updateCat(catDto.name(), catDto.birthDate(), catDto.breed(), color, ownerId, catDto.id());
        return new ResponseEntity<>(updatedCatDto, HttpStatus.OK);
    }
    @Transactional
    @GetMapping("/cats")
    public ResponseEntity<List<CatDto>> findAllCats(
        @Conjunction(value = {
                @Or({@Spec(path = "birthDate", params = "birthdate", spec = EqualDay.class),
                    @Spec(path = "birthDate", params = {"birthDateFrom", "birthDateTo"}, spec = Between.class)}),

                @Or({@Spec(path = "owner.Id", params = "ownerIdLessThan", spec = LessThan.class),
                    @Spec(path = "owner.Id", params = "ownerIdGreaterThan", spec = GreaterThan.class),
                    @Spec(path = "owner.Id", params = "ownerId", spec = Equal.class)})},

                and =  {@Spec(path = "name", params = "name", spec = Like.class),
                        @Spec(path = "breed", params = "breed", spec = Like.class),
                        @Spec(path = "color", params = "color", paramSeparator = ',', spec = In.class),
                        @Spec(path = "id", params = "id", spec = Equal.class)})
        Specification<Cat> spec)
    {
        List<CatDto> cats = catService.findFilteredCats(spec);
        List<CatDto> availableCats = new ArrayList<CatDto>();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            if (((UserDetails) principal).getAuthorities().contains(Role.ROLE_ADMIN)){
                return new ResponseEntity<>(cats, HttpStatus.OK);
            }
            String userName = ((UserDetails) principal).getUsername();
            UserDetails userDetails = userService.loadUserByUsername(userName);
            if(userDetails instanceof User) {
                var userCats = ((User) userDetails).getOwner().getCats();
                for (CatDto c : cats) {
                    for (Cat c1 : userCats){
                        if (c.id() == c1.getId()){
                            availableCats.add(c);
                        }
                    }
                }
            }
        }
        else {
            throw AccessDeniedException.notAuthorizedException();
        }
        return new ResponseEntity<>(availableCats, HttpStatus.OK);
    }

    @PutMapping("/friends/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> addFriend(@PathVariable @Min(1) int id, @RequestBody @Min(1) Integer friendId) {
        CatDto retrievedCat1 = catService.findCatById(id);
        CatDto retrievedCat2 = catService.findCatById(friendId);

        boolean access = checkIdentity(retrievedCat1.ownerId());
        if(!access){
            throw AccessDeniedException.noAccessForCatException(id);
        }
        access = checkIdentity(retrievedCat2.ownerId());
        if(!access){
            throw AccessDeniedException.noAccessForCatException(friendId);
        }

        catService.addFriend(id, friendId);
        return new ResponseEntity<>("Cats with id " + id + " and " + friendId + " are friends now", HttpStatus.OK);
    }

    private boolean checkIdentity(int ownerId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            Collection<? extends GrantedAuthority> roles = ((UserDetails) principal).getAuthorities();
            if (!roles.contains(Role.ROLE_ADMIN)) {
                String username = ((UserDetails) principal).getUsername();
                return Objects.equals(username, userService.getUsernameByOwnerId(ownerId));
            } else {
                return true;
            }
        }
        else {
            throw AccessDeniedException.notAuthorizedException();
        }
    }
}
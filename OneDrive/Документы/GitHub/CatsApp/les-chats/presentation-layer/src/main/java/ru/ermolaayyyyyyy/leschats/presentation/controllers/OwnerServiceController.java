package ru.ermolaayyyyyyy.leschats.presentation.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.experimental.ExtensionMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities.User;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.models.Role;
import ru.ermolaayyyyyyy.leschats.presentation.dto.ControllerOwnerDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.IOwnerDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.OwnerDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.exceptions.AccessDeniedException;
import ru.ermolaayyyyyyy.leschats.servicelayer.mapping.OwnerDtoMapping;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.implementations.UserService;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.interfaces.OwnerService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RestController
@ExtensionMethod(OwnerDtoMapping.class)
@SecurityRequirement(name="cats-api")
public class OwnerServiceController {
    private final OwnerService ownerService;
    private final UserService userService;

    public OwnerServiceController(OwnerService ownerService, UserService userService) {
        this.ownerService = ownerService;
        this.userService = userService;
    }


    @GetMapping("/owners/{id}")
    public ResponseEntity<IOwnerDto> findOwnerById(@PathVariable @Min(1) int id) {
        User user = getCurrentUser();
        Role role = user.getRole();
        OwnerDto ownerDto = ownerService.findOwnerById(id);
        IOwnerDto ownerDtoAbstract = ownerService.findOwnerById(id);
        if (role == Role.ROLE_USER){
            ownerDtoAbstract = ownerDto.asSecureDto();
        }
        if (Objects.equals(userService.getUsernameByOwnerId(id), user.getUsername())){
            ownerDtoAbstract = ownerDto;
        }
        return new ResponseEntity<>(ownerDtoAbstract, HttpStatus.OK);
    }

    @PostMapping(value = "/owners", consumes = "application/json")
    @Hidden
    public ResponseEntity<OwnerDto> saveOwner(@Valid @RequestBody ControllerOwnerDto ownerDto) {
        OwnerDto ownerDtoSaved = ownerService.saveOwner(ownerDto.name(), ownerDto.birthDate());
        return new ResponseEntity<>(ownerDtoSaved, HttpStatus.CREATED);
    }

    @DeleteMapping("/owners/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteOwner(@PathVariable @Min(1) int id) {
        checkAccessForUser(id);
        User user = (User) userService.loadUserByUsername(userService.getUsernameByOwnerId(id));
        userService.deleteUser(user.getId());
        return new ResponseEntity<>("Owner was deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/owners")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OwnerDto> updateOwner(@Valid @RequestBody OwnerDto ownerDto) {
        checkAccessForUser(ownerDto.id());
        OwnerDto updatedOwnerDto = ownerService.updateOwner(ownerDto.name(), ownerDto.birthDate(), ownerDto.id());
        return new ResponseEntity<>(updatedOwnerDto, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/owners")
    public ResponseEntity<List<IOwnerDto>> findAllOwners() {
        User user = getCurrentUser();
        Role role = user.getRole();
        List<OwnerDto> ownerDtos = ownerService.findAllOwners();
        List<IOwnerDto> ownerDtosAbstract = new ArrayList<>();
        if (role == Role.ROLE_USER){
            for (OwnerDto od : ownerDtos){
                if (Objects.equals(userService.getUsernameByOwnerId(od.id()), user.getUsername())){
                    ownerDtosAbstract.add(od);
                }
                else{
                    ownerDtosAbstract.add(od.asSecureDto());
                }
            }
        }
        else{
            ownerDtosAbstract.addAll(ownerDtos);
        }
        return new ResponseEntity<>(ownerDtosAbstract, HttpStatus.OK);
    }

    private User getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        throw AccessDeniedException.notAuthorizedException();
    }

    private boolean checkIdentity(User user, int ownerId){
            Collection<? extends GrantedAuthority> roles = user.getAuthorities();
            if (!roles.contains(Role.ROLE_ADMIN)) {
                String username = user.getUsername();
                return Objects.equals(username, userService.getUsernameByOwnerId(ownerId));
            } else {
               return true;
            }
    }
    private void checkAccessForUser(int id){
        if (!checkIdentity(getCurrentUser(), id)){
            throw AccessDeniedException.noAccessForUserException(userService.getUsernameByOwnerId(id));
        }
    }
}

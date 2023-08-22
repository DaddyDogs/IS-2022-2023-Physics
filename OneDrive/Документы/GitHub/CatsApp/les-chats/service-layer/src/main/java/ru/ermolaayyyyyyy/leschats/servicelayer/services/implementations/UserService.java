package ru.ermolaayyyyyyy.leschats.servicelayer.services.implementations;

import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities.User;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.models.Role;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.repositories.OwnerRepository;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.repositories.UserRepository;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.OwnerDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.dto.UserDto;
import ru.ermolaayyyyyyy.leschats.servicelayer.exceptions.EntityNotFoundException;
import ru.ermolaayyyyyyy.leschats.servicelayer.exceptions.InvalidAttributeException;
import ru.ermolaayyyyyyy.leschats.servicelayer.mapping.OwnerDtoMapping;
import ru.ermolaayyyyyyy.leschats.servicelayer.mapping.UserDtoMapping;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@ExtensionMethod(UserDtoMapping.class)
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final OwnerServiceImpl ownerService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, OwnerRepository ownerRepository, OwnerServiceImpl ownerService) {
        this.userRepository = userRepository;
        this.ownerService = ownerService;
    }

    public UserDto saveUser(String login, String password, String name, LocalDate birthdate, String role) {
        if (userRepository.findByLogin(login) != null) {
            throw InvalidAttributeException.loginAlreadyExist(login);
        }

        OwnerDto ownerDto = ownerService.saveOwner(name, birthdate);
        Role role1 = getRole(role);
        User user = new User(ownerService.getOwnerById(ownerDto.id()), login, bCryptPasswordEncoder.encode(password), role1);
        userRepository.saveAndFlush(user);
        return user.asDto(name, birthdate);
    }
    public void deleteUser(int id) {
        getUserById(id);
        userRepository.deleteById(id);
    }

    public UserDto findUserById(int id) {
        User user = getUserById(id);
        return user.asDto(user.getOwner().getName(), user.getOwner().getBirthDate());
    }
    public UserDto updateUser(String password, String login, String name, LocalDate birthDate, int id) {
        User user = userRepository.findByLogin(login);
        User user2 = getUserById(id);
        if (user != null && user.getId() != id) {
            throw InvalidAttributeException.loginAlreadyExist(login);
        }

        user2.update(login, bCryptPasswordEncoder.encode(password));
        userRepository.save(user2);

        ownerService.getOwnerById(user2.getOwner().getId()).update(name, birthDate);

        return user2.asDto(user2.getOwner().getName(), user2.getOwner().getBirthDate());
    }

    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream().map(x -> x.asDto(x.getOwner().getName(), x.getOwner().getBirthDate())).toList();
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username);
        if (user == null){
            throw EntityNotFoundException.userDoesNotExist(username);
        }
        return user;
    }

    public String getUsernameByOwnerId(int id){
        User user = userRepository.findByOwnerId(id);
        if (user == null){
            throw EntityNotFoundException.ownerDoesNotExist(id);
        }
        return user.getUsername();
    }
    private User getUserById(int id){
        return userRepository.findById(id).orElseThrow(() -> EntityNotFoundException.userDoesNotExist(id));
    }
    private Role getRole(String role){
        Role roleEnum = Role.findRole(role);
        if (roleEnum == null){
            throw InvalidAttributeException.roleDoesNotExist(role);
        }
        return roleEnum;
    }
}

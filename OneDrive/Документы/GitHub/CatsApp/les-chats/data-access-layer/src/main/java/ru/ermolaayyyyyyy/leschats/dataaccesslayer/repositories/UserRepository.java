package ru.ermolaayyyyyyy.leschats.dataaccesslayer.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    User findByLogin(String login);
    User findByOwnerId(int id);
}

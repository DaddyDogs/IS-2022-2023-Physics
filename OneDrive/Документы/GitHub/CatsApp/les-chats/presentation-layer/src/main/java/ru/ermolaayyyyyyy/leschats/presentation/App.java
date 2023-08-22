package ru.ermolaayyyyyyy.leschats.presentation;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.models.Role;
import ru.ermolaayyyyyyy.leschats.servicelayer.services.implementations.UserService;

import java.time.LocalDate;

@SpringBootApplication(scanBasePackages = {"ru.ermolaayyyyyyy"})
@EntityScan("ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities")
@EnableJpaRepositories("ru.ermolaayyyyyyy.leschats.dataaccesslayer.repositories")
@SecurityScheme(name = "cats-api", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class App {
    public static void main(String[] args){
        ApplicationContext context = SpringApplication.run(App.class, args);
    }
}

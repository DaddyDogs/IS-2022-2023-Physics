package tests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.repositories.CatRepository;
import ru.ermolaayyyyyyy.leschats.dataaccesslayer.repositories.OwnerRepository;

@SpringBootApplication(scanBasePackages = {"ru.ermolaayyyyyyy"})
@EnableJpaRepositories(basePackages = "ru.ermolaayyyyyyy.leschats.dataaccesslayer.repositories")
@EntityScan("ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities")
public class Application {
}

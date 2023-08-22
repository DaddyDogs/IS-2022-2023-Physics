package tests;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = {"ru.ermolaayyyyyyy"})
@EntityScan("ru.ermolaayyyyyyy.leschats.dataaccesslayer.entities")
public class TestConfig {
}

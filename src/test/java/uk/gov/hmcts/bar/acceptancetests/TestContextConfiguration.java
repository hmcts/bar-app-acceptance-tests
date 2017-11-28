package uk.gov.hmcts.bar.acceptancetests;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@EnableJpaRepositories
@ComponentScan(basePackages= {"uk.gov.hmcts.bar"})
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class TestContextConfiguration {
}

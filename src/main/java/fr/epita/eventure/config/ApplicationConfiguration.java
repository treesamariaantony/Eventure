package fr.epita.eventure.config;



import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "fr.epita.eventure.repository")
public class ApplicationConfiguration {
}

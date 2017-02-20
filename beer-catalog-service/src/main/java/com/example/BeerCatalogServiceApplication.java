package com.example;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.stream.Stream;

@EnableDiscoveryClient
@SpringBootApplication
public class BeerCatalogServiceApplication {
    @Bean
    CommandLineRunner demoData(BeerRepository repository) {
        return args -> {
            Stream.of("Budweiser", "Miller", "Samuel Adams", "Corona",
                    "Heineken", "Dos Equis", "Fat Tire")
                    .map(beerName -> new Beer(beerName))
                    .forEach(repository::save);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(BeerCatalogServiceApplication.class, args);
    }
}

@RepositoryRestResource
interface BeerRepository extends JpaRepository<Beer, Long> {}

@Entity
@Data
@NoArgsConstructor
class Beer {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Beer(String name) {
        this.name = name;
    }
}
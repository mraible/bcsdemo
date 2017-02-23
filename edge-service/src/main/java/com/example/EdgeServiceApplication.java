package com.example;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@EnableFeignClients
@EnableZuulProxy
@SpringBootApplication
public class EdgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdgeServiceApplication.class, args);
    }
}

@FeignClient("beer-catalog-service")
interface CraftBeerClient {
    @GetMapping("/beers")
    Resources<Resource<Beer>> read();
}

@RestController
class CraftBeerController {
    private CraftBeerClient client;

    public CraftBeerController(CraftBeerClient client) {
        this.client = client;
    }

    @GetMapping("/good-beers")
    //@CrossOrigin("http://localhost:4200")
    @HystrixCommand(fallbackMethod = "fallBack")
    public Collection<Map<String, String>> goodBeers() {
        return this.client
                .read()
                .getContent()
                .stream()
                .filter(beerResource -> !beerResource
                        .getContent()
                        .getName()
                        .equalsIgnoreCase("budweiser"))
                .map(beerResource -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", beerResource.getContent().getName());
                    map.put("imageUrl", beerResource.getLink("self").getHref() + ".png");
                    return map;
                })
                .collect(Collectors.toList());
    }

    public Collection<Map<String, String>> fallBack() {
        return new ArrayList<>();
    }
}

@Data
@NoArgsConstructor
class Beer {
    private String name, imageUrl;
}

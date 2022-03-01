package com.first.demo.database;

import com.first.demo.models.Product;
import com.first.demo.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Now connect with my sql using JPA
/*
docker run -d --rm -name mysql-spring-boot-tutorial \
-e MYSQL_ROOT_PASSWORD= \
-e MYSQL_USER=hanght \
-e MYSQL_PASSWORD= \
-e MYSQL_DATABASE= test_db \
-p 3309:3306 \
-- volume mysql-spring-boot-tutorial-volume:/var/lib/mysql \
mysql:latest

mysql -h localhost -P 3309 --protoco=tcp -u hanght -p

*/

@Configuration
public class Database {
    @Bean
    CommandLineRunner initDatabase(ProductRepository repository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Product product1 = new Product("Dell", 2022, 150000.0, "");
                Product product2 = new Product("Dell", 2022, 150000.0, "");
                Product product3 = new Product("Dell", 2022, 150000.0, "");
                System.out.println("inster data" + repository.save(product1));
                System.out.println("inster data" + repository.save(product2));
                System.out.println("inster data" + repository.save(product3));
            }
        };
    }
}

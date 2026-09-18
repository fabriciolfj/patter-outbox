package com.github.fabriciolfj.outbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableKafka
@EnableScheduling
@EnableJpaRepositories(basePackages = {"com.github.fabriciolfj.outbox.repository"})
@EntityScan(basePackages = {"com.github.fabriciolfj.outbox.entity"})
@SpringBootApplication
public class OutboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(OutboxApplication.class, args);
	}

}

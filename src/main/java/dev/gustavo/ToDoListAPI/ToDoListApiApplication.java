package dev.gustavo.ToDoListAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "dev.gustavo.ToDoListAPI.repositories.interfaces")
public class ToDoListApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToDoListApiApplication.class, args);
	}

}

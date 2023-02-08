package com.ishlaw.crudservice;


import com.ishlaw.crudservice.configuration.Jpaconfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(Jpaconfiguration.class)
@SpringBootApplication(scanBasePackages = {"com.ishlaw.crudservice"})
public class CrudserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudserviceApplication.class, args);

	}

}

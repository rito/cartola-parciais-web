package br.com.devgeek.cartolaparciais;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CartolaParciaisWebApplication {

	public static void main(String[] args){
		SpringApplication.run(CartolaParciaisWebApplication.class, args);
	}
}
package br.com.ucs.laboratorio.gestao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GestaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestaoApplication.class, args);
	}

}

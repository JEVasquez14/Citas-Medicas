package com.unimagdalena.citas;

import org.springframework.boot.SpringApplication;

public class TestCitasApplication {

	public static void main(String[] args) {
		SpringApplication.from(CitasApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

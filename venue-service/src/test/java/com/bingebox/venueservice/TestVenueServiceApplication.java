package com.bingebox.venueservice;

import org.springframework.boot.SpringApplication;

public class TestVenueServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(VenueServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

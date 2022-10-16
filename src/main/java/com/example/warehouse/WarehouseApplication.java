package com.example.warehouse;

import com.example.warehouse.model.Automation;
import com.example.warehouse.repository.AutomationRestRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class WarehouseApplication {


	public static void main(String[] args) {
		SpringApplication.run(WarehouseApplication.class, args);
	}

}

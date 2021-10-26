package com.zctest.testprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class TestProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestProviderApplication.class, args);
	}


	@GetMapping("/hi")
	public String test(@RequestParam("name") String name){
		return "hi " +name;
	}
}

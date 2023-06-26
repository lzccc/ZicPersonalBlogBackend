package com.zic.springboot.demo.zicCoolApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//The scanBasePackages here is optional. Only need to be add if you have other packages outsides of
//the base packages that need to be scanned.
@SpringBootApplication(scanBasePackages = {"com.zic.springboot.demo.zicCoolApp"})
public class ZicCoolAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZicCoolAppApplication.class, args);
	}

}

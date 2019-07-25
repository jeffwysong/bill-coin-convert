package com.wysong.billcoinconvert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BillCoinConvertApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillCoinConvertApplication.class, args);
	}

}

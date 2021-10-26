package com.zctest.ssotest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class SsoTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsoTestApplication.class, args);
	}

}

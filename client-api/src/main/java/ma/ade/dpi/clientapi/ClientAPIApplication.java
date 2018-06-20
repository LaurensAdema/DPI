package ma.ade.dpi.clientapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ClientAPIApplication {
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(ClientAPIApplication.class, args);
	}
}

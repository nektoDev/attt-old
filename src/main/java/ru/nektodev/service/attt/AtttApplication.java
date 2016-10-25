package ru.nektodev.service.attt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.nektodev.notification.client.NotificationServiceClient;

@SpringBootApplication
@EnableScheduling
@Import(NotificationServiceClient.class)
public class AtttApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtttApplication.class, args);
	}

}

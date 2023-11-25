package yuzu.ticktaskbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TicktaskBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicktaskBotApplication.class, args);
	}

}

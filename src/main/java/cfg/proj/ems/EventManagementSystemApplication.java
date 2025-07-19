package cfg.proj.ems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"cfg.proj"})
@EntityScan("cfg.proj")
@EnableJpaRepositories("cfg.proj")
public class EventManagementSystemApplication {

	public static void main(String[] args) {
	ConfigurableApplicationContext context=	SpringApplication.run(EventManagementSystemApplication.class, args);
		
		
	}

}

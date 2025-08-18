package hexagon_talent.geundaero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class GeundaeroApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeundaeroApplication.class, args);
	}

}

package ski.crunch.tools;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ToolsApplication {

	public static void main(String[] args) {

		System.exit(SpringApplication.exit(SpringApplication.run(ToolsApplication.class, args)));
	}


}

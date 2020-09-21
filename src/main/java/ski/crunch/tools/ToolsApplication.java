package ski.crunch.tools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ToolsApplication {

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(
				new SpringApplicationBuilder(ToolsApplication.class)
						.logStartupInfo(false)
						.run(args))
		);
	}


}

package chewyt.Template;

// import java.util.Collections;
// import org.springframework.boot.ApplicationArguments;
// import org.springframework.boot.DefaultApplicationArguments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemplateApplication.class, args);

		// Template for CLI configuration
		// SpringApplication app = new SpringApplication(TemplateApplication.class);

		// String port = "3000";
		// ApplicationArguments cliOpts = new DefaultApplicationArguments(args);
		// if (cliOpts.containsOption("port")) {
		// port = cliOpts.getOptionValues("port").get(0);
		// }
		// app.setDefaultProperties(
		// Collections.singletonMap("server.port",port)
		// );
		// System.out.printf("Application started on port %s\n", port);
		// app.run(args);
	}

}

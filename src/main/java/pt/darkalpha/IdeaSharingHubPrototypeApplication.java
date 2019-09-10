package pt.darkalpha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pt.darkalpha.auth.SecurityConfig;

@SpringBootApplication
public class IdeaSharingHubPrototypeApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdeaSharingHubPrototypeApplication.class, args);
	}

	protected Class<?>[] getRootConfigClasses() {
        return new Class[] {SecurityConfig.class};
    }
	
}

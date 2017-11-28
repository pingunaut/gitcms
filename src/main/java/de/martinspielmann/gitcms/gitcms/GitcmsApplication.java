package de.martinspielmann.gitcms.gitcms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ContextConfiguration;

@SpringBootApplication
@ContextConfiguration("classpath:/applicationContext-CrowdRestClient.xml")
public class GitcmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitcmsApplication.class, args);
	}
}

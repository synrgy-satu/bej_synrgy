package com.example.finalProject_synrgy;

//import com.example.finalProject_synrgy.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableConfigurationProperties({
//		FileStorageProperties.class
//})

@EnableJpaAuditing
@EnableScheduling
public class FinalProjectSynrgyApplication {


	public static void main(String[] args) {
		SpringApplication.run(FinalProjectSynrgyApplication.class, args);
	}

}

package eu.crg.ega.configservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.map.repository.config.EnableMapRepositories;

@Profile("mem")
@Configuration
@EnableMapRepositories(basePackages = "eu.crg.ega.configservice.repository.memory")
public class MemoryConfig {

}

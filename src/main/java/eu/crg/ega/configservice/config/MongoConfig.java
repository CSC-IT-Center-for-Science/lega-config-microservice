package eu.crg.ega.configservice.config;

import com.mongodb.MongoClientURI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Profile("mongo")
@Configuration
@EnableMongoRepositories(basePackages = "eu.crg.ega.configservice.repository.mongo")
public class MongoConfig {

  @Autowired
  private Environment env;

  @Bean
  public MongoDbFactory mongoDbFactory() throws Exception {
    return new SimpleMongoDbFactory(new MongoClientURI(env.getProperty("spring.data.mongodb.uri")));
  }

  @Bean
  public MongoTemplate mongoTemplate() throws Exception {
    return new MongoTemplate(mongoDbFactory());
  }
}

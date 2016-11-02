package eu.crg.ega.configservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan({"eu.crg.ega.configservice", "eu.crg.ega.microservice"})
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class
    , DataSourceTransactionManagerAutoConfiguration.class
    , HibernateJpaAutoConfiguration.class
    , MongoAutoConfiguration.class
    , MongoRepositoriesAutoConfiguration.class
    , MongoDataAutoConfiguration.class})
public class Application implements CommandLineRunner {

  @Value("${service.name}")
  private String serviceName;

  @PostConstruct
  public void initServiceName() {
    System.setProperty("service.name", serviceName);
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    System.out.println("Welcome to " + this.getClass().getPackage());

  }
}

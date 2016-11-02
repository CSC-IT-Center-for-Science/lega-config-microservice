package eu.crg.ega.configservice;

import eu.crg.ega.configservice.model.ConfigModel;
import eu.crg.ega.configservice.repository.ConfigRepository;
import eu.crg.ega.microservice.enums.ServiceType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

import lombok.extern.log4j.Log4j;

@Profile("fill")
@Log4j
@Component
public class FillRepo implements ApplicationListener {

  @Autowired
  ConfigRepository repository;

  @Override
  public void onApplicationEvent(ApplicationEvent applicationEvent) {
    if (applicationEvent instanceof ApplicationReadyEvent) {
      log.debug("Filling information");
      ConfigModel config =
          new ConfigModel("configservice1", "configservice", "v1", "app01", "9000",
              ServiceType.CONFIG, "");
      ConfigModel session =
          new ConfigModel("sessionservice1", "sessionservice", "v1", "app01", "9200",
              ServiceType.SESSION, "");
      ConfigModel worker =
          new ConfigModel("workerservice1", "workerservice", "v1", "c01", "9850",
              ServiceType.WORKER, "");

      Stream.of(config, session, worker).forEach(n -> repository.save(n));
      log.debug("Done");
    }
  }
}

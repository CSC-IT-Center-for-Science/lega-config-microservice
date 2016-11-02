package eu.crg.ega.configservice.repository;

import java.util.List;

import eu.crg.ega.configservice.model.ConfigModel;
import eu.crg.ega.microservice.enums.ServiceType;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ConfigRepository extends CrudRepository<ConfigModel, String> {

  ConfigModel findByName(String name);

  ConfigModel findByServiceId(String serviceId);

  List<ConfigModel> findByType(ServiceType type);

  List<ConfigModel> findByTypeAndVersion(ServiceType type, String version);

}

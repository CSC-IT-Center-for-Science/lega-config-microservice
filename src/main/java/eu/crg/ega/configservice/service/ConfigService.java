package eu.crg.ega.configservice.service;

import eu.crg.ega.configservice.model.ConfigModel;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ConfigService {

  @PreAuthorize("permitAll")
  List<ConfigModel> list(String type, String version);

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  ConfigModel save(ConfigModel cm);

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  ConfigModel update(String serviceId, ConfigModel cm);

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  void delete(String serviceID);

  @PreAuthorize("permitAll")
  ConfigModel get(String serviceId);

}

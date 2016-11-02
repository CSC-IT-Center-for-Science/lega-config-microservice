package eu.crg.ega.configservice.service;

import com.google.common.collect.Lists;

import eu.crg.ega.configservice.model.ConfigModel;
import eu.crg.ega.configservice.repository.ConfigRepository;
import eu.crg.ega.microservice.enums.ServiceType;
import eu.crg.ega.microservice.exception.NotFoundException;
import eu.crg.ega.microservice.util.CustomBeanUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {

  @Autowired
  private ConfigRepository repository;

  @Override
  public List<ConfigModel> list(String type, String version) {
    if (StringUtils.isEmpty(type) && StringUtils.isEmpty(version)) {
      return Lists.newArrayList(repository.findAll());
    }

    ServiceType serviceType = ServiceType.parse(type);

    ConfigModel model = null;
    List<ConfigModel> list = null;
    if (StringUtils.isEmpty(version)) {
      list = repository.findByType(serviceType);
    } else {
      list = repository.findByTypeAndVersion(serviceType, version);
    }
    if (list != null && !list.isEmpty()) {
      model = list.get(0);
    }
    return (model != null ? Arrays.asList(model) : new ArrayList<ConfigModel>());
  }

  @Override
  public ConfigModel save(ConfigModel cm) {

    String[] nullProperties = CustomBeanUtils.getNullPropertyNamesExcept(cm, "id");
    if (nullProperties.length != 0) {
      return repository.save(cm);
    } else {
      throw new RuntimeException("Properties missing: " + nullProperties.toString());
    }

  }

  @Override
  public void delete(String serviceId) {
    ConfigModel modelFound = repository.findByServiceId(serviceId);
    repository.delete(modelFound);
  }

  @Override
  public ConfigModel update(String serviceId, ConfigModel configModel) {
    ConfigModel modelFound = repository.findByServiceId(serviceId);
    if (modelFound != null) {
      CustomBeanUtils.copyNotNullProperties(configModel, modelFound);
      return repository.save(modelFound);
    } else {
      throw new NotFoundException("service with serviceId:" + serviceId + "was not found");
    }
  }

  @Override
  public ConfigModel get(String serviceId) {
    return repository.findByServiceId(serviceId);
  }

}

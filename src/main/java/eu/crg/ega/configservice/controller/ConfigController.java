package eu.crg.ega.configservice.controller;

import eu.crg.ega.configservice.model.ConfigModel;
import eu.crg.ega.configservice.service.ConfigService;
import eu.crg.ega.microservice.constant.CoreConstants;
import eu.crg.ega.microservice.constant.ParamName;
import eu.crg.ega.microservice.dto.Base;
import eu.crg.ega.microservice.dto.ServiceLocation;
import eu.crg.ega.microservice.enums.ServiceType;
import eu.crg.ega.microservice.util.Converter;
import eu.crg.ega.microservice.util.CustomBeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/services")
public class ConfigController {

  @Autowired
  private ConfigService configService;

  @RequestMapping(value = "/types", method = RequestMethod.GET)
  public Base<ServiceType> listTypes() {
    return new Base<>(Arrays.asList(ServiceType.values()));
  }

  @RequestMapping(value = "/types/{type}", method = RequestMethod.GET)
  public Base<ServiceLocation> listByType(
      @PathVariable(value = ParamName.TYPE) String type,
      @RequestParam(value = ParamName.VERSION, required = false) String version) {
    List<ConfigModel> configModels = configService.list(type, version);
    return new Base<>(Converter.convertList(ServiceLocation.class, configModels));
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public Base<ServiceLocation> list(
      @RequestParam(value = ParamName.TYPE, required = false) String type,
      @RequestParam(value = ParamName.VERSION, required = false) String version) {
    List<ConfigModel> configModels = configService.list(type, version);
    return new Base<>(Converter.convertList(ServiceLocation.class, configModels));
  }

  @RequestMapping(value = "/{serviceid}", method = RequestMethod.GET)
  public Base<ServiceLocation> getService(
      @PathVariable(value = ParamName.SERVICE_ID) String serviceId) {
    ConfigModel configModel = configService.get(serviceId);
    return new Base<>(Converter.convert(ServiceLocation.class, configModel));
  }

  @RequestMapping(value = "", method = RequestMethod.POST)
  public Base<ServiceLocation> register(@RequestBody ServiceLocation serviceLocation) {
    if (!CustomBeanUtils.hasNullProperties(serviceLocation)) {
      ConfigModel configModel =
          configService.save(Converter.convert(ConfigModel.class, serviceLocation));
      return new Base<>(Converter.convert(ServiceLocation.class, configModel));
    } else {
      throw new RuntimeException("Properties missing: "
          + Arrays.toString(CustomBeanUtils.getNullPropertyNames(serviceLocation)));
    }
  }

  @RequestMapping(value = "/{serviceid}", method = RequestMethod.PUT)
  public Base<ServiceLocation> update(@PathVariable(value = "serviceid") String serviceId,
                                      @RequestBody ServiceLocation serviceLocation) {

    ConfigModel configModel =
        configService.update(serviceId, Converter.convert(ConfigModel.class, serviceLocation));

    return new Base<>(Converter.convert(ServiceLocation.class, configModel));
  }

  @RequestMapping(value = "/{serviceid}", method = RequestMethod.DELETE)
  public Base<String> unregister(@PathVariable(value = "serviceid") String serviceId) {
    configService.delete(serviceId);
    return new Base<>(CoreConstants.OK);
  }

}

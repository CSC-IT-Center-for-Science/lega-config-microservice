package eu.crg.ega.configservice.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.crg.ega.microservice.enums.ServiceType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@Document
public class ConfigModel {

  @Id
  private String id;

  private String serviceId;
  private String name;
  private String server;
  private String port;
  private String version;
  private ServiceType type;
  private String baseUrl;

  public ConfigModel(String serviceId, String name, String version, String server, String port,
      ServiceType type, String baseUrl) {
    this.serviceId = serviceId;
    this.name = name;
    this.version = version;
    this.server = server;
    this.port = port;
    this.type = type;
    this.baseUrl = baseUrl;
  }

  @Override
  public String toString() {
    return String.format("Config[serviceid=%s, name='%s', server='%s', port='%s']", serviceId,
        name, server, port);
  }

}

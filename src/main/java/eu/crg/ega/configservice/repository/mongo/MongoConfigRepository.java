package eu.crg.ega.configservice.repository.mongo;

import eu.crg.ega.configservice.repository.ConfigRepository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile("mongo")
@Repository
public interface MongoConfigRepository extends ConfigRepository {

}

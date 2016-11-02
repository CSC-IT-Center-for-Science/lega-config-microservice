package eu.crg.ega.configservice.repository.memory;

import eu.crg.ega.configservice.repository.ConfigRepository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile("mem")
@Repository
public interface MemoryConfigRepository extends ConfigRepository {

}

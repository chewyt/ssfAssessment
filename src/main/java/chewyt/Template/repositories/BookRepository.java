package chewyt.Template.repositories;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.stereotype.Repository;
import static chewyt.Template.Constants.*;


@Repository
public class BookRepository {
    
    @Autowired
    RedisTemplate<String, String> template;

    private final Logger logger = Logger.getLogger(BookRepository.class.getName());
    

    //Saving as a JSON string
    public void save(String key, String jsonValue){
        logger.info("REDIS KEY SELECTOR: %s".formatted(REDIS_KEY));
        template.opsForValue().set(REDIS_KEY+normalise(key), jsonValue, 10L, TimeUnit.MINUTES);
    }

    
    // Getting the JSON string from JSON array
    public Optional<String> get(String key){
        String value = template.opsForValue().get(REDIS_KEY+normalise(key));
        return Optional.ofNullable(value);

    }

    private String normalise(String key){
        return key.toLowerCase().trim();
    }
}

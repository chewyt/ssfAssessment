package chewyt.Template.configurations;

import java.util.Optional;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
// import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

// import static chewyt.Template.Constants.*;

@Configuration
public class Config {

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private Optional<Integer> redisPort;

    // >>>>>>>> Uncomment/Comment
    @Value("${spring.redis.password}")
    private Optional<String> redisPassword;
    // >>>>>>>> Uncomment/Comment

    Logger logger = Logger.getLogger(Config.class.getName());

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort.get());

        // >>>>>>>> Uncomment/Comment either one
        // config.setPassword(ENV_REDISCLOUD);
        config.setPassword(redisPassword.get());
        // >>>>>>>> Uncomment/Comment either one

        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {

        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        // RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer(getClass().getClassLoader());

        // Ops for Value (Good for storing JSON string)
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        // Ops for Hash (DEFAULT: COMMENTED)
        // template.setHashKeySerializer(new StringRedisSerializer());
        // template.setHashValueSerializer(new StringRedisSerializer());

        template.setConnectionFactory(redisConnectionFactory());

        return template;
    }

}

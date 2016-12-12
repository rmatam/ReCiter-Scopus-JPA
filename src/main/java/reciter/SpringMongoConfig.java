package reciter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

@Configuration
@EnableMongoRepositories("reciter")
public class SpringMongoConfig extends AbstractMongoConfiguration {

    private String host = System.getenv("spring.data.mongodb.host");
    
    private Integer port = Integer.parseInt(System.getenv("spring.data.mongodb.port"));
    
    private String username = System.getenv("spring.data.mongodb.username");
    
    private String database = System.getenv("spring.data.mongodb.database");
    
    private String password = System.getenv("spring.data.mongodb.password");
    
	@Bean
	public GridFsTemplate gridFsTemplate() throws Exception {
		return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
	}

	@Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }
	
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
    
	@Override
	protected String getDatabaseName() {
		return database;
	}

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		List<ServerAddress> serverAddresses = new ArrayList<ServerAddress>();
		serverAddresses.add(new ServerAddress(host, port));
		List<MongoCredential> mongoCredentials = new ArrayList<MongoCredential>();
		mongoCredentials.add(MongoCredential.createCredential(username,database, password.toCharArray()));
		return new MongoClient(serverAddresses, mongoCredentials);
	}

	/**
	 * http://jonathan-whywecanthavenicethings.blogspot.com/2011/11/wheres-my-exceptions-spring-data.html
	 */
	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongo(), getDatabaseName());
		mongoTemplate.setWriteConcern(WriteConcern.ACKNOWLEDGED);
		return mongoTemplate;
	}    
}

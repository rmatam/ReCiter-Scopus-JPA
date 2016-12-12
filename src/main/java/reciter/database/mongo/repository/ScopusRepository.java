package reciter.database.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import reciter.database.mongo.model.ScopusArticleMongo;

public interface ScopusRepository extends MongoRepository<ScopusArticleMongo, Long> {

}

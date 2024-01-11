package learning.kirana_stores.repository;

import learning.kirana_stores.entities.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoreRepository extends MongoRepository<Post, String> {
}

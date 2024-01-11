package learning.kirana_stores.repository;

import learning.kirana_stores.entities.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserModel, String> {

    UserModel findByUsername (String username);
}

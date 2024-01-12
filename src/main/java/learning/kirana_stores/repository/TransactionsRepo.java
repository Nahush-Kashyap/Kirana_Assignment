package learning.kirana_stores.repository;

import learning.kirana_stores.entities.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionsRepo extends MongoRepository<Post,String> {

    @Query("{'dateTime':{'$gte': ?0,'$lte':?1}}")
    List<Post> getTransactionBetweenDates(LocalDateTime start, LocalDateTime end);

}

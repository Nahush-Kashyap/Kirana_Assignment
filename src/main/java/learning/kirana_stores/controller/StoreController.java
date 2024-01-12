package learning.kirana_stores.controller;

import io.github.bucket4j.Bucket;
import learning.kirana_stores.configuration.RateLimitConfig;
import learning.kirana_stores.model.posts.PostDTO;
import learning.kirana_stores.services.StoreServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/")
public class StoreController {

    @Autowired
    RateLimitConfig rateLimitConfig;
    private StoreServices storeServices;

    /**
     * Endpoint to redirect to Swagger UI.
     * @param response HttpServletResponse for redirecting.
     * @throws IOException IOException in case of redirection failure.
     */
    @ApiIgnore
    @RequestMapping(value = "/")
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html");
    }

    /**
     * Endpoint to get all transactions with rate limiting.
     * @return ResponseEntity with all transactions or rate limit exceeded message.
     */
    @GetMapping("/alldata")
    public ResponseEntity<?> getAllTransactions() {
        String id = "0";
        Bucket bucket = rateLimitConfig.resolveBucket(id);
        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok(storeServices.ReadAllData());
        } else {
            return ResponseEntity.ok("Rate limit exceeded for user");
        }
    }

    /**
     * Endpoint to add a new post with rate limiting.
     * @param postDTO PostDTO containing post details.
     * @return ResponseEntity with the added post or rate limit exceeded message.
     */
    @PostMapping("/post")
    @CrossOrigin
    public ResponseEntity<?> addPost(@RequestBody PostDTO postDTO) {
        String id = "1";
        Bucket bucket = rateLimitConfig.resolveBucket(id);
        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok(storeServices.addPost(postDTO));
        } else {
            return ResponseEntity.ok("Rate limit exceeded for user");
        }
    }

    /**
     * Endpoint to delete a post by its ID.
     * @param userId ID of the post to be deleted.
     * @return ResponseEntity with the result of the deletion operation.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") int userId) {
        return storeServices.deletePost(userId);
    }
}

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
@RequestMapping ("/")
public class StoreController {

    @Autowired
    RateLimitConfig rateLimitConfig;
    private StoreServices storeServices;

    /**
     * @param response
     * @throws IOException
     */
    @ApiIgnore
    @RequestMapping (value = "/")
    public void redirect (HttpServletResponse response) throws IOException {
        response.sendRedirect ("/swagger-ui.html ");
    }

    /**
     * @return
     */
    @GetMapping ("/alldata")
    public ResponseEntity<?> getAllTransactions () {
        String id = "0";
        Bucket bucket = rateLimitConfig.resolveBucket (id);
        if (bucket.tryConsume (1)) {
            return ResponseEntity.ok (storeServices.ReadAllData ());
        } else {
            return ResponseEntity.ok ("Rate limit exceeded for user");
            //return ResponseEntity.status(429).body(new ApiResponse("Rate limit exceeded for user"));
        }

    }

    /**
     * @param postDTO
     * @return
     */

    @PostMapping ("/post")
    @CrossOrigin
    public ResponseEntity<?> addPost (@RequestBody PostDTO postDTO) {
        String id = "1";
        Bucket bucket = rateLimitConfig.resolveBucket (id);
        if (bucket.tryConsume (1)) {
            return ResponseEntity.ok (storeServices.addPost (postDTO));
        } else {
            return ResponseEntity.ok ("Rate limit exceeded for user");
        }

    }

    /**
     * @param userId
     * @return
     */
    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<?> deletePost (@PathVariable ("id") int userId) {
        return storeServices.deletePost (userId);
    }
}

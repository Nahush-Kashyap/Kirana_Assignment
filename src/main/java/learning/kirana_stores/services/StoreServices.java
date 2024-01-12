package learning.kirana_stores.services;

import learning.kirana_stores.entities.Post;
import learning.kirana_stores.mappers.PostMapper;
import learning.kirana_stores.model.posts.PostDTO;
import learning.kirana_stores.repository.StoreRepository;
import learning.kirana_stores.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StoreServices {

    private final StoreRepository repo;
    private final CurrencyConvertionService currencyConvertionService;

    /**
     * Retrieves all data from the store.
     *
     * @return ApiResponse containing the status and data.
     */
    public ApiResponse ReadAllData() {
        List<Post> posts = repo.findAll();
        ApiResponse apiResponse = new ApiResponse();
        if (posts.isEmpty()) {
            apiResponse.setStatus("DATA NOT FOUND");
            apiResponse.setStatusCode("404");
            apiResponse.setError("No data found");
        } else {
            apiResponse.setStatusCode("200");
            apiResponse.setStatus("SUCCESS");
            apiResponse.setData(posts);
        }
        return apiResponse;
    }

    /**
     * Adds a new post to the store.
     *
     * @param postDTO The PostDTO containing the post data.
     * @return ApiResponse containing the status and data.
     */
    public ApiResponse addPost(@RequestBody PostDTO postDTO) {
        ApiResponse apiResponse = new ApiResponse();
        Post post = PostMapper.INSTANCE.dtotoPost(postDTO);
        String currency = post.getCurrency();
        System.out.println("Currency is: " + currency);
        BigDecimal convertedRate = currencyConvertionService.getConversionRate(currency, "INR");
        System.out.println("Converted rate is: " + convertedRate);
        BigDecimal originalAmount = BigDecimal.valueOf(post.getOriginalAmount());
        BigDecimal temp = originalAmount.multiply(convertedRate);
        post.setAmount(temp);
        post.setDateTime(LocalDateTime.now());

        String id = repo.save(post).getId();
        if (StringUtils.hasText(id)) {
            apiResponse.setData(id);
            apiResponse.setStatusCode("200");
            apiResponse.setStatus("SUCCESS");
        } else {
            apiResponse.setErrorMsg("Couldn't create");
            apiResponse.setStatus("429");
        }
        return apiResponse;
    }

    /**
     * Deletes a post from the store based on the provided ID.
     *
     * @param userId The ID of the post to be deleted.
     * @return ResponseEntity with the deletion status.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") int userId) {
        if (repo.existsById(String.valueOf(userId))) {
            repo.deleteById(String.valueOf(userId));
            return new ResponseEntity<>("Post with ID " + userId + " deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Post with ID " + userId + " not found", HttpStatus.NOT_FOUND);
        }
    }
}

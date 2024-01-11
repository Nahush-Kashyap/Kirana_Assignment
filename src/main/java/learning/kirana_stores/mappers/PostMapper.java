package learning.kirana_stores.mappers;

import learning.kirana_stores.entities.Post;
import learning.kirana_stores.model.posts.PostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *
 */
@Mapper (
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring"
)
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper (PostMapper.class);

    @Mapping (source = "dto.originalAmount", target = "originalAmount")
    @Mapping (source = "dto.currency", target = "currency")
    @Mapping (source = "dto.transactionType", target = "transactionType")
    Post dtotoPost (PostDTO dto);
}

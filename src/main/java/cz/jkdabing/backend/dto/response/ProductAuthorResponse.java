package cz.jkdabing.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductAuthorResponse {

    private UUID authorId;

    private String firstName;

    private String lastName;

    private String authorType;
}

package cz.jkdabing.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponActivationDTO {

    @NotNull
    @JsonProperty("isActive")
    private boolean isActive;

    private LocalDate expirationDate;

    private Integer maxUsageCount;
}

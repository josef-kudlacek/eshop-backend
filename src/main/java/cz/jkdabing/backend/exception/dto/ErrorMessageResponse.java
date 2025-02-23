package cz.jkdabing.backend.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessageResponse {

    private int statusCode;

    private Date timestamp;

    private String message;

    private Map<String, List<String>> errors;

    private String description;

    public ErrorMessageResponse(int statusCode, Date timestamp, String message, String description) {
        this.statusCode = statusCode;
        this.description = description;
        this.message = message;
        this.timestamp = timestamp;
    }
}

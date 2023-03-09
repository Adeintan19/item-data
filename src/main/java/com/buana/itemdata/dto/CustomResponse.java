package com.buana.itemdata.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        setterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE
)
@Component
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomResponse {
    @JsonIgnore
    private Integer httpCode= HttpStatus.OK.value();
    private Integer responseCode=null;
    private String responseMessage=null;
    private Object data=null;
}

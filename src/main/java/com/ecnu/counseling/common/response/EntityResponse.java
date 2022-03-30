package com.ecnu.counseling.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class EntityResponse<T> extends AbstractResponse {
    private T data;

    public EntityResponse(ResponseCodeEnum code, String message, T data) {
        super(code, message);
        this.data = data;
    }

//    @JsonIgnore
    public T getData() {
        return this.data;
    }
}

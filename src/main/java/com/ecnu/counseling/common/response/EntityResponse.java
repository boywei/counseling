package com.ecnu.counseling.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
public class EntityResponse<T> extends AbstractResponse {
    private T data;

    public EntityResponse(ResponseCodeEnum code, String message, T data) {
        super(code, message);
        this.data = data;
    }

    public T getData() {
        return this.data;
    }
}
package com.ecnu.counseling.common.response;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ListResponse<T> extends AbstractResponse {

    /**
     * 数据
     */
    private List<T> data;

}

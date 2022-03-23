package com.ecnu.counseling.common.response;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/20 10:46 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ListResponse<T> extends AbstractResponse {

    /**
     * 数据
     */
    private List<T> data;

}

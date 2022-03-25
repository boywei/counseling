package com.ecnu.counseling.supervisor.model.param;

import com.ecnu.counseling.common.model.param.PagingParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/21 1:02 上午
 */

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SupervisorQueryParam extends PagingParam {

    private Integer callerId;
}

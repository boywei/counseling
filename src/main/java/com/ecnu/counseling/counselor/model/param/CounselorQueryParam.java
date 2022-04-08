package com.ecnu.counseling.counselor.model.param;

import com.ecnu.counseling.common.model.param.PagingParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CounselorQueryParam extends PagingParam {

    private Integer counselorId;
}

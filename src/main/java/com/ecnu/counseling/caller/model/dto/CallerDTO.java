package com.ecnu.counseling.caller.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/20 5:38 下午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CallerDTO {

    private Integer id;

    private String name;

    private String phone;

    private String password;

    private String emergencyContactName;

    private String emergencyNumber;
}

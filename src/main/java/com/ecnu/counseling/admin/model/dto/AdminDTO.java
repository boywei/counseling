package com.ecnu.counseling.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/25 10:38 上午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {

    private Integer id;

    private String name;

    private String password;

}

package com.ecnu.counseling.supervisor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/21 12:03 上午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupervisorDTO {

    private Integer id;

    private String name;

    /**
     * 0女1男
     */
    private Integer gender;

    private Integer age;

    private String idCard;

    private String phone;

    private String email;

    private String userName;

    private String workplace;

    private String position;

    private Integer status;

    private String url;

    private Integer countHelp;

    private String qualification;

    private String qualificationId;

}

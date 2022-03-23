package com.ecnu.counseling.counselor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/21 12:03 上午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CounselorDTO {

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

    private Integer countCounsel;

    /**
     * 综合评分, 0-5
     */
    private Double rating;

    /**
     * 是否被当前用户咨询过
     */
    private Boolean hadConsulted;

//      `id` int unsigned NOT NULL AUTO_INCREMENT,
//        `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '真实姓名',
//        `gender` int NOT NULL COMMENT '0女1男',
//        `age` int NOT NULL,
//        `id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号',
//        `phone` int NOT NULL COMMENT '电话号码,11位',
//        `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱,aa@bb',
//        `user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户名',
//        `password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '密码, 20位以内',
//        `workplace` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '工作地点',
//        `position` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '职位',
//        `status` int NOT NULL DEFAULT '0' COMMENT '在线状态0不在线1空闲2忙碌',
//        `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
//  `count_counsel` int NOT NULL DEFAULT '0' COMMENT '咨询次数',
//        `rating` double(3,0) NOT NULL DEFAULT '0' COMMENT '综合评分, 0-5',
//        `is_deleted` int NOT NULL DEFAULT '0' COMMENT '0未删除1已删除',
}

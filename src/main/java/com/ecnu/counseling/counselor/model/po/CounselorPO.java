package com.ecnu.counseling.counselor.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.counselor.model.dto.CounselorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("counselor")
public class CounselorPO extends BasePO {

    private String name;

    private Integer gender;

    private Integer age;

    private String idCard;

    private String phone;

    private String email;

    private String userName;

    private String password;

    private String workplace;

    private String position;

    /**
     * 在线状态0不在线1空闲2忙碌
     */
    private Integer status;

    private String url;

    private Integer countCounsel;

    private Double rating;

    public CounselorDTO convert2DTO() {
        return CounselorDTO.builder()
            .id(this.id)
            .name(this.name)
            .gender(this.gender)
            .age(this.age)
            .idCard(this.idCard)
            .phone(this.phone)
            .email(this.email)
            .userName(this.userName)
            .workplace(this.workplace)
            .position(this.position)
            .status(this.status)
            .url(this.url)
            .countCounsel(this.countCounsel)
            .rating(this.rating)
            .build();
    }
}

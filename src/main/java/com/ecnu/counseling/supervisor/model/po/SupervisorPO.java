package com.ecnu.counseling.supervisor.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.supervisor.model.dto.SupervisorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/20 10:20 下午
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("supervisor")
public class SupervisorPO extends BasePO {

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

    private Integer countHelp;

    private String qualification;

    private String qualificationId;

    public SupervisorDTO convert2DTO() {
        return SupervisorDTO.builder()
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
                .countHelp(this.countHelp)
                .qualification(this.qualification)
                .qualificationId(this.qualificationId)
                .build();
    }
}

package com.ecnu.counseling.common.model.enumeration;

import com.ecnu.counseling.common.constant.UserConstant;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum IdentityEnum {

    ADMINISTRATOR(0, "管理员", UserConstant.ADMINISTRATOR_PREFIX),

    CALLER(1, "访客", UserConstant.CALLER_PREFIX),

    COUNSELOR(2, "咨询师", UserConstant.COUNSELOR_PREFIX),

    SUPERVISOR(3, "督导", UserConstant.SUPERVISOR_PREFIX),

    ;

    private Integer id;
    private String desc;
    private String prefix;


    IdentityEnum(int id, String desc, String prefix) {
        this.id = id;
        this.desc = desc;
        this.prefix = prefix;
    }

    public static IdentityEnum parseById(Integer id) {
        return Stream.of(IdentityEnum.values()).filter(e -> Objects.equals(e.getId(), id)).findAny()
            .orElseThrow(() -> new IllegalArgumentException("身份类型不正确"));
    }
}

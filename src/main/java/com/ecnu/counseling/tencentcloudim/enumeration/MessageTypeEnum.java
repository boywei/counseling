package com.ecnu.counseling.tencentcloudim.enumeration;

import java.util.Objects;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum MessageTypeEnum {

    TIMTextElem(1, "文本消息", "TIMTextElem"),
    TIMLocationElem(2, "位置消息", "TIMLocationElem"),
    TIMFaceElem(3, "表情消息", "TIMFaceElem"),
    TIMCustomElem(4, "自定义消息", "TIMCustomElem"),
    TIMSoundElem(5, "语音消息", "TIMSoundElem"),
    TIMImageElem(6, "图像消息", "TIMImageElem"),
    TIMFileElem(7, "文件消息", "TIMFileElem"),
    TIMVideoFileElem(8, "视频消息", "TIMVideoFileElem"),

    ;


    private Integer id;
    private String desc;
    private String imMessageType;

    MessageTypeEnum(int id, String desc, String imMessageType) {
        this.id = id;
        this.desc = desc;
        this.imMessageType = imMessageType;
    }

    public static MessageTypeEnum parseById(Integer id) {
        return Stream.of(MessageTypeEnum.values()).filter(e -> Objects.equals(e.getId(), id)).findAny()
            .orElseThrow(() -> new IllegalArgumentException("消息类型不正确"));
    }
}

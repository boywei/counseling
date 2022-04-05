package com.ecnu.counseling.help.model.param;

import com.ecnu.counseling.common.model.param.PagingParam;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveMessageParam {

    /**
     * 发送者
     */
    @NotNull(message = "发送者id不可为空")
    private Integer fromUserId;

    /**
     * 接收者
     */
    @NotNull(message = "接收者id不可为空")
    private Integer toUserId;

    /**
     * 发送者身份：0督导 1咨询师
     */
    @NotNull(message = "发送者身份不可为空")
    private Integer owner;

    /**
     * 查询数量
     */
    private Integer count;

    /**
     * 开始时间时间戳，秒
     */
    @NotNull(message = "开始时间时间戳不可为空")
    private Integer startTime;

    /**
     * 结束时间时间戳，秒
     */
    @NotNull(message = "结束时间时间戳不可为空")
    private Integer endTime;

    /**
     * 最后一条消息的msgKey，没有的话传null
     */
    private String lastMsgKey;

}

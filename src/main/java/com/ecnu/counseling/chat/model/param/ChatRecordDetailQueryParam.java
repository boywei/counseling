package com.ecnu.counseling.chat.model.param;

import com.ecnu.counseling.common.model.param.PagingParam;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/21 11:59 上午
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRecordDetailQueryParam extends PagingParam {

    /**
     * 咨询会话记录id
     */
    @NotNull(message = "咨询会话记录id不可为空")
    @Min(value = 1, message = "咨询会话记录id最小为1")
    private Integer chatId;

    /**
     * 查询消息者身份：0访客 1咨询师
     */
    @NotNull(message = "查询者身份类型不可为空")
    private Integer querierType;

    /**
     * 查询者id
     */
    @NotNull(message = "查询者id不可为空")
    @Min(value = 1, message = "查询者id最小为1")
    private Integer querierId;
}

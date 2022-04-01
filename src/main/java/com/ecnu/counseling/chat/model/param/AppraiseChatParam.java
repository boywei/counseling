package com.ecnu.counseling.chat.model.param;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppraiseChatParam {

    /**
     * 谁评价的 0：访客 1：咨询师
     */
    @NotNull(message = "评价者类型不可为空")
    private Integer who;

    /**
     * chatId
     */
    @NotNull(message = "chatId不可为空")
    private Integer chatId;

    /**
     * 访客给咨询师的评分
     */
    private Integer score;

    /**
     * 访客评价
     */
    private String commentCaller;

    /**
     * 咨询师评价
     */
    private String commentCounselor;

    public Boolean isEmpty() {
        return score == null
            && StringUtils.isEmpty(commentCaller)
            && StringUtils.isEmpty(commentCounselor);

    }
}

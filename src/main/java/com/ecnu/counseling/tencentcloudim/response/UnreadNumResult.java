package com.ecnu.counseling.tencentcloudim.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/4/5 1:29 上午
 */
@Data
public class UnreadNumResult {

    @JsonProperty("ActionStatus")
    private String actionStatus;

    @JsonProperty("ErrorInfo")
    private String errorInfo;

    @JsonProperty("ErrorCode")
    private Integer errorCode;

    @JsonProperty("AllC2CUnreadMsgNum")
    private Integer allC2CUnreadMsgNum;

    @JsonProperty("C2CUnreadMsgNumList")
    private List<UnreadMsgNumDetail> c2CUnreadMsgNumList;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UnreadMsgNumDetail {

        @JsonProperty("Peer_Account")
        private String peerAccount;

        @JsonProperty("C2CUnreadMsgNum")
        private Integer c2CUnreadMsgNum;
    }


//    {
//        "ActionStatus": "OK",
//        "ErrorInfo": "",
//        "ErrorCode": 0,
//        "C2CUnreadMsgNumList": [
//        {
//            "Peer_Account": "dramon2",
//            "C2CUnreadMsgNum": 12
//        },
//        {
//            "Peer_Account": "teacher",
//            "C2CUnreadMsgNum": 12
//        }
//    ]
//    }
}

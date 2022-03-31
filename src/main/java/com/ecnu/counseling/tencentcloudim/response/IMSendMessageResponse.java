package com.ecnu.counseling.tencentcloudim.response;

import com.ecnu.counseling.tencentcloudim.constant.ImConstant;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IMSendMessageResponse {

    @JsonProperty("ActionStatus")
    private String actionStatus;

    @JsonProperty("ErrorInfo")
    private String errorInfo;

    @JsonProperty("ErrorCode")
    private Integer errorCode;

    @JsonProperty("MsgTime")
    private Integer msgTime;

    @JsonProperty("MsgKey")
    private String msgKey;

    public Boolean isRight() {
        return ImConstant.ACTION_STATUS_OK.equals(this.actionStatus);
    }



//    {
//        "ActionStatus": "OK",
//        "ErrorInfo": "",
//        "ErrorCode": 0,
//        "MsgTime": 1572870301,
//        "MsgKey": "89541_2574206_1572870301"
//    }
//    {
//        "ActionStatus": "FAIL",
//        "ErrorInfo": "Fail to Parse json data of body, Please check it",
//        "ErrorCode": 90001
//    }
}

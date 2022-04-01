package com.ecnu.counseling.tencentcloudim.response;

import com.ecnu.counseling.tencentcloudim.constant.ImConstant;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class IMReceiveMessageResponse {

    @JsonProperty("ActionStatus")
    private String actionStatus;

    @JsonProperty("ErrorInfo")
    private String errorInfo;

    @JsonProperty("ErrorCode")
    private Integer errorCode;

    @JsonProperty("Complete")
    private Integer complete;

    @JsonProperty("MsgCnt")
    private Integer msgCnt;

    @JsonProperty("LastMsgTime")
    private Integer lastMsgTime;

    @JsonProperty("LastMsgKey")
    private String lastMsgKey;

    @JsonProperty("MsgList")
    private List<Message> msgList;

    @Data
    public static class Message {

        @JsonProperty("From_Account")
        private String fromAccount;

        @JsonProperty("To_Account")
        private String toAccount;

        @JsonProperty("MsgSeq")
        private Integer msgSeq;

        @JsonProperty("MsgRandom")
        private Integer msgRandom;

        @JsonProperty("MsgTimeStamp")
        private Integer msgTimeStamp;

        @JsonProperty("MsgClientTime")
        private Integer msgClientTime;

        @JsonProperty("MsgFlagBits")
        private Integer msgFlagBits;

        @JsonProperty("MsgKey")
        private String msgKey;

        @JsonProperty("MsgBody")
        private List<MessageBody> msgBody;
    }

    @Data
    public static class MessageBody {

        @JsonProperty("MsgType")
        private String msgType;

        @JsonProperty("MsgContent")
        private Object msgContent;
    }



    public Boolean isRight() {
        return ImConstant.ACTION_STATUS_OK.equals(this.actionStatus);
    }

//    {
//        "ActionStatus": "OK",
//        "ErrorInfo": "",
//        "ErrorCode": 0,
//        "Complete": 1,
//        "MsgCnt": 2,
//        "LastMsgTime": 1648729970,
//        "LastMsgKey": "1222389972_12438987_1648729970",
//        "MsgList": [
//        {
//            "From_Account": "11",
//            "To_Account": "12",
//            "MsgSeq": 1222389972,
//            "MsgRandom": 12438987,
//            "MsgTimeStamp": 1648729970,
//            "MsgClientTime": 1648729970,
//            "MsgFlagBits": 0,
//            "MsgKey": "1222389972_12438987_1648729970",
//            "MsgBody": [
//            {
//                "MsgType": "TIMTextElem",
//                "MsgContent": {
//                "Text": "pariatur dolore esse111111111"
//            }
//            }
//            ]
//        },
//        {
//            "From_Account": "11",
//            "To_Account": "12",
//            "MsgSeq": 1272059734,
//            "MsgRandom": 341410443,
//            "MsgTimeStamp": 1648729991,
//            "MsgClientTime": 1648729991,
//            "MsgFlagBits": 0,
//            "MsgKey": "1272059734_341410443_1648729991",
//            "MsgBody": [
//            {
//                "MsgType": "TIMTextElem",
//                "MsgContent": {
//                "Text": "pariatur dolore esse22222222"
//            }
//            }
//            ]
//        }
//    ]
//    }
}

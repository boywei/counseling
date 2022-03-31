package com.ecnu.counseling.common.util;

import com.ecnu.counseling.tencentcloudim.response.IMReceiveMessageResponse;
import com.ecnu.counseling.tencentcloudim.response.IMSendMessageResponse;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class JsonUtilsTest {

    @Test
    void decodeJsonToMap() {
    }

    @Test
    void readValue() {
        String str = "{\"ActionStatus\":\"OK\",\"ErrorInfo\":\"\",\"ErrorCode\":0,\"MsgTime\":1648723691,\"MsgKey\":\"1093284151_231151917_1648723691\"}";
        Optional<IMSendMessageResponse> imSendMessageResponse = JsonUtils.readValue(str, IMSendMessageResponse.class);
        System.out.println(imSendMessageResponse);
    }

    @Test
    void readValueV2() {
        String str = "{\"ActionStatus\":\"OK\",\"ErrorInfo\":\"\",\"ErrorCode\":0,\"Complete\":1,\"MsgCnt\":2,\"LastMsgTime\":1648729970,\"LastMsgKey\":\"1222389972_12438987_1648729970\",\"MsgList\":[{\"From_Account\":\"11\",\"To_Account\":\"12\",\"MsgSeq\":1222389972,\"MsgRandom\":12438987,\"MsgTimeStamp\":1648729970,\"MsgClientTime\":1648729970,\"MsgFlagBits\":0,\"MsgKey\":\"1222389972_12438987_1648729970\",\"MsgBody\":[{\"MsgType\":\"TIMTextElem\",\"MsgContent\":{\"Text\":\"pariatur dolore esse111111111\"}}]},{\"From_Account\":\"11\",\"To_Account\":\"12\",\"MsgSeq\":1272059734,\"MsgRandom\":341410443,\"MsgTimeStamp\":1648729991,\"MsgClientTime\":1648729991,\"MsgFlagBits\":0,\"MsgKey\":\"1272059734_341410443_1648729991\",\"MsgBody\":[{\"MsgType\":\"TIMTextElem\",\"MsgContent\":{\"Text\":\"pariatur dolore esse22222222\"}}]}]}";
        Optional<IMReceiveMessageResponse> optional = JsonUtils.readValue(str, IMReceiveMessageResponse.class);
        System.out.println(optional);
    }
}
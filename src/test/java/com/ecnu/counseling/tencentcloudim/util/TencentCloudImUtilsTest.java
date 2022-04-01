package com.ecnu.counseling.tencentcloudim.util;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class TencentCloudImUtilsTest {

    @Autowired
    private TencentCloudImUtils tencentCloudImUtils;

    @Test
    void getTxCloudUserSig() {
        String txCloudUserSig = tencentCloudImUtils.getTxCloudUserSig();
        System.out.println(txCloudUserSig);
        System.out.println(111);
        tencentCloudImUtils.accountImport("111");
        String s = tencentCloudImUtils.queryAccount(Collections.singletonList("111"));
        System.out.println("检查是否导入im：" + s);
    }

    @Test
    void messageTest() {
        ArrayList<String> userIds = Lists.newArrayList("1", "2");
        tencentCloudImUtils.multiAccountImport(userIds);
        String accountCheck = tencentCloudImUtils.queryAccount(userIds);
        log.info("检查导入账号结果：{}", accountCheck);

        String sendMsgResult = tencentCloudImUtils.sendMsg(1, "111", "111", "TIMTextElem", "hello world");
        log.info("发送消息{} -> {}结果：{}", "1", "2", sendMsgResult);

        String queryMessageResult = tencentCloudImUtils.adminGetRoamMsg("111", "111", 10, 1648700210, 1648701210, "1382550325_484825850_1648701070");
        log.info("查询消息结果：{}", queryMessageResult);
    }
}
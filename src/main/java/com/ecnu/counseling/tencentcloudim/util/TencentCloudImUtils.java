package com.ecnu.counseling.tencentcloudim.util;

import com.alibaba.fastjson.JSONObject;
import com.ecnu.counseling.tencentcloudim.constant.ImApiConstant;
import com.ecnu.counseling.tencentcloudim.constant.ImConstant;
import com.tencentyun.TLSSigAPIv2;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class TencentCloudImUtils {

    private static final String HTTPS_URL_PREFIX = "https://console.tim.qq.com/";
    private static final String APP_MANAGER = "administrator";
    private static final String REDIS_IM_USER_SIG = "silence:im_user_sig:";

    @Value("${silence.tencent.cloud.im.sdkAppId}")
    private long sdkAppId;
    @Value("${silence.tencent.cloud.im.key}")
    private String key;

    @Autowired
    private RedisServiceUtils redisServiceUtils;

    /**
     * 获取腾讯云用户签名
     */
    public String getTxCloudUserSig() {
        String userSig = redisServiceUtils.get(REDIS_IM_USER_SIG + APP_MANAGER);
        if (StringUtils.isEmpty(userSig)) {
            TLSSigAPIv2 tlsSigApi = new TLSSigAPIv2(sdkAppId, key);
            userSig = tlsSigApi.genUserSig(APP_MANAGER, 86400);
            redisServiceUtils.set(REDIS_IM_USER_SIG + APP_MANAGER, userSig, 24L, TimeUnit.HOURS);
        }
        return userSig;
    }

    /**
     * 获取腾讯im请求路径
     */
    private String getHttpsUrl(String imServiceApi, Integer random) {
        return String.format("%s%s?sdkappid=%s&identifier=%s&usersig=%s&random=%s&contenttype=json",
            HTTPS_URL_PREFIX, imServiceApi, sdkAppId, APP_MANAGER, this.getTxCloudUserSig(), random);
    }

    /**
     * 导入单个账号
     *
     * @param userId 用户id
     */
    public void accountImport(String userId) {
        accountImport(userId, null);
    }

    public void accountImport(String userId, String userName) {
        accountImport(userId, userName, null);
    }

    public void accountImport(String userId, String userName, String faceUrl) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(ImApiConstant.AccountManage.ACCOUNT_IMPORT, random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Identifier", userId);
        if (StringUtils.isNotEmpty(userName)) {
            jsonObject.put("Nick", userName);
        }
        if (StringUtils.isNotEmpty(faceUrl)) {
            jsonObject.put("FaceUrl", faceUrl);
        }
        log.info("腾讯云im导入单个账号，请求参数：{}", jsonObject);
        String result = HttpUtils.doPost2(httpsUrl, jsonObject);
        log.info("腾讯云im导入单个账号，返回结果：{}", result);
    }

    /**
     * 导入多个账号
     *
     * @param userIds 用户id集合
     */
    public void multiAccountImport(List<String> userIds) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(ImApiConstant.AccountManage.MULTI_ACCOUNT_IMPORT, random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Accounts", userIds);
        log.info("腾讯云im导入多个账号，请求参数：{}", jsonObject);
        String result = HttpUtils.doPost2(httpsUrl, jsonObject);
        log.info("腾讯云im导入单个账户，返回结果：{}", result);
    }

    /**
     * 删除账号
     *
     * @param userIds 用户id集合
     */
    public void accountDelete(List<String> userIds) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(ImApiConstant.AccountManage.ACCOUNT_DELETE, random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("DeleteItem", getUserIdJsonList(userIds));
        log.info("腾讯云im删除账号，请求参数：{}", jsonObject);
        String result = HttpUtils.doPost2(httpsUrl, jsonObject);
        log.info("腾讯云im删除账户，返回结果：{}", result);
    }

    /**
     * 查询账号是否已经导入im
     *
     * @param userIds 用户id集合
     */
    public String queryAccount(List<String> userIds) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(ImApiConstant.AccountManage.ACCOUNT_CHECK, random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("CheckItem", getUserIdJsonList(userIds));
        log.info("腾讯云im查询账号，请求参数：{}", jsonObject);
        String result = HttpUtils.doPost2(httpsUrl, jsonObject);
        log.info("腾讯云im查询账号，返回结果：{}", result);
        return result;
    }

    private List<JSONObject> getUserIdJsonList(List<String> userIds) {
        return userIds.stream().map(v -> {
            JSONObject userIdJson = new JSONObject();
            userIdJson.put("UserID", v);
            return userIdJson;
        }).collect(Collectors.toList());
    }

    /**
     * 单发单聊消息
     *
     * @param syncOtherMachine 是否同步消息到发送方（1-同步，2-不同步）
     * @param fromUserId       发送方用户id
     * @param toUserId         接收方用户id
     * @param msgType          消息对象类型
     * @param msgContent       消息内容
     */
    public String sendMsg(Integer syncOtherMachine, String fromUserId, String toUserId, String msgType, String msgContent) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(ImApiConstant.SingleChatManage.SEND_MSG, random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("SyncOtherMachine", syncOtherMachine);
        if (StringUtils.isNotEmpty(fromUserId)) {
            // 发送方不为空表示指定发送用户，为空表示为管理员发送消息
            jsonObject.put("From_Account", fromUserId);
        }
        jsonObject.put("To_Account", toUserId);
        jsonObject.put("MsgRandom", random);
        List<JSONObject> msgBody = getMsgBody(msgType, msgContent);
        jsonObject.put("MsgBody", msgBody);
        log.info("腾讯云im单发单聊消息，请求参数：{}", jsonObject.toString());
        String result = HttpUtils.doPost2(httpsUrl, jsonObject);
        log.info("腾讯云im单发单聊消息，返回结果：{}", result);
        return result;
    }

    public static void main(String[] args) {
        TencentCloudImUtils util = new TencentCloudImUtils();
        String s = util.sendMsg(1, "111", "222", "type", "消息");
        System.out.println(s);
    }

    /**
     * 批量发单聊消息
     *
     * @param syncOtherMachine 是否同步消息到发送方（1-同步，2-不同步）
     * @param fromUserId       发送方用户id
     * @param toUserIds        接收方用户id集合
     * @param msgType          消息对象类型
     * @param msgContent       消息内容
     */
    public String batchSendMsg(Integer syncOtherMachine, String fromUserId, List<String> toUserIds, String msgType, String msgContent) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(ImApiConstant.SingleChatManage.BATCH_SEND_MSG, random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("SyncOtherMachine", syncOtherMachine);
        if (StringUtils.isNotEmpty(fromUserId)) {
            // 发送方不为空表示指定发送用户，为空表示为管理员发送消息
            jsonObject.put("From_Account", fromUserId);
        }
        jsonObject.put("To_Account", toUserIds);
        jsonObject.put("MsgRandom", random);
        List<JSONObject> msgBody = getMsgBody(msgType, msgContent);
        jsonObject.put("MsgBody", msgBody);
        log.info("腾讯云im批量发单聊消息，请求参数：{}", jsonObject.toString());
        String result = HttpUtils.doPost2(httpsUrl, jsonObject);
        log.info("腾讯云im批量发单聊消息，返回结果：{}", result);
        return result;
    }


    /**
     * 拼接发送消息内容
     *
     * @param msgType    消息类型
     * @param msgContent 发送消息内容
     * @return 消息内容
     */
    private List<JSONObject> getMsgBody(String msgType, String msgContent) {
        List<JSONObject> msgBody = new ArrayList<>();
        if (msgType.equals(ImConstant.TIM_TEXT_ELEM)) {
            // 文本类型
            JSONObject msgBodyJson = new JSONObject();
            msgBodyJson.put("MsgType", msgType);
            JSONObject msgContentObj = new JSONObject();
            msgContentObj.put("Text", msgContent);
            msgBodyJson.put("MsgContent", msgContentObj);
            msgBody.add(msgBodyJson);
        }
        return msgBody;
    }

    /**
     * 查询单聊消息
     *
     * @param fromUserId 发送方用户id
     * @param toUserId   接收方用户id
     * @param maxCnt     查询条数
     * @param startTime  起始时间（单位：秒）
     * @param endTime    结束时间（单位：秒）
     * @param lastMsgKey 最后一条消息的 MsgKey
     * @return 单聊消息列表
     */
    public String adminGetRoamMsg(String fromUserId, String toUserId, Integer maxCnt, Integer startTime, Integer endTime, String lastMsgKey) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(ImApiConstant.SingleChatManage.ADMIN_GET_ROAM_MSG, random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("From_Account", fromUserId);
        jsonObject.put("To_Account", toUserId);
        jsonObject.put("MaxCnt", maxCnt);
        jsonObject.put("MinTime", startTime);
        jsonObject.put("MaxTime", endTime);
        if (StringUtils.isNotEmpty(lastMsgKey)) {
            jsonObject.put("LastMsgKey", lastMsgKey);
        }
        log.info("腾讯云im查询单聊消息，请求参数：{}", jsonObject);
        String result = HttpUtils.doPost2(httpsUrl, jsonObject);
        log.info("腾讯云im查询单聊消息，返回结果：{}", result);
        return result;
    }

    /**
     * 撤回单聊消息
     *
     * @param fromUserId 发送方用户id
     * @param toUserId   接收方用户id
     * @param msgKey     MsgKey
     */
    public void adminMsgWithDraw(String fromUserId, String toUserId, String msgKey) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(ImApiConstant.SingleChatManage.ADMIN_MSG_WITH_DRAW, random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("From_Account", fromUserId);
        jsonObject.put("To_Account", toUserId);
        jsonObject.put("MsgKey", msgKey);
        log.info("腾讯云im撤回单聊消息，请求参数：{}", jsonObject.toString());
        String result = HttpUtils.doPost2(httpsUrl, jsonObject);
        log.info("腾讯云im撤回单聊消息，返回结果：{}", result);
    }

    /**
     * 设置单聊消息已读
     *
     * @param reportUserId 读取消息的用户
     * @param peerUserId   发送消息的用户
     */
    public void adminSetMsgRead(String reportUserId, String peerUserId) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(ImApiConstant.SingleChatManage.ADMIN_SET_MSG_READ, random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Report_Account", reportUserId);
        jsonObject.put("Peer_Account", peerUserId);
        log.info("腾讯云im设置单聊消息已读，请求参数：{}", jsonObject.toString());
        String result = HttpUtils.doPost2(httpsUrl, jsonObject);
        log.info("腾讯云im设置单聊消息已读，返回结果：{}", result);
    }

    /**
     * 查询本账号下所有未读消息数量或详情
     *
     * @param toAccountId
     * @return
     */
    public String getAllUnreadNum(String toAccountId, Collection<String> peerAccountIds) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String url = getHttpsUrl(ImApiConstant.SingleChatManage.GET_C2C_UNREAD_MSG_NUM, random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("To_Account", toAccountId);
        if (CollectionUtils.isNotEmpty(peerAccountIds)) {
            jsonObject.put("Peer_Account", peerAccountIds);
        }
        log.info("腾讯云im获取所有未读消息数量，请求参数：{}", jsonObject);
        String result = HttpUtils.doPost2(url, jsonObject);
        log.info("腾讯云im获取所有未读消息数量，响应参数：{}", result);
        return result;
    }
}

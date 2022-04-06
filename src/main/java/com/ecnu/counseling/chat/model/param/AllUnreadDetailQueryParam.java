package com.ecnu.counseling.chat.model.param;

import com.ecnu.counseling.caller.model.param.AccountParam;
import java.util.List;
import lombok.Data;

@Data
public class AllUnreadDetailQueryParam {

    /**
     * 当前账号
     */
    private AccountParam userAccount;

    /**
     * 目标账号们, 不传只查询总未读数
     */
    private List<AccountParam> peerAccounts;

//    {
//        "To_Account":"dramon1",
//        "Peer_Account":[
//        "dramon2",
//            "teacher"
//    ]
//    }
}

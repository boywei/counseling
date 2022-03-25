package com.ecnu.counseling.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.counseling.chat.model.po.MessagePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<MessagePO> {

}

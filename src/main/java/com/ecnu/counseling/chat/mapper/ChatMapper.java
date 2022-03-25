package com.ecnu.counseling.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.counseling.chat.model.po.ChatPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper extends BaseMapper<ChatPO> {

}

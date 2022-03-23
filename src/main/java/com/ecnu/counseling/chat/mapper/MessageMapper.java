package com.ecnu.counseling.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.counseling.chat.model.po.MessagePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/21 2:22 下午
 */
@Mapper
public interface MessageMapper extends BaseMapper<MessagePO> {

}

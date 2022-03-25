package com.ecnu.counseling.help.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecnu.counseling.help.mapper.RecordMapper;
import com.ecnu.counseling.help.model.dto.RecordDTO;
import com.ecnu.counseling.help.model.param.HelpMessageDetailQueryParam;
import com.ecnu.counseling.help.model.po.RecordPO;
import com.ecnu.counseling.help.service.RecordService;
import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/21 2:23 下午
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, RecordPO> implements RecordService {


    @Override
    public ListPagingResponse<RecordDTO> queryPaging(HelpMessageDetailQueryParam queryParam) {
        List<Integer> deleteType = Lists.newArrayList(0, queryParam.getQuerierType() == 0 ? 1 : 2);
        LambdaQueryChainWrapper<RecordPO> queryChainWrapper = new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(RecordPO::getHelpId, queryParam.getHelpId())
                .in(RecordPO::getRecordDeleted, deleteType);
        Integer count = queryChainWrapper.count();
        if (count == 0) {
            return ListPagingResponse.EMPTY_SUCCESS;
        }
        Page<RecordPO> page = new Page<>(queryParam.getStart() / queryParam.getLength() + 1, queryParam.getLength());
        List<RecordDTO> dtos = queryChainWrapper.page(page).getRecords().stream().map(RecordPO::convert2DTO).collect(Collectors.toList());
        return new ListPagingResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, dtos, queryParam.getStart(), queryParam.getLength(), count);
    }

    @Override
    public List<RecordDTO> queryByHelpIds(Collection<Integer> helpIds) {
        if (CollectionUtils.isEmpty(helpIds)) {
            return Collections.emptyList();
        }
        List<RecordPO> pos = new LambdaQueryChainWrapper<>(this.baseMapper)
                .in(RecordPO::getHelpId, helpIds)
                .list();
        return pos.stream().map(RecordPO::convert2DTO).collect(Collectors.toList());
    }
}

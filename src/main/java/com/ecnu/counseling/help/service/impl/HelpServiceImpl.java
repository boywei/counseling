package com.ecnu.counseling.help.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecnu.counseling.help.mapper.HelpMapper;
import com.ecnu.counseling.help.model.dto.HelpDTO;
import com.ecnu.counseling.help.model.param.HelpRecordListQueryParam;
import com.ecnu.counseling.help.model.po.HelpPO;
import com.ecnu.counseling.help.service.HelpService;
import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/21 12:53 上午
 */
@Service
public class HelpServiceImpl extends ServiceImpl<HelpMapper, HelpPO> implements HelpService {

    @Override
    public ListPagingResponse<HelpDTO> queryRecordList(HelpRecordListQueryParam queryParam) {
        LambdaQueryChainWrapper<HelpPO> queryChainWrapper = new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(HelpPO::getSupervisorId, queryParam.getSupervisorId())
                .eq(BasePO::getIsDeleted, 0);
        Integer count = queryChainWrapper.count();
        if (count == 0) {
            return ListPagingResponse.EMPTY_SUCCESS;
        }
        Page<HelpPO> page = new Page<>(queryParam.getStart() / queryParam.getLength() + 1, queryParam.getLength());
        List<HelpDTO> dtos = queryChainWrapper.page(page).getRecords().stream().map(HelpPO::convert2DTO).collect(Collectors.toList());
        return new ListPagingResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, dtos, queryParam.getStart(), queryParam.getLength(),
                count);
    }

    @Override
    public HelpDTO queryById(Integer helpId) {
        HelpPO po = this.getById(helpId);
        return po == null ? null : po.convert2DTO();
    }
}

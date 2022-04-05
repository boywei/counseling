package com.ecnu.counseling.help.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecnu.counseling.help.mapper.HelpMapper;
import com.ecnu.counseling.help.model.dto.HelpDTO;
import com.ecnu.counseling.help.model.param.HelpRecordListQueryParam;
import com.ecnu.counseling.help.model.param.CreateHelpParam;
import com.ecnu.counseling.help.model.param.FinishHelpParam;
import com.ecnu.counseling.help.model.po.HelpPO;
import com.ecnu.counseling.help.service.HelpService;
import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.common.util.CheckUtils;
import com.ecnu.counseling.common.util.LocalDateTimeUtils;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class HelpServiceImpl extends ServiceImpl<HelpMapper, HelpPO> implements HelpService {

    @Override
    public ResultInfo<Integer> create(CreateHelpParam createHelpParam) {
        if (CheckUtils.anyEmptyIds(createHelpParam.getSupervisorId(), createHelpParam.getCounselorId())) {
            return ResultInfo.error("请求参数中存在id不合法");
        }
        Integer count = new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(HelpPO::getSupervisorId, createHelpParam.getSupervisorId())
                .eq(HelpPO::getCounselorId, createHelpParam.getCounselorId())
                .isNull(HelpPO::getEndTime)
                .count();
        if (count != 0) {
            return ResultInfo.error("已存在未结束的会话, 请勿重复开始会话");
        }
        HelpPO po = HelpPO.builder().supervisorId(createHelpParam.getSupervisorId())
                .counselorId(createHelpParam.getCounselorId())
                .startTime(LocalDateTimeUtils.getDateTimeOfTimestamp(createHelpParam.getStartTime()))
                .build();
        this.save(po);
        return ResultInfo.success(po.getId());
    }

    @Override
    public BaseResult finish(FinishHelpParam finishHelpParam) {
        if (CheckUtils.isEmptyId(finishHelpParam.getHelpId())) {
            return BaseResult.error("helpId不合法");
        }
        boolean update = new LambdaUpdateChainWrapper<>(this.baseMapper)
                .eq(BasePO::getId, finishHelpParam.getHelpId())
                .isNull(HelpPO::getEndTime)
                .set(HelpPO::getEndTime, LocalDateTimeUtils.getDateTimeOfTimestamp(finishHelpParam.getEndTime()))
                .update();
        return update ? BaseResult.SUCCESS : BaseResult.error("不存在该求助会话记录或者该会话已结束");
    }

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

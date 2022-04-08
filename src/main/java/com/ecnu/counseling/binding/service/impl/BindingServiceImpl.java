package com.ecnu.counseling.binding.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecnu.counseling.binding.mapper.BindingMapper;
import com.ecnu.counseling.binding.model.param.BindingAddParam;
import com.ecnu.counseling.binding.model.param.BindingEditParam;
import com.ecnu.counseling.binding.model.param.BindingQueryParam;
import com.ecnu.counseling.binding.model.po.BindingPO;
import com.ecnu.counseling.binding.service.BindingService;
import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BindingServiceImpl extends ServiceImpl<BindingMapper, BindingPO> implements BindingService {

    @Override
    public ResultInfo<Integer> add(BindingAddParam addParam) {
        int count = new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(BindingPO::getCounselorId, addParam.getCounselorId())
                .eq(BindingPO::getSupervisorId, addParam.getSupervisorId())
                .count();
        if (count != 0) {
            return ResultInfo.error("绑定关系已存在！");
        }

        BindingPO po = BindingPO.builder()
                .counselorId(addParam.getCounselorId())
                .supervisorId(addParam.getSupervisorId())
                .build();
        this.save(po);
        return ResultInfo.success(po.getId());
    }

    @Override
    public BaseResult remove(Integer bindingId) {
        boolean remove = new LambdaUpdateChainWrapper<>(this.baseMapper)
                .eq(BasePO::getId, bindingId)
                .remove();
        return remove ? BaseResult.SUCCESS : BaseResult.error("不存在该条记录");
    }

    @Override
    public BaseResult edit(BindingEditParam editParam) {
        BindingPO po = BindingPO.builder()
                .counselorId(editParam.getCounselorId())
                .supervisorId(editParam.getSupervisorId())
                .build();
        boolean update = new LambdaUpdateChainWrapper<>(this.baseMapper)
                .eq(BasePO::getId, editParam.getBindingId())
                .update(po);
        return update ? BaseResult.SUCCESS : BaseResult.error("不存在该条记录");
    }

    @Override
    public ListPagingResponse<BindingPO> detailById(BindingQueryParam queryParam, Integer start, Integer length) {
        LambdaQueryChainWrapper<BindingPO> queryWrapper = new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(BindingPO::getId, queryParam.getBindingId())
                .or()
                .eq(BindingPO::getCounselorId, queryParam.getCounselorId())
                .or()
                .eq(BindingPO::getSupervisorId, queryParam.getSupervisorId())
                .eq(BasePO::getIsDeleted, 0);
        Integer count = queryWrapper.count();
        if (count == 0) {
            return ListPagingResponse.EMPTY_SUCCESS;
        }
        Page<BindingPO> page = new Page<>(start / length + 1, length);
        List<BindingPO> records = queryWrapper.page(page).getRecords();
        return new ListPagingResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, records, start, length, count);
    }

    @Override
    public ListPagingResponse<BindingPO> list(Integer start, Integer length) {
        LambdaQueryChainWrapper<BindingPO> queryWrapper = new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(BasePO::getIsDeleted, 0);
        Integer count = queryWrapper.count();
        if (count == 0) {
            return ListPagingResponse.EMPTY_SUCCESS;
        }
        Page<BindingPO> page = new Page<>(start / length + 1, length);
        List<BindingPO> records = queryWrapper.page(page).getRecords();
        return new ListPagingResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, records, start, length, count);
    }

}

package com.ecnu.counseling.counselor.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.common.util.CheckUtils;
import com.ecnu.counseling.counselor.mapper.CounselorMapper;
import com.ecnu.counseling.counselor.model.po.CounselorPO;
import com.ecnu.counseling.counselor.service.CounselorService;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CounselorServiceImpl extends ServiceImpl<CounselorMapper, CounselorPO> implements CounselorService {

    @Override
    public ResultInfo<CounselorPO> detailById(Integer id) {
        if (CheckUtils.isEmptyId(id)) {
            return ResultInfo.error("id不合法");
        }
        CounselorPO po = new LambdaQueryChainWrapper<>(this.baseMapper)
            .eq(BasePO::getId, id)
            .last("limit 1")
            .one();
        return po == null ? ResultInfo.error("不存在该咨询师记录") : ResultInfo.success(po);
    }

    @Override
    public ListPagingResponse<CounselorPO> list(Collection<Integer> counselorIds, Integer start, Integer length) {
        LambdaQueryChainWrapper<CounselorPO> queryWrapper = new LambdaQueryChainWrapper<>(this.baseMapper)
            .in(CounselorPO::getStatus, Lists.newArrayList(1, 2))
            .eq(BasePO::getIsDeleted, 0);
        Integer count = queryWrapper.count();
        if (count == 0) {
            return ListPagingResponse.EMPTY_SUCCESS;
        }
        Page<CounselorPO> page = new Page<>(start / length + 1, length);
        List<CounselorPO> records = queryWrapper.page(page).getRecords();
        return new ListPagingResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, records, start, length, count);
    }
}

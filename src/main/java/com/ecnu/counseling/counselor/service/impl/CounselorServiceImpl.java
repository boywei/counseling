package com.ecnu.counseling.counselor.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.counselor.mapper.CounselorMapper;
import com.ecnu.counseling.counselor.model.po.CounselorPO;
import com.ecnu.counseling.counselor.service.CounselorService;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author wuhongbin
 * @Date 2022/3/20 10:18 下午
 */
@Service
public class CounselorServiceImpl extends ServiceImpl<CounselorMapper, CounselorPO> implements CounselorService {

    @Override
    public ListPagingResponse<CounselorPO> list(Collection<Integer> counselorIds, Integer start, Integer length) {
        LambdaQueryChainWrapper<CounselorPO> queryWrapper = new LambdaQueryChainWrapper<>(this.baseMapper)
            .in(CounselorPO::getStatus, Lists.newArrayList(1, 2))
            .eq(BasePO::getIsDelete, 0);
        Integer count = queryWrapper.count();
        if (count == 0) {
            return ListPagingResponse.EMPTY_SUCCESS;
        }
        Page<CounselorPO> page = new Page<>(start / length + 1, length);
        List<CounselorPO> records = queryWrapper.page(page).getRecords();
        return new ListPagingResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, records, start, length, count);
    }
}

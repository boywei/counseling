package com.ecnu.counseling.counselor.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecnu.counseling.common.constant.BaseConstant;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.response.ListPagingResponse;
import com.ecnu.counseling.common.response.ResponseCodeEnum;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.common.util.Md5Util;
import com.ecnu.counseling.counselor.mapper.CounselorMapper;
import com.ecnu.counseling.counselor.model.po.CounselorPO;
import com.ecnu.counseling.counselor.service.CounselorService;
import com.ecnu.counseling.counselor.model.dto.CounselorDTO;
import com.ecnu.counseling.counselor.model.param.CounselorEditParam;
import com.ecnu.counseling.counselor.model.param.CounselorLoginParam;
import com.ecnu.counseling.counselor.model.param.CounselorRegisterParam;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
public class CounselorServiceImpl extends ServiceImpl<CounselorMapper, CounselorPO> implements CounselorService {

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

    @Override
    public ResultInfo<Integer> register(CounselorRegisterParam registerParam) {
        int count = new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(CounselorPO::getPhone, registerParam.getPhone())
                .count();
        if (count != 0) {
            return ResultInfo.error("手机号已注册，请点击登录");
        }

        CounselorPO po = CounselorPO.builder()
                .name(registerParam.getName())
                .password(Md5Util.encryptPassword(registerParam.getPhone(), registerParam.getPassword()))
                .phone(registerParam.getPhone())
                .age(registerParam.getAge())
                .gender(registerParam.getGender())
                .idCard(registerParam.getIdCard())
                .email(registerParam.getEmail())
                .userName(registerParam.getUserName())
                .workplace(registerParam.getWorkplace())
                .position(registerParam.getPosition())
//                .status(registerParam.getStatus())
                .url(registerParam.getUrl())
                .build();
        this.save(po);

        return ResultInfo.success(po.getId());
    }

    @Override
    public BaseResult edit(CounselorEditParam editParam) {
        CounselorPO po = CounselorPO.builder()
                .name(editParam.getName())
                .password(Md5Util.encryptPassword(editParam.getPhone(), editParam.getPassword()))
                .phone(editParam.getPhone())
                .age(editParam.getAge())
                .gender(editParam.getGender())
                .idCard(editParam.getIdCard())
                .email(editParam.getEmail())
                .userName(editParam.getUserName())
                .workplace(editParam.getWorkplace())
                .position(editParam.getPosition())
                .status(editParam.getStatus())
                .url(editParam.getUrl())
                .build();
        boolean update = new LambdaUpdateChainWrapper<>(this.baseMapper)
                .eq(BasePO::getId, editParam.getId())
                .update(po);
        return update ? BaseResult.SUCCESS : BaseResult.error("不存在该条记录");
    }

    @Override
    public ResultInfo<CounselorDTO> login(CounselorLoginParam loginParam) {
        List<CounselorPO> pos = new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(CounselorPO::getPhone, loginParam.getPhone())
                .eq(CounselorPO::getPassword, Md5Util.encryptPassword(loginParam.getPhone(), loginParam.getPassword()))
                .list();
        System.out.println();
        return CollectionUtils.isEmpty(pos)
                ? ResultInfo.error("手机号不存在或密码错误")
                : ResultInfo.success(pos.get(0).convert2DTO());
    }

    @Override
    public ResultInfo<CounselorDTO> detailById(Integer id) {
        if (id == null || id <= 0) {
            return ResultInfo.error("id不存在或不合法");
        }
        CounselorPO po = this.getById(id);
        return po == null ? ResultInfo.error("数据不存在") : ResultInfo.success(po.convert2DTO());
    }

    @Override
    public ResultInfo<List<CounselorDTO>> detailByIds(Collection<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return ResultInfo.success();
        }
        if (Collections.min(ids) <= 0) {
            return ResultInfo.error("咨询师id列表中存在非法数据", Collections.emptyList());
        }
        List<CounselorPO> pos = new LambdaQueryChainWrapper<>(this.baseMapper)
                .in(BasePO::getId, ids)
                .list();
        List<CounselorDTO> dtos = pos.stream().map(CounselorPO::convert2DTO).collect(Collectors.toList());
        return ResultInfo.success(dtos);
    }
    
}

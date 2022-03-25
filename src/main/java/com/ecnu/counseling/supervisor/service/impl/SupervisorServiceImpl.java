package com.ecnu.counseling.supervisor.service.impl;

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
import com.ecnu.counseling.supervisor.mapper.SupervisorMapper;
import com.ecnu.counseling.supervisor.model.dto.SupervisorDTO;
import com.ecnu.counseling.supervisor.model.param.SupervisorEditParam;
import com.ecnu.counseling.supervisor.model.param.SupervisorLoginParam;
import com.ecnu.counseling.supervisor.model.param.SupervisorRegisterParam;
import com.ecnu.counseling.supervisor.model.po.SupervisorPO;
import com.ecnu.counseling.supervisor.service.SupervisorService;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/20 10:18 下午
 */
@Service
public class SupervisorServiceImpl extends ServiceImpl<SupervisorMapper, SupervisorPO> implements SupervisorService {

    @Override
    public ListPagingResponse<SupervisorPO> list(Collection<Integer> supervisorIds, Integer start, Integer length) {
        LambdaQueryChainWrapper<SupervisorPO> queryWrapper = new LambdaQueryChainWrapper<>(this.baseMapper)
                .in(SupervisorPO::getStatus, Lists.newArrayList(1, 2))
                .eq(BasePO::getIsDelete, 0);
        Integer count = queryWrapper.count();
        if (count == 0) {
            return ListPagingResponse.EMPTY_SUCCESS;
        }
        Page<SupervisorPO> page = new Page<>(start / length + 1, length);
        List<SupervisorPO> records = queryWrapper.page(page).getRecords();
        return new ListPagingResponse<>(ResponseCodeEnum.SUCCESS, BaseConstant.SUCCESS, records, start, length, count);
    }

    @Override
    public ResultInfo<Integer> register(SupervisorRegisterParam registerParam) {
        int count = new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(SupervisorPO::getPhone, registerParam.getPhone())
                .count();
        if (count != 0) {
            return ResultInfo.error("手机号已注册，请点击登陆");
        }

        SupervisorPO po = SupervisorPO.builder()
                .name(registerParam.getName())
                .password(Md5Util.encryptPassword(registerParam.getName(), registerParam.getPassword()))
                .phone(registerParam.getPhone())
                .age(registerParam.getAge())
                .gender(registerParam.getGender())
                .idCard(registerParam.getIdCard())
                .email(registerParam.getEmail())
                .userName(registerParam.getUserName())
                .workplace(registerParam.getWorkplace())
                .position(registerParam.getPosition())
                .status(registerParam.getStatus())
                .url(registerParam.getUrl())
                .countHelp(registerParam.getCountHelp())
                .qualification(registerParam.getQualification())
                .qualificationId(registerParam.getQualificationId())
                .build();
        this.save(po);
        
        return ResultInfo.success(po.getId());
    }

    @Override
    public BaseResult edit(SupervisorEditParam editParam) {
        SupervisorPO po = SupervisorPO.builder()
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
                .countHelp(editParam.getCountHelp())
                .qualification(editParam.getQualification())
                .qualificationId(editParam.getQualificationId())
                .build();
        boolean update = new LambdaUpdateChainWrapper<>(this.baseMapper)
                .eq(BasePO::getId, editParam.getSupervisorId())
                .update(po);
        return update ? BaseResult.SUCCESS : BaseResult.error("不存在该条记录");
    }

    @Override
    public ResultInfo<SupervisorDTO> login(SupervisorLoginParam loginParam) {
        List<SupervisorPO> pos = new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(SupervisorPO::getPhone, loginParam.getPhone())
                .eq(SupervisorPO::getPassword, Md5Util.encryptPassword(loginParam.getPhone(), loginParam.getPassword()))
                .list();
        return CollectionUtils.isEmpty(pos)
                ? ResultInfo.error("用户不存在")
                : ResultInfo.success(pos.get(0).convert2DTO());
    }

    @Override
    public ResultInfo<SupervisorDTO> detailById(Integer id) {
        if (id <= 0) {
            return ResultInfo.error("id不合法");
        }
        SupervisorPO po = this.getById(id);
        return po == null ? ResultInfo.error("数据不存在") : ResultInfo.success(po.convert2DTO());
    }

    @Override
    public ResultInfo<List<SupervisorDTO>> detailByIds(Collection<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return ResultInfo.success();
        }
        if (Collections.min(ids) <= 0) {
            return ResultInfo.error("督导id列表中存在非法数据", Collections.emptyList());
        }
        List<SupervisorPO> pos = new LambdaQueryChainWrapper<>(this.baseMapper)
                .in(BasePO::getId, ids)
                .list();
        List<SupervisorDTO> dtos = pos.stream().map(SupervisorPO::convert2DTO).collect(Collectors.toList());
        return ResultInfo.success(dtos);
    }
    
}

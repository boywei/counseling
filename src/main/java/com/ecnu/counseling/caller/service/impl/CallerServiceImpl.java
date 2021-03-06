package com.ecnu.counseling.caller.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecnu.counseling.caller.mapper.CallerMapper;
import com.ecnu.counseling.caller.model.dto.CallerDTO;
import com.ecnu.counseling.caller.model.param.CallerEditParam;
import com.ecnu.counseling.caller.model.param.CallerLoginParam;
import com.ecnu.counseling.caller.model.param.CallerRegisterParam;
import com.ecnu.counseling.caller.model.po.CallerPO;
import com.ecnu.counseling.caller.service.CallerService;
import com.ecnu.counseling.common.model.po.BasePO;
import com.ecnu.counseling.common.result.BaseResult;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.common.util.CheckUtils;
import com.ecnu.counseling.common.util.Md5Utils;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CallerServiceImpl extends ServiceImpl<CallerMapper, CallerPO> implements CallerService {

    @Override
    public ResultInfo<Integer> register(CallerRegisterParam registerParam) {
        int count = new LambdaQueryChainWrapper<>(this.baseMapper)
            .eq(CallerPO::getPhone, registerParam.getPhone())
            .count();
        if (count != 0) {
            return ResultInfo.error("手机号已注册，请点击登陆");
        }

        CallerPO po = CallerPO.builder()
            .name(registerParam.getName())
            .password(Md5Utils.encryptPassword(registerParam.getPhone(), registerParam.getPassword()))
            .phone(registerParam.getPhone())
            .emergencyName(registerParam.getEmergencyName())
            .emergencyPhone(registerParam.getEmergencyPhone())
            .build();
        this.save(po);
        return ResultInfo.success(po.getId());
    }

    @Override
    public BaseResult edit(CallerEditParam editParam) {
        CallerPO po = CallerPO.builder()
            .name(editParam.getName())
            .password(Md5Utils.encryptPassword(editParam.getPhone(), editParam.getPassword()))
            .phone(editParam.getPhone())
            .emergencyName(editParam.getEmergencyName())
            .emergencyPhone(editParam.getEmergencyPhone())
            .build();
        boolean update = new LambdaUpdateChainWrapper<>(this.baseMapper)
            .eq(BasePO::getId, editParam.getCallerId())
            .update(po);
        return update ? BaseResult.SUCCESS : BaseResult.error("不存在该条记录");
    }

    @Override
    public ResultInfo<CallerDTO> login(CallerLoginParam loginParam) {
        LambdaQueryWrapper<CallerPO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CallerPO::getPhone, loginParam.getPhone());
        Integer count = this.baseMapper.selectCount(queryWrapper);
        if (count == 0) {
            return ResultInfo.error("用户不存在");
        }
        if (count > 1) {
            log.error("DB中存在多条相同手机号的账号，phone = {}", loginParam.getPhone());
        }
        queryWrapper.eq(CallerPO::getPassword, Md5Utils.encryptPassword(loginParam.getPhone(), loginParam.getPassword()));
        List<CallerPO> pos = this.baseMapper.selectList(queryWrapper);
        return CollectionUtils.isEmpty(pos)
            ? ResultInfo.error("密码错误，请重试！")
            : ResultInfo.success(pos.get(0).convert2DTO());
    }

    @Override
    public ResultInfo<CallerDTO> detailById(Integer id) {
        if (id <= 0) {
            return ResultInfo.error("id不合法");
        }
        CallerPO po = this.getById(id);
        return po == null ? ResultInfo.error("数据不存在") : ResultInfo.success(po.convert2DTO());
    }

    @Override
    public ResultInfo<List<CallerDTO>> detailByIds(Collection<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return ResultInfo.success();
        }
        if (Collections.min(ids) <= 0) {
            return ResultInfo.error("访客id列表中存在非法数据", Collections.emptyList());
        }
        List<CallerPO> pos = new LambdaQueryChainWrapper<>(this.baseMapper)
            .in(BasePO::getId, ids)
            .list();
        List<CallerDTO> dtos = pos.stream().map(CallerPO::convert2DTO).collect(Collectors.toList());
        return ResultInfo.success(dtos);
    }

    @Override
    public BaseResult allExist(Collection<Integer> ids) {
        if (CheckUtils.anyEmptyIds(ids)) {
            return BaseResult.error("ids存在非法数据");
        }
        if (CollectionUtils.isEmpty(ids)) {
            return BaseResult.SUCCESS;
        }
        Integer count = new LambdaQueryChainWrapper<>(this.baseMapper)
            .in(BasePO::getId, ids)
            .count();
        return count == CollectionUtils.size(ids) ? BaseResult.SUCCESS : BaseResult.error("部分数据不存在");
    }
}

package com.ecnu.counseling.caller.service.impl;

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
import com.ecnu.counseling.common.util.Md5Util;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

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
            .password(Md5Util.encryptPassword(registerParam.getName(), registerParam.getPassword()))
            .phone(registerParam.getPhone())
            .emergencyContact(registerParam.getEmergencyContactName())
            .emergencyNumber(registerParam.getEmergencyNumber())
            .build();
        this.save(po);
        return ResultInfo.success(po.getId());
    }

    @Override
    public BaseResult edit(CallerEditParam editParam) {
        CallerPO po = CallerPO.builder()
            .name(editParam.getName())
            .password(Md5Util.encryptPassword(editParam.getPhone(), editParam.getPassword()))
            .phone(editParam.getPhone())
            .emergencyContact(editParam.getEmergencyContactName())
            .emergencyNumber(editParam.getEmergencyNumber())
            .build();
        boolean update = new LambdaUpdateChainWrapper<>(this.baseMapper)
            .eq(BasePO::getId, editParam.getCallerId())
            .update(po);
        return update ? BaseResult.SUCCESS : BaseResult.error("不存在该条记录");
    }

    @Override
    public ResultInfo<CallerDTO> login(CallerLoginParam loginParam) {
        List<CallerPO> pos = new LambdaQueryChainWrapper<>(this.baseMapper)
            .eq(CallerPO::getPhone, loginParam.getPhone())
            .eq(CallerPO::getPassword, Md5Util.encryptPassword(loginParam.getPhone(), loginParam.getPassword()))
            .list();
        return CollectionUtils.isEmpty(pos)
            ? ResultInfo.error("用户不存在")
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
}

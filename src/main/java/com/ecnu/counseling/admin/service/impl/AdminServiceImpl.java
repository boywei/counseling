package com.ecnu.counseling.admin.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecnu.counseling.admin.mapper.AdminMapper;
import com.ecnu.counseling.admin.model.param.AdminLoginParam;
import com.ecnu.counseling.admin.model.param.AdminRegisterParam;
import com.ecnu.counseling.admin.model.po.AdminPO;
import com.ecnu.counseling.admin.service.AdminService;
import com.ecnu.counseling.admin.model.dto.AdminDTO;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.common.util.Md5Utils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/19 10:38 下午
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, AdminPO> implements AdminService {

    @Override
    public ResultInfo<AdminDTO> login(AdminLoginParam loginParam) {
        List<AdminPO> pos = new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(AdminPO::getPhone, loginParam.getPhone())
                .eq(AdminPO::getPassword, Md5Utils.encryptPassword(loginParam.getPhone(), loginParam.getPassword()))
                .list();
        return CollectionUtils.isEmpty(pos)
                ? ResultInfo.error("用户不存在或密码错误！")
                : ResultInfo.success(pos.get(0).convert2DTO());
    }

    @Override
    public ResultInfo<Integer> register(AdminRegisterParam registerParam) {
        int count = new LambdaQueryChainWrapper<>(this.baseMapper)
                .eq(AdminPO::getPhone, registerParam.getPhone())
                .count();
        if (count != 0) {
            return ResultInfo.error("手机号已注册，请点击登陆");
        }

        AdminPO po = AdminPO.builder()
                .name(registerParam.getName())
                .password(Md5Utils.encryptPassword(registerParam.getPhone(), registerParam.getPassword()))
                .phone(registerParam.getPhone())
                .build();
        this.save(po);
        return ResultInfo.success(po.getId());
    }
    
}

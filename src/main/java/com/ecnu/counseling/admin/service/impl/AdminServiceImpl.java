package com.ecnu.counseling.admin.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecnu.counseling.admin.mapper.AdminMapper;
import com.ecnu.counseling.admin.model.param.AdminLoginParam;
import com.ecnu.counseling.admin.model.po.AdminPO;
import com.ecnu.counseling.admin.service.AdminService;
import com.ecnu.counseling.admin.model.dto.AdminDTO;
import com.ecnu.counseling.common.result.ResultInfo;
import com.ecnu.counseling.common.util.Md5Util;
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
                .eq(AdminPO::getName, loginParam.getName())
                .eq(AdminPO::getPassword, Md5Util.encryptPassword(loginParam.getName(), loginParam.getPassword()))
                .list();
        return CollectionUtils.isEmpty(pos)
                ? ResultInfo.error("用户不存在或密码错误！")
                : ResultInfo.success(pos.get(0).convert2DTO());
    }

    public static void main(String[] args) {
        // 测试：amdin：a,a; caller: 19921878760,12345678
        System.out.println(Md5Util.encryptPassword("19921878760", "12345678"));
    }
    
}

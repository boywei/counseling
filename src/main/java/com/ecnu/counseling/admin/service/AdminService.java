package com.ecnu.counseling.admin.service;

import com.ecnu.counseling.admin.model.dto.AdminDTO;
import com.ecnu.counseling.admin.model.param.AdminLoginParam;
import com.ecnu.counseling.admin.model.param.AdminRegisterParam;
import com.ecnu.counseling.common.result.ResultInfo;

/**
 * @Description
 * @Author wei
 * @Date 2022/3/19 10:38 下午
 */
public interface AdminService {

    ResultInfo<AdminDTO> login(AdminLoginParam loginParam);

    ResultInfo<Integer> register(AdminRegisterParam registerParam);

}

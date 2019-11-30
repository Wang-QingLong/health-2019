package com.itcat.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/29 20:04
 * @description:
 */
@Service(interfaceClass = UserLoginService.class)
@Transactional
public interface UserLoginService {
}

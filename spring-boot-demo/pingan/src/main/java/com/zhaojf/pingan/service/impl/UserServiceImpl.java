package com.zhaojf.pingan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhaojf.pingan.entity.User;
import com.zhaojf.pingan.service.UserService;
import com.zhaojf.pingan.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-07-14 11:23:15
*/
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}





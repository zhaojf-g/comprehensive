package com.zhaojf.pingan.mapper;

import com.zhaojf.pingan.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-07-14 11:23:15
* @Entity com.zhaojf.pingan.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}





package com.poxiao.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.poxiao.cloud.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * user表 Mapper 接口
 * </p>
 *
 * @author zhangsan
 * @since 2020-04-23
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

}

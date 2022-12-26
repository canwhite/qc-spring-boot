package com.qc.boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qc.boot.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 功能描述:
 *
 * @author lijinhua
 * @date 2022/9/3 16:51
 */
//注意对应的实体类最好跟表名一致
@Mapper
public interface UsersMapper extends BaseMapper<User> {
}

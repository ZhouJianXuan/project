package com.sise.graduation.dao.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;

/**
 * @ClassName BaseMapperScan
 * @Description
 * @Author CCJ
 * @Date 2019/9/15 0:32
 **/
@MapperScans({
        @MapperScan("com.sise.graduation.dao.common.mapper"),
        @MapperScan("com.sise.graduation.common.dao.mapper")
})
public class BaseMapperScan {
}

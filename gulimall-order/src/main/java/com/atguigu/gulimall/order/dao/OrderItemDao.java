package com.atguigu.gulimall.order.dao;

import com.atguigu.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author methenberg
 * @email hawking3361@gmail.com
 * @date 2020-12-31 15:40:20
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}

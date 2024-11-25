package com.example.dcloud_shop.mapper;

import com.example.dcloud_shop.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dcloud_shop.entity.vo.ProductVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 产品表 Mapper 接口
 * </p>
 *
 * @author RainbowJier
 * @since 2024-11-23
 */

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}

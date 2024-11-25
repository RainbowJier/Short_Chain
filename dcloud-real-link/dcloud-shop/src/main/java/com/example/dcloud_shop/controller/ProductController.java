package com.example.dcloud_shop.controller;


import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_shop.entity.vo.ProductVo;
import com.example.dcloud_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.example.dcloud_common.util.JsonData.buildSuccess;

/**
 * <p>
 * 产品表 前端控制器
 * </p>
 *
 * @author RainbowJier
 * @since 2024-11-23
 */
@RestController
@RequestMapping("/api/product/v1")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 获取产品列表
     */
    @GetMapping("/list")
    public JsonData list(){
        List<ProductVo> list = productService.list();
        return JsonData.buildSuccess(list);
    }

    /**
     * 获取产品详情
     */
    @GetMapping("/detail/{product_id}")
    public JsonData detail(@PathVariable("product_id") Long productId){
        ProductVo productVo = productService.findDetailById(productId);
        return JsonData.buildSuccess(productVo);
    }

}

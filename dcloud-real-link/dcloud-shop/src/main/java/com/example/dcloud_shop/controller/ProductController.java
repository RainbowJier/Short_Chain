package com.example.dcloud_shop.controller;

import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_shop.model.vo.ProductVo;
import com.example.dcloud_shop.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/product/v1")
public class ProductController {

    @Resource
    private ProductService productService;

    /**
     * Get product list.
     */
    @GetMapping("/list")
    public JsonData list(){
        List<ProductVo> list = productService.list();
        return JsonData.buildSuccess(list);
    }

    /**
     * GEt product detail.
     */
    @GetMapping("/detail/{product_id}")
    public JsonData detail(@PathVariable("product_id") Long productId){
        ProductVo productVo = productService.findDetailById(productId);
        return JsonData.buildSuccess(productVo);
    }

}

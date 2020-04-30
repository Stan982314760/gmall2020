package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.service.SkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin
public class SkuController {
    @Reference
    SkuService skuService;

    @GetMapping("spuSaleAttrList")
    public List<PmsProductSaleAttr> spuSaleAttrList(@RequestParam("spuId") String spuId) {
        log.debug("查找spuId={}对应的销售属性列表", spuId);

        List<PmsProductSaleAttr> pmsProductSaleAttrs = skuService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }

    @GetMapping("spuImageList")
    public List<PmsProductImage> spuImageList(@RequestParam("spuId") String spuId) {
        log.debug("查找spuId={}对应的图片列表", spuId);

        List<PmsProductImage> pmsProductImages = skuService.spuImageList(spuId);
        return pmsProductImages;
    }

    @PostMapping("saveSkuInfo")
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo) {
        log.debug("添加sku信息 spuId={}" , pmsSkuInfo.getSpuId());

        // 把spuId转为productId
        pmsSkuInfo.setProductId(pmsSkuInfo.getSpuId());


        // 默认图片处理
        if (StringUtils.isEmpty(pmsSkuInfo.getSkuDefaultImg())) {
            pmsSkuInfo.setSkuDefaultImg(pmsSkuInfo.getSkuImageList().get(0).getImgUrl());
        }

        skuService.saveSkuInfo(pmsSkuInfo);

        return "success";
    }
}

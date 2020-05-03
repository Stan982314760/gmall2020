package com.atguigu.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.service.SpuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Controller
@Slf4j
@CrossOrigin
public class ItemController {
    @Reference
    SkuService skuService;
    @Reference
    SpuService spuService;

    @Qualifier("mainThreadPool")
    @Autowired
    ThreadPoolExecutor mainThreadPool;

    @GetMapping("{skuId}.html")
    public String item(@PathVariable("skuId") String skuId, Model model) {
        log.debug("main...开始查找skuId={}的商品详情信息", skuId);

        // 查询skuInfo
        CompletableFuture<PmsSkuInfo> future1 = CompletableFuture.supplyAsync(
                () -> {
                    log.debug(Thread.currentThread().getName() + "  开始查找skuId={}的商品销售属性", skuId);
                    return skuService.getSkuInfo(skuId);
                }, mainThreadPool);

        PmsSkuInfo skuInfo = null;
        try {
            skuInfo = future1.get();
            model.addAttribute("skuInfo", skuInfo);
        } catch (Exception e) {
            log.error("异步任务查询skuInfo出错");
            e.printStackTrace();
        }


        // 查询spu销售属性 及对sku销售属性进行checked选中
        final PmsSkuInfo skuInfoTemp = skuInfo; // lamda表达式里面要final变量
        CompletableFuture<List<PmsProductSaleAttr>> future2 = CompletableFuture.supplyAsync(() -> {
            if (skuInfoTemp != null) {
                log.debug(Thread.currentThread().getName() + "  开始查找spu销售属性");
                return spuService.spuSaleAttrListCheckBySku(skuInfoTemp.getProductId(), skuId);
            }
            return null;
        }, mainThreadPool);

        List<PmsProductSaleAttr> spuSaleAttrListCheckBySku = null;
        try {
            spuSaleAttrListCheckBySku = future2.get();
            model.addAttribute("spuSaleAttrListCheckBySku", spuSaleAttrListCheckBySku);
        } catch (Exception e) {
            log.error("异步任务查询spu销售属性出错");
        }


        // 查询当前sku兄弟姐妹 封装成哈希表
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            if (skuInfoTemp != null) {
                log.debug(Thread.currentThread().getName() + "  开始查找当前sku的兄弟姐妹");
                return skuService.getSkuJsonStr(skuInfoTemp.getProductId());
            }
            return null;
        }, mainThreadPool);

        String saleAttrValueJsonStr = null;
        try {
            saleAttrValueJsonStr = future3.get();
            model.addAttribute("saleAttrValueJsonStr", saleAttrValueJsonStr);
        } catch (Exception e) {
            log.error("异步任务查找当前sku兄弟姐妹出错");
            e.printStackTrace();
        }


        log.debug("main...准备返回结果");
        return "item";
    }
}

package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.manage.util.UploadUtil;
import com.atguigu.gmall.service.SpuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
@Slf4j
public class SpuController {
    @Reference
    SpuService spuService;

    @GetMapping("spuList")
    public List<PmsProductInfo> spuList(@RequestParam("catalog3Id") String catalog3Id) {
        log.debug("SpuController****查找三级分类id={}对应的spu列表", catalog3Id);

        List<PmsProductInfo> pmsProductInfos = spuService.spuList(catalog3Id);
        return pmsProductInfos;
    }


    @PostMapping("baseSaleAttrList")
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        log.debug("SpuController****查找销售属性列表");

        List<PmsBaseSaleAttr> pmsBaseSaleAttrs = spuService.baseSaleAttrList();
        return pmsBaseSaleAttrs;
    }


    @PostMapping("saveSpuInfo")
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo) {
        log.debug("SpuController****保存spu信息");

        spuService.saveSpuInfo(pmsProductInfo);

        return "success";
    }


    @PostMapping("fileUpload")
    public String fileUpload(@RequestParam("file") MultipartFile file) {
        log.debug("SpuController****准备上传图片");

        UploadUtil uploadUtil = new UploadUtil();
        String imageUrl = uploadUtil.uploadImage(file);

        if (StringUtils.isEmpty(imageUrl)) {
            log.error("上传失败");
            return null;
        }
        return imageUrl;
    }
}

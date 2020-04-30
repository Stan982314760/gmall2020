package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.service.AttrInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin
public class AttrInfoController {
    @Reference
    AttrInfoService attrInfoService;

    @GetMapping("attrInfoList")
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        log.debug("AttrInfoController****开始查询catalog3Id={}分类对应的平台属性列表", catalog3Id);

        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrInfoService.attrInfoList(catalog3Id);
        return pmsBaseAttrInfos;
    }


    @PostMapping("saveAttrInfo")
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo) {

        if (pmsBaseAttrInfo.getId() == null) {
            log.debug("AttrInfoController***准备添加PmsBaseAttrInfo={}", pmsBaseAttrInfo);

            attrInfoService.saveAttrInfo(pmsBaseAttrInfo);
        } else {
            log.debug("AttrInfoController***准备修改PmsBaseAttrInfo={}", pmsBaseAttrInfo);

            attrInfoService.saveAttrValue(pmsBaseAttrInfo);
        }

        return "ok";
    }

    @PostMapping("getAttrValueList")
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        log.debug("AttrInfoController****开始查询平台属性id={}的属性值列表", attrId);

        List<PmsBaseAttrValue> pmsBaseAttrValues = attrInfoService.getAttrValueList(attrId);
        return pmsBaseAttrValues;
    }
}

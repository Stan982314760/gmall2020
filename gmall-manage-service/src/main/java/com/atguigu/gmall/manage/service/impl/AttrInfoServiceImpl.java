package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.atguigu.gmall.service.AttrInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
public class AttrInfoServiceImpl implements AttrInfoService {
    @Autowired
    PmsBaseAttrInfoMapper attrInfoMapper;
    @Autowired
    PmsBaseAttrValueMapper attrValueMapper;

    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        log.debug("AttrInfoServiceImpl****开始查询catalog3Id={}分类对应的平台属性列表", catalog3Id);

        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrInfoMapper.select(pmsBaseAttrInfo);

        // 封装每个平台属性对应的平台属性值列表
        for (PmsBaseAttrInfo baseAttrInfo : pmsBaseAttrInfos) {
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(baseAttrInfo.getId());
            List<PmsBaseAttrValue> pmsBaseAttrValues = attrValueMapper.select(pmsBaseAttrValue);

            baseAttrInfo.setAttrValueList(pmsBaseAttrValues);
        }

        return pmsBaseAttrInfos;
    }

    @Override
    public void saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        log.debug("AttrInfoServiceImpl****准备添加平台属性");

        String attrInfoId = isAttrInfoExisted(pmsBaseAttrInfo.getAttrName());
        if (StringUtils.isEmpty(attrInfoId)) {
            log.debug("平台属性{}不存在 准备添加到数据库", pmsBaseAttrInfo.getAttrName());
            attrInfoMapper.insert(pmsBaseAttrInfo);
            attrInfoId = pmsBaseAttrInfo.getId();
        }

        //==========这里是用查数据库校验重复的方法 去判断要不要添加
        //==========也可以把attrInfoId对应的所有属性值列表全部删除后 再添加 省去了判断重复
        log.debug("AttrInfoServiceImpl****准备添加平台属性值列表");

        List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
        for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
            if (!isAttrValueExisted(pmsBaseAttrValue.getValueName())) {
                log.debug("平台属性值{}不存在 准备添加到数据库", pmsBaseAttrValue.getValueName());
                pmsBaseAttrValue.setAttrId(attrInfoId); // 设置外键
                attrValueMapper.insert(pmsBaseAttrValue);
            }
        }

    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        log.debug("AttrInfoServiceImpl****查询attrId={}对应的属性值列表", attrId);

        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);

        return attrValueMapper.select(pmsBaseAttrValue);
    }

    @Override
    @Transactional
    public void saveAttrValue(PmsBaseAttrInfo pmsBaseAttrInfo) {
        log.debug("AttrInfoServiceImpl****准备删除attrId={}对应的平台属性值列表", pmsBaseAttrInfo.getId());

        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
        int delete = attrValueMapper.delete(pmsBaseAttrValue);

        log.debug("删除完成 共删除{}行", delete);


        log.debug("AttrInfoServiceImpl****准备添加attrId={}的平台属性值", pmsBaseAttrInfo.getId());

        List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
        for (PmsBaseAttrValue attrValue : attrValueList) {
            attrValue.setAttrId(pmsBaseAttrInfo.getId());
            attrValueMapper.insert(attrValue);
        }
    }


    private String isAttrInfoExisted(String attrName) {
        log.debug("开始校验平台属性={}", attrName);

        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setAttrName(attrName);
        PmsBaseAttrInfo pmsBaseAttrInfo1 = attrInfoMapper.selectOne(pmsBaseAttrInfo);

        if (pmsBaseAttrInfo1 != null) {
            return pmsBaseAttrInfo1.getId();
        }
        return null;
    }

    private boolean isAttrValueExisted(String attrValue) {
        log.debug("开始校验平台属性值={}", attrValue);

        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setValueName(attrValue);
        PmsBaseAttrValue pmsBaseAttrValue1 = attrValueMapper.selectOne(pmsBaseAttrValue);

        return pmsBaseAttrValue1 != null;
    }
}

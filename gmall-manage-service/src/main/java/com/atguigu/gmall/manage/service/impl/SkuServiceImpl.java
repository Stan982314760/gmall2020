package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.mapper.*;
import com.atguigu.gmall.service.SkuService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;
    @Autowired
    PmsProductImageMapper pmsProductImageMapper;
    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;
    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;
    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;
    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    // 保存skuId
    ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        // 查询销售属性列表
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);

        // 查询每个销售属性 对应的销售属性值列表
        for (PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrs) {
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setProductId(spuId);
            pmsProductSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues1 = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);

            productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues1);

        }

        return pmsProductSaleAttrs;
    }

    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);

        return pmsProductImageMapper.select(pmsProductImage);
    }

    /**
     * sku信息保存 四张表数据同时插入 学习完雷神后 优化代码
     *
     * @param pmsSkuInfo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        SkuServiceImpl skuServiceProxy = (SkuServiceImpl) AopContext.currentProxy();

        skuServiceProxy.savePmsSkuInfo(pmsSkuInfo);

        skuServiceProxy.savePmsSkuImage(pmsSkuInfo);

        skuServiceProxy.savePmsSkuAttrValue(pmsSkuInfo);

        skuServiceProxy.savePmsSkuSaleAttrValue(pmsSkuInfo);
    }

    // 保存pms_sku_sale_attr_value
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void savePmsSkuSaleAttrValue(PmsSkuInfo pmsSkuInfo) {
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(threadLocal.get());
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);

            int i = 10 / 0;
        }
    }

    //保存pms_sku_attr_value
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void savePmsSkuAttrValue(PmsSkuInfo pmsSkuInfo) {
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(threadLocal.get());
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }
    }

    // 保存pms_sku_image
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void savePmsSkuImage(PmsSkuInfo pmsSkuInfo) {
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setProductImgId(pmsSkuInfo.getProductId());
            pmsSkuImage.setSkuId(threadLocal.get());
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }
    }

    // 保存pms_sku_info
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void savePmsSkuInfo(PmsSkuInfo pmsSkuInfo) {
        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        threadLocal.set(pmsSkuInfo.getSpuId());
    }
}

package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.mapper.*;
import com.atguigu.gmall.service.SpuService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;
    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;
    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
    @Autowired
    PmsProductImageMapper pmsProductImageMapper;
    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    // 用来保存生成的产品id
    ThreadLocal<String> threadLocal = new ThreadLocal<>();



    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        List<PmsProductInfo> pmsProductInfos = pmsProductInfoMapper.select(pmsProductInfo);

        return pmsProductInfos;
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {

        return pmsBaseSaleAttrMapper.selectAll();
    }

    /**
     * 多张表的保存 pms_product_info,pms_product_image,pms_product_sale_attr,pms_product_sale_attr_value
     * TODO 看完雷神视频回来 加上事务重新修改一下这个方法
     *
     * @param pmsProductInfo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {

        // 获取暴露的代理对象
        SpuServiceImpl spuServiceProxy = (SpuServiceImpl) AopContext.currentProxy();


        spuServiceProxy.saveProductInfo(pmsProductInfo);

        spuServiceProxy.saveProductImage(pmsProductInfo);

        spuServiceProxy.saveProductSaleAttr(pmsProductInfo);

    }

    // 保存销售属性
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductSaleAttr(PmsProductInfo pmsProductInfo) {
        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for (PmsProductSaleAttr pmsProductSaleAttr : spuSaleAttrList) {
            // 保存销售属性表
            pmsProductSaleAttr.setProductId(threadLocal.get());
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);


            // 保存销售属性值表
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();
            for (PmsProductSaleAttrValue pmsProductSaleAttrValue : pmsProductSaleAttrValueList) {
                pmsProductSaleAttrValue.setProductId(threadLocal.get());
                pmsProductSaleAttrValue.setSaleAttrId(pmsProductSaleAttr.getSaleAttrId());
                pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);
            }
        }
    }

    // 保存pms_product_image
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductImage(PmsProductInfo pmsProductInfo) {
        List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();
        for (PmsProductImage pmsProductImage : spuImageList) {
            pmsProductImage.setProductId(threadLocal.get());
            pmsProductImageMapper.insertSelective(pmsProductImage);
        }
    }

    // 保存pms_product_info
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductInfo(PmsProductInfo pmsProductInfo) {
        pmsProductInfoMapper.insertSelective(pmsProductInfo);
        threadLocal.set(pmsProductInfo.getId());
    }

}

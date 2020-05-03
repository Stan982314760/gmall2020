package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.consts.GmallConst;
import com.atguigu.gmall.manage.mapper.*;
import com.atguigu.gmall.service.SkuService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

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


    @Override
    public PmsSkuInfo getSkuInfo(String skuId) {
        // 先查缓存 设计缓存里面存储的数据类型是 Object:Id:Field 即 sku:107:info
        String key = GmallConst.REDIS_SKU_PREFIX + skuId + GmallConst.REDIS_SKU_INFO_SUFFIX;
        Object info = redisTemplate.opsForValue().get(key);
        if (info != null) {
            PmsSkuInfo pmsSkuInfoinfo = (PmsSkuInfo) info;
            return pmsSkuInfoinfo;
        }

        // 缓存中没有 查询数据库
        // 1.skuInfo查询
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

        if (skuInfo == null) {
            // 解决缓存穿透问题 放一个空值 三分钟过期
            redisTemplate.opsForValue().set(key, "", 3L, TimeUnit.MINUTES);
            return null;

        }

        // 2.skuImage查询
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);
        skuInfo.setSkuImageList(pmsSkuImages);

        // 3.将查询结果放进缓存
        redisTemplate.opsForValue().set(key, skuInfo);
        return skuInfo;

    }

    @Override
    public String getSkuJsonStr(String productId) {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectSkusByProductId(productId);

        Map<String, String> map = new HashMap<>();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            String value = pmsSkuInfo.getId();
            String key = "";

            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                key += pmsSkuSaleAttrValue.getSaleAttrValueId();
                key += "|";
            }

            map.put(key, value);
        }

        String skuSaleAttrValueJsonStr = JSON.toJSONString(map);

        return skuSaleAttrValueJsonStr;
    }
}

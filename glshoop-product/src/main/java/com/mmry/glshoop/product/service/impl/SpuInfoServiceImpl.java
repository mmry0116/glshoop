package com.mmry.glshoop.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mmry.common.to.SkuReductionTO;
import com.mmry.common.to.SpuBoundsTO;
import com.mmry.common.utils.R;
import com.mmry.glshoop.product.dao.*;
import com.mmry.glshoop.product.entity.*;
import com.mmry.glshoop.product.feign.CouponFeignService;
import com.mmry.glshoop.product.service.*;
import com.mmry.glshoop.product.vo.SpuInfoVO;
import com.mmry.glshoop.product.vo.skuvo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmry.common.utils.PageUtils;
import com.mmry.common.utils.Query;

import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    BrandDao brandDao;
    @Autowired
    CouponFeignService couponFeignService;
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    SkuInfoDao skuInfoDao;
    @Autowired
    SkuImagesService skuImagesService;
    @Autowired
    ProductAttrValueService productAttrValueService;
    @Autowired
    SpuInfoDescDao spuInfoDescDao;
    @Autowired
    SpuImagesService spuImagesService;
    @Autowired
    AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveSpuInfo(SpuSaveVO spuSaveVO) {
        //1.保存基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVO, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        baseMapper.insert(spuInfoEntity);
        Long spuId = spuInfoEntity.getId();

        //2.保存spu描述图片 pms_spu_info_desc
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        List<String> decript = spuSaveVO.getDecript();
        spuInfoDescEntity.setDecript(String.join(",", decript));
        spuInfoDescEntity.setSpuId(spuId);
        spuInfoDescDao.insert(spuInfoDescEntity);

        //3.保存spu图片集 pms_images
        List<String> images = spuSaveVO.getImages();
        spuImagesService.saveBatchImages(spuId, images);

        //4.保存spu规格参数 pms_product_attr_value
        List<BaseAttrs> baseAttrs = spuSaveVO.getBaseAttrs();
        List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map(baseAttr -> {
            ProductAttrValueEntity entity = new ProductAttrValueEntity();
            entity.setSpuId(spuId);
            Long attrId = baseAttr.getAttrId();
            entity.setAttrId(attrId);
            entity.setAttrName(attrDao.selectById(attrId).getAttrName());
            entity.setAttrValue(baseAttr.getAttrValues());
            entity.setQuickShow(baseAttr.getShowDesc());
            return entity;
        }).collect(Collectors.toList());
        productAttrValueService.saveBatch(productAttrValueEntities);

        //5.保存spu的积分信息 gulimall_sms -> sms_spu_bounds
        SpuBoundsTO spuBoundsTO = new SpuBoundsTO();
        BeanUtils.copyProperties(spuSaveVO.getBounds(), spuBoundsTO);
        spuBoundsTO.setSpuId(spuId);
        R rSave = couponFeignService.save(spuBoundsTO);
        if (rSave.getCode() != 0)
            throw new RuntimeException("保存spu的积分信息失败");


        //5.1) sku基本信息 pms_sku_info
        List<Skus> skus = spuSaveVO.getSkus();
        skus.forEach(sku -> {
            List<Images> images1 = sku.getImages();
            String defaultImage = "";
            for (Images i : images1) {
                if (i.getDefaultImg() == 1)
                    defaultImage = i.getImgUrl();
            }
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(sku, skuInfoEntity);
            skuInfoEntity.setSpuId(spuId);
            skuInfoEntity.setBrandId(spuSaveVO.getBrandId());
            skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
            skuInfoEntity.setSaleCount(0L);
            skuInfoEntity.setSkuDefaultImg(defaultImage);
            skuInfoDao.insert(skuInfoEntity);
            Long skuId = skuInfoEntity.getSkuId();

            //5.2) sku图片信息  pms_sku_images
            List<SkuImagesEntity> imagesEntities = images1.stream().map(e -> {
                        SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                        BeanUtils.copyProperties(e, skuImagesEntity);
                        skuImagesEntity.setSkuId(skuId);
                        return skuImagesEntity;
                    }).filter(e -> (!StringUtils.isEmpty(e.getImgUrl())))
                    .collect(Collectors.toList());
            skuImagesService.saveBatch(imagesEntities);

            //5.3) sku销售属性信息  pms_sku_sale_attr_value
            List<Attr> attr = sku.getAttr();
            List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(e -> {
                SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                BeanUtils.copyProperties(e, skuSaleAttrValueEntity);
                skuSaleAttrValueEntity.setSkuId(skuId);
                return skuSaleAttrValueEntity;
            }).collect(Collectors.toList());
            skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

            //5.4) sku的优惠、满减等信息 sms_sku_ladder/sms_sku_full_reduction
            SkuReductionTO skuReductionTO = new SkuReductionTO();
            skuReductionTO.setSkuId(skuId);
            BeanUtils.copyProperties(sku, skuReductionTO);

            R rSkuReduction = couponFeignService.saveSkuReduction(skuReductionTO);
            if (rSkuReduction.getCode() != 0)
                throw new RuntimeException("保存sku的优惠、满减等信息失败");

        });


    }

    @Override
    public PageUtils queryPageBycondition(Map<String, Object> params) {
        LambdaQueryWrapper<SpuInfoEntity> wrapper = new LambdaQueryWrapper<>();
        String key = (String) params.get("key");
        String status = (String) params.get("status");
        String brandId = (String) params.get("brandId");
        String catelogId = (String) params.get("catelogId");
        if (StringUtils.isNotEmpty(key))
            wrapper.and(w -> w.eq(SpuInfoEntity::getId, key).or().like(SpuInfoEntity::getSpuName, key));

        if (StringUtils.isNotEmpty(status))
            wrapper.eq(SpuInfoEntity::getPublishStatus, status);

        if (StringUtils.isNotEmpty(brandId))
            wrapper.eq(SpuInfoEntity::getBrandId, brandId);

        if (StringUtils.isNotEmpty(catelogId))
            wrapper.eq(SpuInfoEntity::getCatalogId, catelogId);


        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        //设置CatalogName BrandName
        PageUtils pageUtils = new PageUtils(page);
        List<SpuInfoEntity> spuInfoEntities = page.getRecords();
        List<SpuInfoVO> spuInfoVOS = spuInfoEntities.stream().map(e -> {
            CategoryEntity category = categoryDao.selectById(e.getCatalogId());
            BrandEntity brand = brandDao.selectById(e.getBrandId());
            SpuInfoVO spuInfoVO = new SpuInfoVO();
            BeanUtils.copyProperties(e, spuInfoVO);
            spuInfoVO.setBrandName(brand.getName());
            spuInfoVO.setCatalogName(category.getName());
            return spuInfoVO;
        }).collect(Collectors.toList());
        //将pageUtils 重新设置数据
        pageUtils.setList(spuInfoVOS);
        return pageUtils;
    }


}
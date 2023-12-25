package com.mmry.glshoop.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmry.common.utils.PageUtils;
import com.mmry.common.utils.Query;

import com.mmry.glshoop.product.dao.SpuImagesDao;
import com.mmry.glshoop.product.entity.SpuImagesEntity;
import com.mmry.glshoop.product.service.SpuImagesService;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 批量保存spu_image
     * @param spuId
     * @param images
     */
    @Override
    public void saveBatchImages(Long spuId, List<String> images) {
        List<SpuImagesEntity> spuImagesEntities = images.stream().map(imageUrl -> {
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            spuImagesEntity.setSpuId(spuId);
            spuImagesEntity.setImgUrl(imageUrl);
            return spuImagesEntity;
        }).collect(Collectors.toList());
        this.saveBatch(spuImagesEntities);
    }

}
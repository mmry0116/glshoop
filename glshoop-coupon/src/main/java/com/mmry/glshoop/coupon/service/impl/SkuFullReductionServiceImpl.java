package com.mmry.glshoop.coupon.service.impl;

import com.mmry.common.to.MemberPrice;
import com.mmry.common.to.SkuReductionTO;
import com.mmry.glshoop.coupon.entity.MemberPriceEntity;
import com.mmry.glshoop.coupon.entity.SkuLadderEntity;
import com.mmry.glshoop.coupon.service.MemberPriceService;
import com.mmry.glshoop.coupon.service.SkuLadderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmry.common.utils.PageUtils;
import com.mmry.common.utils.Query;

import com.mmry.glshoop.coupon.dao.SkuFullReductionDao;
import com.mmry.glshoop.coupon.entity.SkuFullReductionEntity;
import com.mmry.glshoop.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {
    @Autowired
    MemberPriceService memberPriceService;
    @Autowired
    SkuLadderService skuLadderService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTO TO) {
        //5.4) sku的优惠、满减等信息 sms_sku_ladder/
        //打折信息
        if (TO.getFullCount() > 0 && StringUtils.isNotEmpty(TO.getDiscount().toString())) {
            SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
            skuLadderEntity.setSkuId(TO.getSkuId());
            skuLadderEntity.setFullCount(TO.getFullCount());
            skuLadderEntity.setDiscount(TO.getDiscount());
            skuLadderEntity.setAddOther(TO.getCountStatus());//打折状态是否参与其他优惠
            skuLadderService.save(skuLadderEntity);
        }
        //满减信息 sms_sku_full_reduction
        if (StringUtils.isNotEmpty(TO.getFullPrice().toString()) && StringUtils.isNotEmpty(TO.getReducePrice().toString())) {
            SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
            skuFullReductionEntity.setSkuId(TO.getSkuId());
            skuFullReductionEntity.setFullPrice(TO.getFullPrice());
            skuFullReductionEntity.setReducePrice(TO.getReducePrice());
            this.save(skuFullReductionEntity);
        }
        //sms_member_price
        List<MemberPrice> memberPrices = TO.getMemberPrice();
        if (memberPrices.size() != 0) {
            List<MemberPriceEntity> memberPriceEntities = memberPrices.stream().map(memberPrice -> {
                MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                memberPriceEntity.setSkuId(TO.getSkuId());
                memberPriceEntity.setMemberLevelId(memberPrice.getId());
                memberPriceEntity.setMemberLevelName(memberPrice.getName());
                memberPriceEntity.setMemberPrice(memberPrice.getPrice());
                memberPriceEntity.setAddOther(1);
                return memberPriceEntity;
            }).filter(e-> e.getMemberPrice().compareTo(new BigDecimal("0"))==1) .collect(Collectors.toList());
            memberPriceService.saveBatch(memberPriceEntities);
        }
    }


}
package com.mmry.glshoop.product.feign;

import com.mmry.common.to.SkuReductionTO;
import com.mmry.common.to.SpuBoundsTO;
import com.mmry.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author : mmry
 * @date : 2023/12/17 13:30
 */
@FeignClient("glshoop-coupon")
public interface CouponFeignService {
    @PostMapping("/coupon/spubounds/save")
    R save(@RequestBody SpuBoundsTO spuBoundsTO);

    @PostMapping("/coupon/skufullreduction/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTO skuReductionTO);
}

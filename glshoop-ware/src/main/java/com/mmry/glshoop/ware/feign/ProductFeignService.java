package com.mmry.glshoop.ware.feign;

import com.mmry.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : mmry
 * @date : 2023/12/20 16:08
 */
@FeignClient("glshoop-product")
public interface ProductFeignService {
    @RequestMapping("/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}

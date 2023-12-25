package com.mmry.glshoop.member.feign;

import com.mmry.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("glshoop-coupon")
public interface CouponFeignService {
    @RequestMapping("/coupon/coupon/member/feign")
    public R memberfeign();
}

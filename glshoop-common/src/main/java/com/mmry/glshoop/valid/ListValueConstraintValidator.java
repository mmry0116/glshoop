package com.mmry.glshoop.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * 校验BrandEntity类showStatus字段
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {
    private Set<Integer> set = new HashSet<>();

    @Override
    public void initialize(ListValue constraintAnnotation) {
        //初始化值
        int[] vals = constraintAnnotation.vals();
        if (vals.length>0){
            for (int val : vals)
                set.add(val);
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        //校验值是否合法
        return set.contains(value);
    }
}



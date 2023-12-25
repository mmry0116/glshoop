package com.mmry.glshoop.product.exception;

import com.mmry.common.utils.R;
import com.mmry.glshoop.exception.BuzCodeEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Priority;
import java.util.HashMap;

@RestControllerAdvice(basePackages = "com.mmry.glshoop.product.controller") //basePackages： 接管哪些包下面的异常
@Slf4j
@Priority(1)
public class GlshoopExceptionControllerAdvice {

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)//value: 处理什么类型的异常
    public R handlerValidException(MethodArgumentNotValidException exception) {
        //获取绑定异常结果
        BindingResult bindingResult = exception.getBindingResult();
        //取出绑定异常k-v
        HashMap<String, String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach(field -> {
            map.put(field.getField(), field.getDefaultMessage());
        });
        //把异常返回给前端
        log.error("数据出现异常");
        return R.error(BuzCodeEnums.VALID_EXCEPTION.getCode(), BuzCodeEnums.VALID_EXCEPTION.getMsg()).put("data", map);
    }

    /**
     * 集中异常处理
     *
     * @param exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R handlerException(Exception exception) {
        //系统未知异常直接返回给前端
        log.error("错误",exception);
        return R.error(BuzCodeEnums.UNKNOWN_EXCEPTION.getCode(), BuzCodeEnums.UNKNOWN_EXCEPTION.getMsg());
    }

}

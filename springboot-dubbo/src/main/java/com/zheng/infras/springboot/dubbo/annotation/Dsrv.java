package com.zheng.infras.springboot.dubbo.annotation;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2016/11/27/027.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Dsrv {
}

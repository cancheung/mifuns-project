package com.mifuns.security.web.bind.annotation;



import com.mifuns.security.shiro.constants.SecurityConstants;

import java.lang.annotation.*;


@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {

    /**
     * 当前用户在request中的名字
     *
     * @return
     */
    String value() default SecurityConstants.CURRENT_USER;

}

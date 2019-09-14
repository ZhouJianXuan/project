package com.sise.graduation.common.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessAuthority {

    String PUBLIC = "PUBLIC";

    String PRIVATE = "PRIVATE";

    String GENERAL = "GENERAL";

    String value() default PUBLIC;
}

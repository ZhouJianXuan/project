package com.sise.graduation.common.web.component;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
 
    @Override
    public void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        applicationContext = arg0;
    }
 
    /**
     * 获取applicationContext对象
     * @return
     */
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static boolean containsBean(String name) {
    	return applicationContext.containsBean(name);
    }

    /**
     * 根据bean的name来查找对象
     * @param id
     * @return
     */
    public static Object getBeanById(String name){
        return applicationContext.getBean(name);
    }
    
    /**
     * 根据bean的类型来查找对象
     * @param clazz
     * @return
     */
    public static <T> T getBeanByType(Class<T> clazz) {
    	return applicationContext.getBean(clazz);
    }
}
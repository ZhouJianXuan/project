package com.sise.graduation.common.web.config;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private Map<String, HandlerInterceptor> handlerInterceptorMap;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        for (HandlerInterceptor interceptor: handlerInterceptorMap.values()){
            if (interceptor == null){
                continue;
            }
            registry.addInterceptor(interceptor);
        }
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 默认的DateFormatter不支持本格式，遂新增一个formatter
        registry.addFormatter(new Formatter<Date>() {
            private String format = "yyyy-MM-dd HH:mm:ss";
            @Override
            public Date parse(String text, Locale locale) throws ParseException {
                DateFormat dateFormat = new SimpleDateFormat(format, locale);
                return dateFormat.parse(text);
            }
            @Override
            public String print(Date object, Locale locale) {
                DateFormat dateFormat = new SimpleDateFormat(format, locale);
                return dateFormat.format(object);
            }
        });
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 标注着@Restful 和 @RequestBody都会默认使用MappingJackson2HttpMessageConverter构造器
        // 这个构造器要求较多，不如fastjson好用，所以这里都改成使用fastjon来做解析
        converters.clear();
        // 1. 处理字符串, 避免直接返回字符串的时候被添加了引号
        StringHttpMessageConverter smc = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converters.add(smc);
        // 2. 处理JSON
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(

                /*
                 * 结果格式化
                 */
                SerializerFeature.PrettyFormat,

                /*
                 * 输出key时使用双引号
                 */
                SerializerFeature.QuoteFieldNames,

                /*
                 * Date的日期转换器
                 */
                SerializerFeature.WriteDateUseDateFormat,

                /*
                 * 空字段处理
                 */
                SerializerFeature.WriteMapNullValue,

                SerializerFeature.WriteNullListAsEmpty,

                SerializerFeature.WriteNullStringAsEmpty
        );

        // 增加支持的media type
        List<MediaType> fastMediaTypes = new ArrayList<>(2);
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastMediaTypes.add(MediaType.APPLICATION_JSON);

        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        fastConverter.setFastJsonConfig(fastJsonConfig);

        // fastjson转换器
        converters.add(fastConverter);

        // 3. 增加byte数组，处理下载文件功能
        ByteArrayHttpMessageConverter bac = new ByteArrayHttpMessageConverter();
        converters.add(bac);
    }

    /**
     * 文件上传配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize("20MB");
		/*
		//设置总上传数据总大小
		factory.setMaxRequestSize("100MB");
		 */
        return factory.createMultipartConfig();
    }
}

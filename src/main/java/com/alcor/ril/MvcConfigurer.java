package com.alcor.ril;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author roamer - 徐泽宇
 * @create 2017-09-2017/9/22  上午10:32
 */
@Configuration
public class MvcConfigurer extends WebMvcConfigurerAdapter {

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/error").setViewName("error.html");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }


    /**
     * 以前要访问一个页面需要先创建个Controller控制类，再写方法跳转到页面
     * 在这里配置后就不需要那么麻烦了，直接访问http://localhost:8080/system_log/show就跳转到systemLogs/systemLoggerIndex.html页面了
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/system_log/show").setViewName("systemLogs/systemLoggerIndex");
        registry.addViewController("/").setViewName("/index");
        super.addViewControllers(registry);
    }
}

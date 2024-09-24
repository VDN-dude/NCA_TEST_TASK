package com.nca.config.web;

import com.nca.config.app.AppConfig;
import com.nca.config.web.filter.RequestLoggingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.InputStream;
import java.util.Map;

/**
 * {@code WebInitializer} intended to initialize context for servlets.
 */

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{AppConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        //        READING PROFILE PROPERTY AND SETTING IT INTO CONTEXT

        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("config/application.yaml");
        Map<String, Object> properties = yaml.load(inputStream);

        servletContext.setInitParameter("spring.profiles.active", String.valueOf(properties.get("profile")));
        servletContext.addFilter("requestLoggingFilter", new RequestLoggingFilter());
        super.onStartup(servletContext);
    }

}

package com.ijsp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;

/**
 * @author ijsp
 * @since 1.0
 */
@Configuration
public class MvcWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { SecurityConfig.class, DatabaseConfig.class, WebApplicationConfig.class  };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebApplicationConfig.class} ;
    }


    @Override
    protected String[] getServletMappings() {
        return new String[0];
    }

    @Override
    protected boolean isAsyncSupported() {
        return true;
    }
}

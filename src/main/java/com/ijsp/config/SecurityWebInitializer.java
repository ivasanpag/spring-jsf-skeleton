package com.ijsp.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * This class will be discovered by Spring and be used to register DelegatingFilterProxy
 * You can override appendFilters or insertFilters to rgister our own filters.
 * @author ijsp
 * @since
 */
public class SecurityWebInitializer extends AbstractSecurityWebApplicationInitializer {
}

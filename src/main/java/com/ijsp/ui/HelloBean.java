package com.ijsp.ui;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * We'll use JSF scope. SprinBeanAutowiringSupport helps to wire the Spring Data repositories classes
 * and use the Spring transactions
 */
@ViewScoped
@Named
@Data
@Log4j2
public class HelloBean extends SpringBeanAutowiringSupport implements Serializable {

    @PostConstruct
    public void init() {
        log.info("Init");
    }
}

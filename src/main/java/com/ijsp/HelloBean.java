package com.ijsp;

import lombok.extern.log4j.Log4j2;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named 
@RequestScoped
@Log4j2
public class HelloBean {

    @PostConstruct
    public void init() {

    }
}

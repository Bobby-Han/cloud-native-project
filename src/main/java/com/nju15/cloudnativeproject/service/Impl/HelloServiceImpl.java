package com.nju15.cloudnativeproject.service.Impl;

import com.nju15.cloudnativeproject.service.HelloService;
import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String greet() {
        return "Hello, We're NJU15 team!";
    }
}

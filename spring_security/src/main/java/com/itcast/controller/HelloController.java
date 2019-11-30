package com.itcast.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/11/30 16:20
 * @description:
 */
@RestController
@RequestMapping("hello")
public class HelloController {


@RequestMapping("add")
@PreAuthorize("hasAuthority('add')")
    public String add(){
    System.out.println("add...");
        return "add";
    }


    @RequestMapping("del")
    @PreAuthorize("hasAuthority('del')")
    public String del(){
        System.out.println("del...");
        return "del";
    }
}

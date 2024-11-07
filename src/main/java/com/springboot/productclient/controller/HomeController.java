package com.springboot.productclient.controller;

import com.springboot.productclient.model.Product;
import com.springboot.productclient.service.ProductClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/home")
public class HomeController {

    @Autowired
    private ProductClientService productClientService;

    @GetMapping("/product/{name}")
    public Product getProductById(@PathVariable String name){
        return productClientService.getProductByName(name);
    }
}

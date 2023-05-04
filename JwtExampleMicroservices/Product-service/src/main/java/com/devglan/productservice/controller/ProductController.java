package com.devglan.productservice.controller;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devglan.commons.DataStore;
import com.devglan.commons.Product;

@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping
    public List<Product> listProducts(){
        return DataStore.listProducts();
    }

    @GetMapping("/{id}")
    private Product getProductById(@PathVariable String id){
        return DataStore.listProducts().stream().filter(prd -> prd.getId().equalsIgnoreCase(id)).findFirst().get();
    }

//    @PostMapping
//    private Product getProductById(@RequestBody Product product){
//        product.setId("PRD " + RandomUtils.nextInt());
//        return product;
//    }

    @GetMapping("/customer/{custId}")
    public List<Product> listProductsByCustomerId(@PathVariable String custId){
        return DataStore.listProducts().stream().filter(product -> product.getCustomerId().equalsIgnoreCase(custId)).collect(Collectors.toList());
    }
}

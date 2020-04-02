package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Cart;
import org.csu.mypetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
    @RequestMapping("/cart")
public class CartController {
    @Autowired
    Cart cart;
    @Autowired
    private CatalogService catalogService;
//    @GetMapping("/addItemToCart")
//    public String addItemToCart(String workingItemId,){
//
//    }
}

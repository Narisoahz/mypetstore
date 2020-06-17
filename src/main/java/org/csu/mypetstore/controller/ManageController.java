package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.*;
import org.csu.mypetstore.service.AccountService;
import org.csu.mypetstore.service.CatalogService;
import org.csu.mypetstore.service.ManagerService;
import org.csu.mypetstore.tool.MD5keyBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/manage")
@SessionAttributes({"accountList","product"})
public class ManageController {
    @Autowired
    private ManagerService managerService;
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private AccountService accountService;
    private static final List<String> CATEGORY_LIST;
    private List<String> PRODUCTID_LIST;
    static {
        List<String> catList = new ArrayList<String>();
        catList.add("FISH");
        catList.add("DOGS");
        catList.add("REPTILES");
        catList.add("CATS");
        catList.add("BIRDS");
        CATEGORY_LIST = Collections.unmodifiableList(catList);
    }
    @GetMapping("/signonForm")
    public String signonForm(){
        return "manage/signon";
    }
    @GetMapping("/view")
    public String view(){
        return "manage/main";
    }
    @PostMapping("signon")
    public String signon(String username, String password,Model model){
        String md5Pwd="";
        MD5keyBean md5keyBean = new MD5keyBean();
        md5Pwd=md5keyBean.getkeyBeanofStr(password);
        Manager manager= managerService.getManager(username,md5Pwd);
        if (manager == null) {
            String msg = "Invalid username or password.  Signon failed.";
            model.addAttribute("msg", msg);
            return "manage/signon";
        } else {
            model.addAttribute("manager", manager);
                manager.setPassword(null);
                return "manage/main";
        }
    }
    @GetMapping("/alluser")
    public String allUser(Model model){
        List<Account> accountList=accountService.getAllAcount();
        model.addAttribute("accountList", accountList);
        return "manage/allUser";
    }
    @GetMapping("/managestore")
    public String managestore(){
        return "manage/managestore";
    }
    @GetMapping("/category")
    public String manageCategory(){
        return "manage/category";
    }
    @GetMapping("/product")
    public String manageProduct(){
        return "manage/product";
    }
    @GetMapping("/item")
    public String manageItem(){ return "manage/item";}
    @PostMapping("/searchCategory")
    public String searchCategory(String keyword,Model model)
    {
        if(keyword == null || keyword.length() < 1){
            String msg = "Please enter a keyword to search for, then press the search button.";
            model.addAttribute("msg",msg);
            return "manage/category";
        }else {
            List<Category> categoryList = catalogService.searchCategoryList(keyword.toLowerCase());
            processCategoryDescription(categoryList);
            model.addAttribute("categoryList",categoryList);
            return "manage/searchCategory";
        }
    }
    @PostMapping("/searchItem")
    public String searchItem(String keyword,Model model){
        if(keyword == null || keyword.length() < 1){
            String msg = "Please enter a keyword to search for, then press the search button.";
            model.addAttribute("msg",msg);
            return "common/error";
        }else {
            List<Item> itemList = catalogService.searchItemList(keyword.toLowerCase());
            processItemProductDescription(itemList);
            model.addAttribute("itemList",itemList);
            return "manage/search_item";
        }
    }
    @PostMapping("/searchProducts")
    public String searchProducts(String keyword,Model model){
        if(keyword == null || keyword.length() < 1){
            String msg = "Please enter a keyword to search for, then press the search button.";
            model.addAttribute("msg",msg);
            return "common/error";
        }else {
            List<Product> productList = catalogService.searchProductList(keyword.toLowerCase());
            processProductDescription(productList);
            model.addAttribute("productList",productList);
            return "manage/search_products";
        }
    }
    /*
            解决Thymeleaf将数据库中的Product的描述(description属性)中的<image>标签解析成普通文本的问题。
            本方法在Product中添加了imageURL属性，相当于将product的描述信息分成两部分处理了。
            同样，界面上也是用了两个标签了，一个img标签和一个lable标签。
            此方法是快速解决上述问题的临时方案，更好的方法应是更改数据库结构，将图片信息和普通文字描述信息分为两个字段存储。
         */
    private void processProductDescription(Product product){
        String [] temp = product.getDescription().split("\"");
        product.setDescriptionImage(temp[1]);
        product.setDescriptionText(temp[2].substring(1));
    }
    private void processProductDescription(List<Product> productList){
        for(Product product : productList) {
            processProductDescription(product);
        }
    }
    private void processItemProductDescription(List<Item> itemList){
        for(Item item:itemList){
            processProductDescription(item.getProduct());
        }
    }
    private void processCategoryDescription(Category category){
        String [] temp = category.getDescription().split("\"");
        category.setDescriptionImage(temp[1]);
        category.setDescriptionText(temp[2].substring(1));
    }
    private void processCategoryDescription(List<Category> categoryList){
        for(Category category :categoryList) {
            processCategoryDescription(category);
        }
    }
    @GetMapping("/newItemForm")
    private  String newItemForm(Model model){
        PRODUCTID_LIST=catalogService.getAllProductId();
        model.addAttribute("newItem",new Item());
        model.addAttribute("PRODUCTID_LIST", PRODUCTID_LIST);
            return "/manage/new_item";
    }
    @GetMapping("/newProductForm")
    private String newProductForm(Model model){
        model.addAttribute("newProduct",new Product());
        model.addAttribute("CATEGORY_LIST", CATEGORY_LIST);
        return "/manage/new_product";
    }
    @PostMapping("/newProduct")
    public String newProduct(Product product,Model model) {
        if (product.getProductId().length()== 0 || product.getName().length()== 0 || product.getDescription() .length()== 0) {
            String msg = "信息不能为空";
            model.addAttribute("msg", msg);
            return "manage/new_product";
        } else {
            catalogService.insertProduct(product);
            boolean authenticated = false;
            model.addAttribute("authenticated", authenticated);
            return "/manage/product";
        }
    }
    @GetMapping("/allProduct")
    public String allProduct(Model model){
        List<Product> productList=catalogService.getAllProduct();
        model.addAttribute("productList", productList);
        return "manage/allProduct";
    }
    @GetMapping("/editProductForm")
    public String editProductForm(String productId,Model model){
        Product product=catalogService.getProduct(productId);
        model.addAttribute("product",product);
        model.addAttribute("CATEGORY_LIST", CATEGORY_LIST);
        return "manage/edit_product";
    }
    @PostMapping("/editProduct_manage")
    public String editProduct_manage(Product product,Model model){
        System.out.println(product.getProductId());
        catalogService.updateProduct(product);
        boolean authenticated = false;
        List<Product> productList=catalogService.getAllProduct();//更新信息
        model.addAttribute("productList", productList);
        model.addAttribute("authenticated", authenticated);
        return "/manage/allProduct";
    }
    @GetMapping("/deleteProduct")
    public String deleteProduct(String productId,Model model){
        catalogService.deleteProduct(productId);
        boolean authenticated = false;
        List<Product> productList=catalogService.getAllProduct();//更新信息
        model.addAttribute("productList", productList);
        model.addAttribute("authenticated", authenticated);
        return "/manage/allProduct";
    }
}

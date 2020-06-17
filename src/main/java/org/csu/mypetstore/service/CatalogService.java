package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Category;
import org.csu.mypetstore.domain.Item;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.persistence.CategoryMapper;
import org.csu.mypetstore.persistence.ItemMapper;
import org.csu.mypetstore.persistence.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CatalogService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ItemMapper itemMapper;

    public List<Category> getCategoryList() {
        return categoryMapper.getCategoryList();
    }

    public Category getCategory(String categoryId) {
        return categoryMapper.getCategory(categoryId);
    }

    public Product getProduct(String productId) {
        return productMapper.getProduct(productId);
    }
    public List<String> getAllProductId(){return productMapper.getAllProductId();}
    public List<Product> getProductListByCategory(String categoryId) {
        return productMapper.getProductListByCategory(categoryId);
    }
    public List<Category> searchCategoryList(String keyword){
        return categoryMapper.searchCategoryList("%" + keyword.toLowerCase() + "%");
    }
    public List<Product> searchProductList(String keyword) {
        return productMapper.searchProductList("%" + keyword.toLowerCase() + "%");
    }
    public List<Item> searchItemList(String keyword){
        return itemMapper.searchItemList("%" + keyword.toLowerCase() + "%");
    }
    public void insertProduct(Product product){
        productMapper.insertProduct(product);
    }
    public List<Item> getItemListByProduct(String productId){
        return itemMapper.getItemListByProduct(productId);
    }

    public Item getItem(String itemId){
        return itemMapper.getItem(itemId);
    }

    public boolean isItemInStock(String itemId){
        return itemMapper.getInventoryQuantity(itemId) > 0;
    }
   public void updateProduct(Product product){ productMapper.updateProduct(product);}
    public void updateInventoryQuantity(Map<String, Object> param){
        itemMapper.updateInventoryQuantity(param);
    }
    public List<Product> getAllProduct(){
        return productMapper.getAllProduct();
    }
    public void deleteProduct(String productId){productMapper.deleteProduct(productId);}
}

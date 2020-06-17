package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMapper {
    List<Product> getProductListByCategory(String categoryId);

    Product getProduct(String productId);
    void updateProduct(Product product);
    List<Product> searchProductList(String keywords);
   void insertProduct(Product product);
    List<Product> getAllProduct();
    void deleteProduct(String productId);
    List<String> getAllProductId();
}

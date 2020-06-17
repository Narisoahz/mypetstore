package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface ItemMapper {
    void updateInventoryQuantity(Map<String,Object> param);

    int getInventoryQuantity(String itemId);
    void insertItem(Item item);
    void insertInventory(Item item);
    List<Item> getItemListByProduct(String productId);
    List<Item> searchItemList(String keyword);
    List<Item> getAllItem();
    void updateItem(Item item);
    Item getItem(String itemId);
    Item getItem1(String itemId);
    void onSaleItem(String itemId);
    void outSaleItem(String itemId);
    void updateQty(String itemId,int quantity);
}

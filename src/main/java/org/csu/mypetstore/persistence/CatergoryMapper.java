package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CatergoryMapper {
    List<Category> getCategoryList();

    Category getCategory(String categoryId);
}

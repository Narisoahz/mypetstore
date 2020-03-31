package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Category;
import org.csu.mypetstore.persistence.CatergoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {
   @Autowired
    private CatergoryMapper catergoryMapper;
   public Category getCategory(String categoryId){
       return catergoryMapper.getCategory(categoryId);
   }
}

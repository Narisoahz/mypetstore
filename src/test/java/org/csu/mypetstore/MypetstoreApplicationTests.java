package org.csu.mypetstore;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Category;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.service.AccountService;
import org.csu.mypetstore.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@MapperScan("org.csu.mypetstore.persistence")
class MypetstoreApplicationTests {
    @Autowired
    private AccountService accountService;
    @Test
    void contextLoads() {
    }

@Test
    void testAccount(){
    Account account=accountService.getAccount("1235");
    System.out.println(account.getUsername()+","+account.getPassword()+","+account.getBannerName());
}


}

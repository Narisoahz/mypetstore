package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Manager;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerMapper {
    Manager getManagerByUsernameAndPassword(Manager manager);
}

package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Manager;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.service.AccountService;
import org.csu.mypetstore.service.ManagerService;
import org.csu.mypetstore.tool.MD5keyBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@RequestMapping("/manage")
@SessionAttributes({"accountList"})
public class ManageController {
    @Autowired
    private ManagerService managerService;
    @Autowired
    private AccountService accountService;
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
}

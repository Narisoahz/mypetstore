package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Manager;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.service.ManagerService;
import org.csu.mypetstore.tool.MD5keyBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/manage")
public class ManageController {
    @Autowired
    private ManagerService managerService;

    @GetMapping("/signonForm")
    public String signonForm(){
        return "manage/signon";
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
                manager.setPassword(null);
                return "manage/main";
        }
    }
}

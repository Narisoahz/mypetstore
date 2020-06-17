package org.csu.mypetstore.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.service.AccountService;
import org.csu.mypetstore.service.CatalogService;
import org.csu.mypetstore.tool.MD5keyBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/account")
@SessionAttributes({"account", "myList", "authenticated","accountList"})
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private CatalogService catalogService;

    private static final List<String> LANGUAGE_LIST;
    private static final List<String> CATEGORY_LIST;
    static {
        List<String> langList = new ArrayList<String>();
        langList.add("ENGLISH");
        langList.add("CHINESE");
        LANGUAGE_LIST = Collections.unmodifiableList(langList);

        List<String> catList = new ArrayList<String>();
        catList.add("FISH");
        catList.add("DOGS");
        catList.add("REPTILES");
        catList.add("CATS");
        catList.add("BIRDS");

        CATEGORY_LIST = Collections.unmodifiableList(catList);
    }
//打开登录界面
    @GetMapping("/signonForm")
public String signonForm(){
    return "account/signon";
}
    @PostMapping("signon")
    public String signon(String username, String password,String msgcode, Model model,HttpSession session){
        String md5Pwd="";
        MD5keyBean md5keyBean = new MD5keyBean();
        md5Pwd=md5keyBean.getkeyBeanofStr(password);
        Account account = accountService.getAccount(username, md5Pwd);
        if (account == null) {
            String msg = "Invalid username or password.  Signon failed.";
            model.addAttribute("msg", msg);
            return "account/signon";
        } else {
            if(session.getAttribute("verifyCodeCreateTime") !=null && (System.currentTimeMillis()-Long.valueOf(String.valueOf(session.getAttribute("verifyCodeCreateTime"))) )> 1000 * 60  ){
                session.removeAttribute("verifyCode");
                session.removeAttribute("verifyCodeCreateTime");
                String msg1="验证码过期";
                model.addAttribute("msg1", msg1);
                return "account/signon";
            }
            else if (session.getAttribute("verifyCode") != null &&
                    msgcode.equals(session.getAttribute("verifyCode"))){
                account.setPassword(null);
                List<Product> myList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
                boolean authenticated = true;
                model.addAttribute("account", account);
                model.addAttribute("myList", myList);
                model.addAttribute("authenticated", authenticated);
                session.removeAttribute("verifyCode");
                session.removeAttribute("verifyCodeCreateTime");
                return "catalog/main";
            }else{
                String msg1="验证码错误";
                model.addAttribute("msg1", msg1);
                return "account/signon";
            }

        }
    }
    @GetMapping("/signoff")
    public String signoff(Model model) {
        Account loginAccount = new Account();
        List<Product> myList = null;
        boolean authenticated = false;
        model.addAttribute("account", loginAccount);
        model.addAttribute("myList", myList);
        model.addAttribute("authenticated", authenticated);
        return "catalog/main";
    }
    @GetMapping("/newAccountForm")
    public String newAccountForm(Model model){
        model.addAttribute("newAccount",new Account());
        model.addAttribute("LANGUAGE_LIST", LANGUAGE_LIST);
        model.addAttribute("CATEGORY_LIST", CATEGORY_LIST);
        return "account/new_account";
    }

    @PostMapping("/newAccount")
    public String newAccount(Account account,String repeatedPassword,Model model){
        String md5Pwd="";
        if (account.getPassword() == null || account.getPassword().length() == 0 || repeatedPassword == null || repeatedPassword.length() == 0) {
            String msg = "密码不能为空";
            model.addAttribute("msg", msg);
            return "account/new_account";
        } else if (!account.getPassword().equals(repeatedPassword)) {
            String msg = "两次密码不一致";
            model.addAttribute("msg", msg);
            return "account/new_account";
        } else {
            MD5keyBean md5keyBean = new MD5keyBean();
            md5Pwd=md5keyBean.getkeyBeanofStr(account.getPassword());
            account.setPassword(md5Pwd);
            accountService.insertAccount(account);
            Account loginAccount = new Account();
            List<Product> myList = null;
            boolean authenticated = false;
            model.addAttribute("account", loginAccount);
            model.addAttribute("myList", myList);
            model.addAttribute("authenticated", authenticated);
            return "redirect:/account/signonForm";
        }
    }
    //my account中打开信息界面
    @GetMapping("/editAccountForm")
    public String editAccountForm(@SessionAttribute("account") Account account , Model model){
        model.addAttribute("account", account);
        model.addAttribute("LANGUAGE_LIST", LANGUAGE_LIST);
        model.addAttribute("CATEGORY_LIST", CATEGORY_LIST);
        return "account/edit_account";
    }
    //管理系统中打开信息界面
    @GetMapping("/manageAccount")
    public String manageAccount(String username,Model model){
        Account account=accountService.getAccount(username);
        model.addAttribute("account", account);
        model.addAttribute("LANGUAGE_LIST", LANGUAGE_LIST);
        model.addAttribute("CATEGORY_LIST", CATEGORY_LIST);
        return "manage/edit_account";
    }
    @GetMapping("/resetPassword")
    public String resetPassword(String username,Model model){
        Account account=accountService.getAccount(username);
        String md5Pwd="";
        MD5keyBean md5keyBean = new MD5keyBean();
        md5Pwd=md5keyBean.getkeyBeanofStr("123");
        account.setPassword(md5Pwd);
        accountService.resetPassword(account);
        boolean authenticated = false;
        List<Account> accountList=accountService.getAllAcount();//更新信息
        model.addAttribute("accountList", accountList);
        model.addAttribute("authenticated", authenticated);
        return "manage/allUser";
    }
    //管理系统中提交信息
    @PostMapping("/editAccount_manage")
    public String editAccount_manage(Account account,Model model){
            accountService.updateAccountManage(account);
        boolean authenticated = false;
        List<Account> accountList=accountService.getAllAcount();//更新信息
       model.addAttribute("accountList", accountList);
        model.addAttribute("authenticated", authenticated);
            return "/manage/allUser";
        }
    //my account中提交信息
    @PostMapping("/editAccount")
    public String editAccount(Account account,String repeatedPassword,Model model){
        String md5Pwd="";
        if (account.getPassword() == null || account.getPassword().length() == 0 || repeatedPassword == null || repeatedPassword.length() == 0) {
            String msg = "密码不能为空";
            model.addAttribute("msg", msg);
            return "account/edit_account";
        } else if (!account.getPassword().equals(repeatedPassword)) {
            String msg = "两次密码不一致";
            model.addAttribute("msg", msg);
            return "account/edit_account";
        } else {
            MD5keyBean md5keyBean = new MD5keyBean();
            md5Pwd=md5keyBean.getkeyBeanofStr(account.getPassword());
            account.setPassword(md5Pwd);
            accountService.updateAccount(account);
            account = accountService.getAccount(account.getUsername());
            List<Product> myList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
            boolean authenticated = true;
            model.addAttribute("account", account);
            model.addAttribute("myList", myList);
            model.addAttribute("authenticated", authenticated);
            return "redirect:/catalog/view";
        }
    }
    //发送验证码按钮
    @RequestMapping(value = "/sendSms")
    @ResponseBody
    public Object sendSMS(@RequestParam("phone") String phone, HttpServletRequest httpServletRequest){
       // System.out.println(phone);
        try {
            //生成6位验证码
            String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
            //设置超时时间(不必修改)
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            //(不必修改)
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            //初始化ascClient，("***"分别填写自己的AccessKey ID和Secret)
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FxKTEFzFB3EG9E2fKss", "Gb7kB6BPh1d9MnaikUgaUgbnONFaTl");
            //(不必修改)
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
            //(不必修改)
            IAcsClient acsClient = new DefaultAcsClient(profile);
            //组装请求对象(不必修改)
            SendSmsRequest request = new SendSmsRequest();
            //****处填写接收方的手机号码
            request.setPhoneNumbers(phone);
            //****填写已申请的短信签名
            request.setSignName("lq的宠物商店");
            //****填写获得的短信模版CODE
            request.setTemplateCode("SMS_187260805");
            //笔者的短信模版中有${code}, 因此此处对应填写验证码
            request.setTemplateParam("{\"code\":\""+verifyCode+"\"}");
            //不必修改
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

            //将生成的验证码和创建时间放到session中，后面验证从session中取
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("verifyCode",verifyCode);
            session.setAttribute("verifyCodeCreateTime",System.currentTimeMillis());
//			return sendSmsResponse;
            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }

    }
}

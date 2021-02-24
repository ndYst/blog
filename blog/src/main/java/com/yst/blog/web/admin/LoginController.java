package com.yst.blog.web.admin;

import com.yst.blog.polo.User;
import com.yst.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")     //前端请求地址 可与下面方法中的地址进行拼接。前端action中一定要指明post get方法
public class LoginController {

    @Autowired
    private UserService userService; //接口 但是能调用实现类的方法

    @GetMapping  //@GetMapping(""),若括号内为空则默认使用最顶部的全局 get请求用以下方法处理
    public String loginPage(){
        return "admin/login";
    }

    @PostMapping("/login") //@RequestParam Post提交的参数得加上
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpSession session, RedirectAttributes attributes, Model model){
        User user = userService.checkUser(username, password);
        if (user!=null){
            //前台尽量隐藏密码
            user.setPassword(null);
            session.setAttribute("user",user);
            model.addAttribute("user",user);
            return "admin/index";
        } else{
            //添加错误提示信息
            attributes.addFlashAttribute("message","用户名或密码错误");
            //model.addAttribute("",""); 由于是重定向 前端页面拿不到
            return "redirect:/admin";    //默认找全局第一个方法。return "admin/login"不对;
        }
    }


    @GetMapping("/logout") //@GetMapping("/logout")可以和方法名不一样
    public String logout(HttpSession session){ //String为大写
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}

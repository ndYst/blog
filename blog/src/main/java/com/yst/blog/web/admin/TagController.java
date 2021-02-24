package com.yst.blog.web.admin;

import com.yst.blog.polo.Tag;
import com.yst.blog.polo.Type;
import com.yst.blog.service.TagService;
import com.yst.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")    //后端全局都/admin
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    //Sort.Direction.DESC与源码不同，可能出错。size=5:一页有5条数据，按id排序
    public String tags(@PageableDefault(size=5, sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable, Model model){      //Spring Boot根据前端页面自动封装Pageable
        model.addAttribute("page",tagService.listTag(pageable));
        return "admin/tags";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model){ //@PathVariable声明后才与GetMapping中的id所对应
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tags-input";
    }

    @GetMapping("/tags/input")
    public String input(Model model){
        model.addAttribute("tag",new Tag());
        return "admin/tags-input";
    }

    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){ //根据types-input页面中<input type="text" name="name" placeholder="分类名称">的name属性自动封装type
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1!=null) //BindingResult result可以自定义错误信息
            result.rejectValue("name","nameError","不能添加重复的标签");
        if (result.hasErrors())
            return "admin/tags-input";
        Tag t = tagService.saveTag(tag);
        if (t==null){
            attributes.addFlashAttribute("message","操作失败");
        }else{
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes){ //根据types-input页面中<input type="text" name="name" placeholder="分类名称">的name属性自动封装type
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1!=null) //BindingResult result可以自定义错误信息
            result.rejectValue("name","nameError","不能添加重复的标签");
        if (result.hasErrors())
            return "admin/tags-input";
        Tag t = tagService.updateTag(id, tag);
        if (t==null){
            attributes.addFlashAttribute("message","更新失败");
        }else{
            attributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/tags";
    }
}

package com.yst.blog.web.admin;

import com.yst.blog.polo.Blog;
import com.yst.blog.polo.Tag;
import com.yst.blog.polo.User;
import com.yst.blog.service.BlogService;
import com.yst.blog.service.TagService;
import com.yst.blog.service.TypeService;
import com.yst.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 2, sort = {"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                        BlogQuery blog, Model model){
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return LIST; //   /admin/blogs与admin/blogs访问的地址不一样
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 2, sort = {"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                        BlogQuery blog, Model model){
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList"; //返回admin/blogs下的blogList片段
    }

    @GetMapping("/blogs/input")
    public String input(Model model){ //新增？初始化博客 分类 标签
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog()); //和修改共用一个页面时不报错
        return INPUT;
    }

    private void setTypeAndTag(Model model){
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model){ //编辑？初始化博客 分类 标签
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        return INPUT;
    }

    //新增 编辑blog
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;
        if (blog.getId()==null)
            b = blogService.saveBlog(blog);
        else
            b = blogService.updateBlog(blog.getId(),blog);

        if (b==null){
            attributes.addFlashAttribute("message", "操作失败");
        }
        else{
            attributes.addFlashAttribute("message","操作成功");
        }
        return REDIRECT_LIST;
    }
 /*   //新增自定义标签?
        private void customTag(List list){
        Tag tag;
        System.out.println("----------------"+list.size());
        for (int i = 0; i < list.size(); i++) {
            tag = (Tag)list.get(i);
            if(tagService.getTag(tag.getId()) == null)
                tagService.saveTag(tag);
        }
    }*/

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }
}

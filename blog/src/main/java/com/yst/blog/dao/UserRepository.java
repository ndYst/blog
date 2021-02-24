package com.yst.blog.dao;

import com.yst.blog.polo.User;
import org.springframework.data.jpa.repository.JpaRepository;

//使用jpa User表示操作什么类型的数据，Long表示主键类型
public interface UserRepository extends JpaRepository<User,Long> {
    //符合命名规则就能调用方法？
    User findByUsernameAndPassword(String name, String password);
}

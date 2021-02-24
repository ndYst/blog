package com.yst.blog.dao;

import com.yst.blog.polo.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type,Long> {
    //咋调用的这是？
    Type findByName(String name);

    //from t_type对吗?
    @Query("select t from t_type t")
    List<Type> findTop(Pageable pageable);

}

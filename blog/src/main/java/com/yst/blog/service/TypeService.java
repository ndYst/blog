package com.yst.blog.service;

import com.yst.blog.polo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {
    Type saveType(Type type);
    Type getType(Long id);
    Page<Type> listType(Pageable pageable);
    public List<Type> listTypeTop(Integer size);
    Type updateType(Long id, Type type);
    void deleteType(Long id);
    Type getTypeByName(String name);
    List<Type> listType();
}

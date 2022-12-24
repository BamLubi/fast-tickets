package com.bytedance.fast_tickets.dao;

import com.bytedance.fast_tickets.entity.BaseEntity;

import java.util.List;

public interface BaseDao<E extends BaseEntity>{
    void insert(E e);
    void insertMany(List<E> list);
    void delete(E condition);
    void deleteById(String id);
    void update(E e);
    E find(E condition);
    E findById(String id);
    List<E> findList(E condition);
    List<E> findAll();
}

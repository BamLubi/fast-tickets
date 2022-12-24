package com.bytedance.fast_tickets.service;

import com.bytedance.fast_tickets.dao.BaseDao;
import com.bytedance.fast_tickets.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class BaseService <D extends BaseDao<E>, E extends BaseEntity>{
    @Autowired
    protected D dao;

    public void insertMany(List<E> list) {
        list.forEach(m -> m.preInsert());
        dao.insertMany(list);
    }

    public void delete(E condition) {
        dao.delete(condition);
    }

    public void deleteById(String id) {
        dao.deleteById(id);
    }

    public E find(E condition) {
        return dao.find(condition);
    }

    public E findById(String id) {
        return dao.findById(id);
    }

    public List<E> findList(E condition) {
        return dao.findList(condition);
    }

    public List<E> findAll() {
        return dao.findAll();
    }

    public void save(E e) {
        if (e.getId() == null || "".equals(e.getId())) {
            //插入
            e.preInsert();
            dao.insert(e);
        } else {
            //更新
            e.preUpdate();
            dao.update(e);
        }
    }
}

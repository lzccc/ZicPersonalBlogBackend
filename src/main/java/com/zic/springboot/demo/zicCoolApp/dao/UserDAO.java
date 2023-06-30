package com.zic.springboot.demo.zicCoolApp.dao;

import com.zic.springboot.demo.zicCoolApp.entity.User;

import java.util.List;

public interface UserDAO {
    void save(User user);

    User findById(Integer id);

    List<User> findAll();

    List<User> findByLastName(String lastName);

    void update(User user);

    void delete(Integer id);

    int deleteAll();
}

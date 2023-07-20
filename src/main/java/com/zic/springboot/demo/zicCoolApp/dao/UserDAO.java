package com.zic.springboot.demo.zicCoolApp.dao;

import com.zic.springboot.demo.zicCoolApp.entity.User;

import java.util.List;


//Now to access Data, we have to define a DAP interface as well as it's
//Impl class. We can make this simpler by using Spring Data JPA.
public interface UserDAO {
    void save(User user);

    User findById(Integer id);

    List<User> findAll();

    List<User> findByLastName(String lastName);

    void update(User user);

    void delete(Integer id);

    int deleteAll();
}

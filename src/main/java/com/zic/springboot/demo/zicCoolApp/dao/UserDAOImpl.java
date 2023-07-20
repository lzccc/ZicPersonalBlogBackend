package com.zic.springboot.demo.zicCoolApp.dao;

import com.zic.springboot.demo.zicCoolApp.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//Comment out this since we don't have a mysql yet
//@Repository
public class UserDAOImpl implements UserDAO {
    private EntityManager entityManager;

    @Autowired
    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public User findById(Integer id) {
        return entityManager.find(User.class, id);
    }

    //JPQL synax: The User and lastName here is the name of the Entity class and fields, not the names in DB.
    @Override
    public List<User> findAll() {
        TypedQuery<User> theQuery = entityManager.createQuery("select a from User a order by lastName", User.class);
        return theQuery.getResultList();
    }

    //the :theData allows as to fill in the value later
    @Override
    public List<User> findByLastName(String lastName) {
        TypedQuery<User> theQuery = entityManager.createQuery("select o from User o where lastName=:theData", User.class);
        theQuery.setParameter("theData", lastName);
        return theQuery.getResultList();
    }

    @Override
    @Transactional
    public void update(User user) {
        entityManager.merge(user);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
    }

    @Override
    @Transactional
    public int deleteAll() {
        TypedQuery<User> query = entityManager.createQuery("delete * from User *", User.class);
        return query.executeUpdate();
    }

}

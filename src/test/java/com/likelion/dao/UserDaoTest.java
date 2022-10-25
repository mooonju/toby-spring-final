package com.likelion.dao;

import com.likelion.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

class UserDaoTest {


    @Test
    @DisplayName("insert select test")
    void addAndGet() throws SQLException, ClassNotFoundException {
        UserDao userDao = new UserDaoFactory().awsUserDao();
        String id = "10";
        userDao.add(new User(id, "Roro", "1312"));
        User user = userDao.findById(id);
        assertEquals("Roro", user.getName());
        assertEquals("1312", user.getPassword());
    }

}
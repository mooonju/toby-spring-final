package com.likelion.dao;

import com.likelion.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserDaoFactory.class)
class UserDaoTest {

    @Autowired
    ApplicationContext context;


    @Test
    @DisplayName("insert select test")
    void addAndGet() throws SQLException, ClassNotFoundException {
        UserDao userDao = context.getBean("awsUserDao", UserDao.class);
        userDao.deleteAll();
        assertEquals(0, userDao.getCount());

        String id = "11";
        userDao.add(new User(id, "Roro", "1312"));
        assertEquals(1, userDao.getCount());
        User user = userDao.findById(id);

        assertEquals("Roro", user.getName());
        assertEquals("1312", user.getPassword());
    }

    @Test
    @DisplayName("count")
    void count() throws SQLException, ClassNotFoundException {
        User user1 = new User("1", "AAA", "1111");
        User user2 = new User("2", "BBB", "2222");
        User user3 = new User("3", "CCC", "3333");
        UserDao userDao = context.getBean("awsUserDao", UserDao.class);

        userDao.deleteAll();
        assertEquals(0, userDao.getCount());

        userDao.add(user1);
        assertEquals(1, userDao.getCount());
        userDao.add(user2);
        assertEquals(2, userDao.getCount());
        userDao.add(user3);
        assertEquals(3, userDao.getCount());
    }

}
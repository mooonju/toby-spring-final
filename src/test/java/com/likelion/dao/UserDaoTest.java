package com.likelion.dao;

import com.likelion.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserDaoFactory.class)
class UserDaoTest {

    @Autowired
    ApplicationContext context;
    UserDao userDao;
    User user1;
    User user2;
    User user3;

    @BeforeEach
    void setUp() {
        this.userDao = context.getBean("awsUserDao", UserDao.class);
        user1 = new User("1", "AAA", "1111");
        user2 = new User("2", "BBB", "2222");
        user3 = new User("3", "CCC", "3333");
    }



    @Test
    @DisplayName("insert select test")
    void addAndGet() throws SQLException, ClassNotFoundException {
        User user1 = new User("4", "DDD", "4444");

        userDao.deleteAll();
        assertEquals(0, userDao.getCount());

        String id = "10";
        userDao.add(user1);
        assertEquals(1, userDao.getCount());
        User user = userDao.findById(user1.getId());

        assertEquals(user1.getName(), user.getName());
        assertEquals(user1.getPassword(), user.getPassword());
    }

    @Test
    @DisplayName("count")
    void count() throws SQLException, ClassNotFoundException {

        userDao.deleteAll();
        assertEquals(0, userDao.getCount());

        userDao.add(user1);
        assertEquals(1, userDao.getCount());
        userDao.add(user2);
        assertEquals(2, userDao.getCount());
        userDao.add(user3);
        assertEquals(3, userDao.getCount());
    }

    @Test
    @DisplayName("findById ????????????")
    void findById() {
        assertThrows(EmptyResultDataAccessException.class, ()->{
            userDao.findById("40");
        });
    }

    @Test
    @DisplayName("getAll")
    void getAll() throws SQLException {
        userDao.deleteAll();
        List<User> users = userDao.getAll();
        assertEquals(0, users.size());

        userDao.add(user1);
        userDao.add(user2);
        userDao.add(user3);
        users = userDao.getAll();
        assertEquals(3, users.size());

    }

}
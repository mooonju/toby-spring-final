package com.likelion.dao;

import com.likelion.domain.User;

import java.sql.*;
import java.util.Map;

public class UserDao {

    private ConnectionMaker connectionMaker;

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add() {
        Map<String, String> env = System.getenv();

        try {
            Connection c = connectionMaker.makeConnection();
            PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) " +
                    "values(?, ?, ?)");
            ps.setString(1, "01");
            ps.setString(2, "AAA");
            ps.setString(3, "password");

            ps.executeUpdate();

            ps.close();
            c.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public User findById(String id) throws ClassNotFoundException, SQLException {
        Map<String, String> env = System.getenv();

        Connection c = connectionMaker.makeConnection();
        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");

        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User(rs.getString("id"),
                rs.getString("name"), rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }

}

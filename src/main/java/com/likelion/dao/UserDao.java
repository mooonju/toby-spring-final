package com.likelion.dao;

import com.likelion.domain.User;

import java.sql.*;
import java.util.Map;

public class UserDao {

    private ConnectionMaker connectionMaker;

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public UserDao() {

    }

    public void add(User user) {
        Map<String, String> env = System.getenv();

        try {
            Connection c = connectionMaker.makeConnection();
            PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) " +
                    "values(?, ?, ?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

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

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao ud = new UserDao();
        ud.add(new User("4", "AAA", "1111"));
        ud.findById("4");
    }

}

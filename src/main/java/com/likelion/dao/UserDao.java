package com.likelion.dao;

import com.likelion.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.*;
import java.util.Map;

public class UserDao {

    private ConnectionMaker connectionMaker;

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public UserDao() {

    }

    public void deleteAll() throws SQLException, ClassNotFoundException {
        Connection c = connectionMaker.makeConnection();
        PreparedStatement ps = c.prepareStatement("delete from users");
        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public int getCount() throws SQLException, ClassNotFoundException {
        Connection c = connectionMaker.makeConnection();
        PreparedStatement ps = c.prepareStatement("select count(*) from users");
        ResultSet rs = ps.executeQuery();

        rs.next();
        int count = rs.getInt(1);
        return count;
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


        try {
            Connection c = connectionMaker.makeConnection();

            PreparedStatement ps = c.prepareStatement("select * from users where id = ?");

            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();
            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("id"),
                        rs.getString("name"), rs.getString("password"));
            }

            rs.close();
            ps.close();
            c.close();

            if (user == null) { throw new EmptyResultDataAccessException(1); }

            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

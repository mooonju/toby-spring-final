package com.likelion.dao;

import com.likelion.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.*;
import java.util.Map;

public class UserDao {

    private ConnectionMaker cm;

    public UserDao(ConnectionMaker connectionMaker) {
        this.cm = connectionMaker;
    }

    public UserDao() {

    }

    public void deleteAll() throws SQLException, ClassNotFoundException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = cm.makeConnection();
            ps = c.prepareStatement("delete from users");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public int getCount() throws SQLException {
        ResultSet rs = null;
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = cm.makeConnection();
            ps = c.prepareStatement("select count(*) from users");
            rs = ps.executeQuery();

            rs.next();

            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void add(User user) {
        Map<String, String> env = System.getenv();
        try {
            Connection c = cm.makeConnection();
            PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) " +
                    "values(?, ?, ?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();

            ps.close();
            c.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findById(String id) throws ClassNotFoundException, SQLException {
        Map<String, String> env = System.getenv();


        try {
            Connection c = cm.makeConnection();

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

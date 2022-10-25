package com.likelion.dao;

import com.likelion.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;

public class UserDao {

    private DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy st) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = st.makePreparedStatement(c);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
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

    public void deleteAll() throws SQLException {
        StatementStrategy deleteAllStrategy = new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement("delete from users");
            }
        };
        jdbcContextWithStatementStrategy(deleteAllStrategy);
    }

    public int getCount() throws SQLException {
        ResultSet rs = null;
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("select count(*) from users");
            rs = ps.executeQuery();

            rs.next();

            return rs.getInt(1);
        } catch (SQLException e) {
            throw e;
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

    public void add(User user) throws SQLException {
        AddStrategy addStrategy = new AddStrategy(user);
        jdbcContextWithStatementStrategy(addStrategy);
    }

    public User findById(String id) throws ClassNotFoundException, SQLException {
        Map<String, String> env = System.getenv();


        try {
            Connection c = dataSource.getConnection();

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

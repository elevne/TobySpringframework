package org.example.fifthchapter;

import org.example.sixthchapter.UserDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private RowMapper<User> userMapper =
            new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    user.setLevel(Level.valueOf(rs.getInt("level")));
                    user.setLogin(rs.getInt("login"));
                    user.setRecommend(rs.getInt("recommend"));
                    user.setEmail(rs.getString("email"));
                    return user;
                }
            };


    private JdbcTemplate jdbcTemplate;

    public DataSource dataSource;

    public UserDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    public void deleteAll() {
        this.jdbcTemplate.update(
                "DELETE FROM users"
        );
    }

    public void add(User user){
        this.jdbcTemplate.update(
                "INSERT INTO users(id, name, password, level, login, recommend, email) values (?,?,?,?,?,?,?)",
                user.getId(), user.getName(), user.getPassword(), user.getLevel().getValue(), user.getLogin(), user.getRecommend(), user.getEmail()
        );
    }

    public void update(User user){
        this.jdbcTemplate.update(
                "UPDATE users set name = ?, password = ?, level = ?, login = ?,"+
                        "recommend = ?, email = ? WHERE id = ?", user.getName(), user.getPassword(),
                user.getLevel().getValue(), user.getLogin(), user.getRecommend(), user.getEmail(), user.getId()
        );
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?",
                this.userMapper, new Object[] {id}
                );
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM users ORDER BY id",
                this.userMapper);
    }

    private void transactionTest() throws SQLException {
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        try {
            PreparedStatement st1 = conn.prepareStatement("UPDATE users SET id = 2 WHERE id = 1");
            st1.executeUpdate();
            PreparedStatement st2 = conn.prepareStatement("DELETE FROM users WHERE id = 2");
            st2.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
        }
        conn.close();
    }







//    private JdbcContext jdbcContext;
//
//    public UserDao(ConnectionMaker connectionMaker) {
//        this.jdbcContext = new JdbcContext(connectionMaker);
//    }
//
//    public void deleteAll() throws SQLException, ClassNotFoundException {
//        this.jdbcContext.workWithStatementStrategy(
//                new StatementStrategy() {
//                    @Override
//                    public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
//                        return connection.prepareStatement("DELETE FROM users");
//                    }
//                }
//        );
//    }
}

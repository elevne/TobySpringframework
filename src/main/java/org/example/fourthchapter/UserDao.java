package org.example.fourthchapter;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.example.firstchapter.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private RowMapper<User> userMapper =
            new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            };


    private JdbcTemplate jdbcTemplate;

    private DataSource dataSource;

    public UserDao(DataSource dataSource) {
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

    public void add(User user) throws DuplicateUserIdException {
//        try {
            this.jdbcTemplate.update(
                    "INSERT INTO users(id, name, password) values (?,?,?)",
                    user.getId(), user.getName(), user.getPassword()
            );
//        } catch (SQLException e) {
//            if (e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY){
//                throw new DuplicateUserIdException(e);
//            } else {
//                throw new RuntimeException(e);
//            }
//        }
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?",
                this.userMapper
                );
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM users ORDER BY id",
                this.userMapper
                );
    }
}

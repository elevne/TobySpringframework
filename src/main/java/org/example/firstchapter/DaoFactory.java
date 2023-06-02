package org.example.firstchapter;

import com.mysql.cj.jdbc.Driver;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.example.thirdchapter.JdbcContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

//@Configuration
//public class DaoFactory {
//
//    @Bean
//    public ConnectionMaker connectionMaker() {
//        return new NewConnectionMaker();
//    }
//
//    @Bean
//    public DataSource dataSource() throws ClassNotFoundException, SQLException {
//        DataSource ds = new SimpleDriverDataSource(
//                new com.mysql.cj.jdbc.Driver(),
//                "jdbc:mysql://localhost:3306/spring",
//                "root",
//                "nf6yxxzc"
//        );
//        return ds;
//    }
//
//    @Bean
//    public UserDao userDao() throws ClassNotFoundException, SQLException {
//        return new UserDao(dataSource());
//    }
//}

package org.example.firstchapter;

import org.example.thirdchapter.JdbcContext;
import org.example.thirdchapter.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {

    @Bean
    public ConnectionMaker connectionMaker() {
        return new NewConnectionMaker();
    }

    @Bean
    public UserDao userDao() {
        return new UserDao(connectionMaker());
    }
}

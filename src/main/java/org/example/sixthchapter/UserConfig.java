package org.example.sixthchapter;

import com.mysql.cj.jdbc.Driver;
import org.example.fifthchapter.MockMailSender;
import org.example.fifthchapter.UserDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class UserConfig {

    @Bean
    public DataSource dataSource() throws SQLException {
        DataSource ds = new SimpleDriverDataSource(
                new Driver(),
                "jdbc:mysql://localhost:3306/spring",
                "root",
                "nf6yxxzc"
        );
        return ds;
    }

    @Bean
    public UserDaoImpl userDao() throws SQLException {
        return new UserDaoImpl(dataSource());
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() throws SQLException {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public MailSender mockMailSender() {
        MockMailSender mailSender = new MockMailSender();
        return mailSender;
    }

    @Bean
    public UserService userServiceImpl() throws SQLException {
        return new UserServiceImpl(userDao(), mockMailSender());
    }

    @Bean
    public UserService userServiceTx() throws SQLException {
        return new UserServiceTx(platformTransactionManager(), userServiceImpl());
    }
}

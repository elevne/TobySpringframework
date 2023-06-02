package org.example.fifthchapter;

import com.mysql.cj.jdbc.Driver;
import org.example.firstchapter.ConnectionMaker;
import org.example.firstchapter.NewConnectionMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DaoFactory {

    @Bean
    public ConnectionMaker connectionMaker() {
        return new NewConnectionMaker();
    }

    @Bean
    public DataSource dataSource() throws ClassNotFoundException, SQLException {
        DataSource ds = new SimpleDriverDataSource(
                new Driver(),
                "jdbc:mysql://localhost:3306/spring",
                "root",
                "nf6yxxzc"
        );
        return ds;
    }

    @Bean
    public UserDaoImpl userDao() throws ClassNotFoundException, SQLException {
        return new UserDaoImpl(dataSource());
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(UserDaoImpl userDao) {
        return new DataSourceTransactionManager(userDao.dataSource);
    }

    @Bean
    public MailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("mail.server.com");
        return mailSender;
    }

    @Bean
    public MailSender dummyMailSender() {
        DummyMailSender mailSender = new DummyMailSender();
        return mailSender;
    }

    @Bean
    public MailSender mockMailSender() {
        MockMailSender mailSender = new MockMailSender();
        return mailSender;
    }

    @Bean
    public UserService userService() throws ClassNotFoundException, SQLException {
        return new UserService(userDao(), platformTransactionManager(userDao()), mockMailSender());
    }
}

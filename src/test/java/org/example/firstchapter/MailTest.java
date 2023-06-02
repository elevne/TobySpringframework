package org.example.firstchapter;

import org.example.fifthchapter.*;
import org.example.fifthchapter.User;
import org.example.fifthchapter.UserDaoImpl;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoFactory.class)
@DirtiesContext
public class MailTest {

    User user1;
    List<User> users;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDaoImpl userDao;

    @Autowired
    private MailSender mockMailSender;

    @Before
    public void setup() {
        userDao.deleteAll();
        this.user1 = new User("user1", "박인범", "spring1", Level.BASIC, 1, 0, "inspring@naver.com");
        this.users = Arrays.asList(
                new User("inbeom", "박인범", "p1", Level.BASIC, UserService.MIN_LOG_COUNT_FOR_SILVER-1, 0, ""),
                new User("inbemo2", "박인범2", "p2", Level.BASIC, UserService.MIN_LOG_COUNT_FOR_SILVER, 0, ""),
                new User("inbeom3", "박인범3", "p3", Level.SILVER, 60, UserService.MIN_RECOMMEND_FOR_GOLD-1, ""),
                new User("inbeom4", "박인범4", "p4", Level.SILVER, 60, UserService.MIN_RECOMMEND_FOR_GOLD, ""),
                new User("inbeom5", "박인범5", "p5", Level.GOLD, 100, 100, "")
        );
    }

//    @Test
//    public void 메일테스트첫번째() {
//        userService.sendUpgradeEmail(user1);
//    }

    @Test
    public void 목객체테스트() {
        for (User user : this.users) userDao.add(user);
        userService.upgradeLevels();
        MockMailSender mailSender = (MockMailSender) userService.getMailSender();
        List<String> requests = mailSender.getRequests();
        System.out.println(requests);
        Assert.assertThat(requests.size(), Is.is(2));
        Assert.assertThat(requests.get(0), Is.is(users.get(1).getEmail()));
        Assert.assertThat(requests.get(1), Is.is(users.get(3).getEmail()));
    }

}

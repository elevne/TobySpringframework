package org.example.aop;

import org.example.fifthchapter.Level;
import org.example.fifthchapter.MockMailSender;
import org.example.fifthchapter.User;
import org.example.fifthchapter.UserDaoImpl;
import org.example.sixthchapter.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UserConfig.class)
@DirtiesContext
public class AOPTest {

    @Autowired
    private UserService userServiceTx;

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private UserDaoImpl userDao;

    private User user1;
    private List<User> users;

    @Before
    public void setUp() {
        userDao.deleteAll();
        this.user1 = new User("user1", "박인범", "spring1", Level.BASIC, 1, 0, "inspring@naver.com");
        this.users = Arrays.asList(
                new User("inbeom", "박인범", "p1", Level.BASIC, org.example.fifthchapter.UserService.MIN_LOG_COUNT_FOR_SILVER-1, 0, ""),
                new User("inbemo2", "박인범2", "p2", Level.BASIC, org.example.fifthchapter.UserService.MIN_LOG_COUNT_FOR_SILVER, 0, ""),
                new User("inbeom3", "박인범3", "p3", Level.SILVER, 60, org.example.fifthchapter.UserService.MIN_RECOMMEND_FOR_GOLD-1, ""),
                new User("inbeom4", "박인범4", "p4", Level.SILVER, 60, org.example.fifthchapter.UserService.MIN_RECOMMEND_FOR_GOLD, ""),
                new User("inbeom5", "박인범5", "p5", Level.GOLD, 100, 100, "")
        );
    }

    @Test
    public void 업그레이드AOP테스트() throws Exception {
         try {
             for (User user : this.users) userDao.add(user);
             userServiceTx.upgradeLevels();
             checkLevelUpgraded(users.get(0), false);
             checkLevelUpgraded(users.get(1), true);
             checkLevelUpgraded(users.get(2), false);
             checkLevelUpgraded(users.get(3), true);
             checkLevelUpgraded(users.get(4), false);
         } catch (Exception e) {}
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdated = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdated.getLevel(), is(user.getLevel().next()));
        } else {
            assertThat(userUpdated.getLevel(), is(user.getLevel()));
        }
    }

    @Test
    public void 업그레이드테스트() throws Exception {
        MockUserDao userDao = new MockUserDao(this.users);
        MockMailSender mailSender = new MockMailSender();
        UserServiceImpl userService = new UserServiceImpl(userDao, mailSender);

        userService.upgradeLevels();
        List<User> updated = userDao.getUpdated();
        assertThat(updated.size(), is(2));
        checkUserAndLevel(updated.get(0), "inbemo2", Level.SILVER);
        checkUserAndLevel(updated.get(1), "inbeom4", Level.GOLD);

        List<String> request = mailSender.getRequests();
        assertThat(request.size(), is(2));
        assertThat(request.get(0), is(users.get(1).getEmail()));
        assertThat(request.get(1), is(users.get(3).getEmail()));
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertThat(updated.getId(), is(expectedId));
        assertThat(updated.getLevel(), is(expectedLevel));
    }

    @Test
    public void Mockito로테스트() throws Exception {

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        MailSender mockMailSender = mock(MailSender.class);

        UserServiceImpl userService = new UserServiceImpl(mockUserDao, mockMailSender);
        userService.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(users.get(1));
        assertThat(users.get(1).getLevel(), is(Level.SILVER));
        verify(mockUserDao).update(users.get(3));
        assertThat(users.get(3).getLevel(), is(Level.GOLD));

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
        assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
    }

}

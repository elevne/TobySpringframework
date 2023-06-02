package org.example.firstchapter;

import org.example.fifthchapter.*;
import org.example.fifthchapter.User;
import org.example.fifthchapter.UserDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoFactory.class)
@DirtiesContext
public class UserDaoTest {

    User user1;
    User user2;
    List<User> users;

    @Before
    public void setUp() {
        userDao.deleteAll();
        this.user1 = new User("user1", "박인범", "spring1", Level.BASIC, 1, 0, "");
        this.user2 = new User("user2", "최원일", "spring2", Level.GOLD, 1, 1, "");
        this.users = Arrays.asList(
                new User("inbeom", "박인범", "p1", Level.BASIC, UserService.MIN_LOG_COUNT_FOR_SILVER-1, 0, ""),
                new User("inbemo2", "박인범2", "p2", Level.BASIC, UserService.MIN_LOG_COUNT_FOR_SILVER, 0, ""),
                new User("inbeom3", "박인범3", "p3", Level.SILVER, 60, UserService.MIN_RECOMMEND_FOR_GOLD-1, ""),
                new User("inbeom4", "박인범4", "p4", Level.SILVER, 60, UserService.MIN_RECOMMEND_FOR_GOLD, ""),
                new User("inbeom5", "박인범5", "p5", Level.GOLD, 100, 100, "")
        );
    }

    @Autowired
    private UserDaoImpl userDao;

    @Autowired
    private UserService userService;


    @Test
    public void 예외발생시작업취소여부테스트() {
        UserService testUserService = new TestUserService(userDao, users.get(3).getId());
        userDao.deleteAll();
        for (User user : users) userDao.add(user);
        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException should have thrown");
        } catch (TestUserServiceException e) {

        }
        checkLevelUpgraded(users.get(1), false);
    }














    @Test
    public void bean() {
        assertThat(this.userService, is(notNullValue()));
    }

    @Test
    public void testAdd() {
        userDao.add(user1);
        userDao.add(user2);
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getLogin(), is(user2.getLogin()));
        assertThat(user1.getRecommend(), is(user2.getRecommend()));
    }

    @Test
    public void update() {

        userDao.deleteAll();
        userDao.add(user1);
        userDao.add(user2);

        user1.setName("박인범2");
        user1.setPassword("spring00");
        user1.setLevel(Level.SILVER);
        user1.setLogin(99);
        user1.setRecommend(99);

        userDao.update(user1);
        User user1Updated = userDao.get(user1.getId());
        checkSameUser(user1, user1Updated);
        User user2Same = userDao.get(user2.getId());
        checkSameUser(user2, user2Same);

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
    public void upgradeLevels() {
        userDao.deleteAll();
        for (User user : users) userDao.add(user);
        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

}

package org.example.firstchapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoFactory.class)
@DirtiesContext
public class UserDaoTest {


    @Autowired
    private UserDao userDao;


    @Before
    public void setUp() {
        ConnectionMaker connectionMaker = new TestConnectionMaker();
        userDao.setConnectionMaker(connectionMaker);
    }


    @Test(expected = SQLException.class)
    public void getUserInstance() throws SQLException, ClassNotFoundException {
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));
        userDao.get("TEST_FAIL");
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        User user = new User("TestID", "TestName", "pwd");
        userDao.add(user);
        assertThat(userDao.getCount(), is(1));

        User user2 = userDao.get(user.getId());
        assertThat(user2.getName(), is(user.getName()));
        assertThat(user2.getPassword(), is(user.getPassword()));
    }

}

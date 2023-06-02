package org.example.sixthchapter;

import org.example.fifthchapter.Level;
import org.example.fifthchapter.User;
import org.example.fifthchapter.UserDaoImpl;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

public class UserServiceImpl implements UserService {
    UserDao userDao;
    MailSender mailSender;
    public UserServiceImpl(UserDao userDao, MailSender mailSender) {
        this.userDao = userDao;
        this.mailSender = mailSender;
    }
    public static final int MIN_LOG_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (canUpgradeLevel(user)){
                upgradeLevel(user);
            }
        }
    }

    public void add(User user) {}





    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOG_COUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown level: " + currentLevel);
        }
    }
    protected void upgradeLevel(User user) {
        Level nextLevel = user.level.next();
        if (nextLevel == null) {
            throw new IllegalArgumentException(user.level + " is not upgradeable");
        }
        user.level = nextLevel;
        userDao.update(user);
        sendUpgradeEmail(user);
    }

    public void sendUpgradeEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("UPGRADE 안내");
        mailMessage.setText("사용자의 등급이 "+user.getLevel().name());
        this.mailSender.send(mailMessage);
    }

    public MailSender getMailSender() {
        return this.mailSender;
    }

    public void setUserDao(UserDaoImpl userDao) {
        this.userDao = userDao;
    }
}

package org.example.fifthchapter;


import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

public class UserService {

    UserDaoImpl userDao;


    public static final int MIN_LOG_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private PlatformTransactionManager transactionManager;

    public UserService(UserDaoImpl userDao, PlatformTransactionManager transactionManager, MailSender mailSender) {
        this.userDao = userDao;
        this.transactionManager = transactionManager;
        this.mailSender = mailSender;
    }

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

    public void upgradeLevels() {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            upgradeLevelsInternal();
            this.transactionManager.commit(status);
        } catch (RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }
    // 본래 upgradeLevels 안에 있던 것 다른 메서드로 분리
    private void upgradeLevelsInternal(){
        List<User> users = userDao.getAll();
        for (User user: users){
            if (canUpgradeLevel(user)){
                upgradeLevel(user);
            }
        }
    }










    public void sendEmail(User user) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "mail.ksug.org");
        Session s = Session.getInstance(properties, null);
        MimeMessage message = new MimeMessage(s);
        try {
            message.setFrom(new InternetAddress("useradmin@ksug.org"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("UPGRADE 안내");
            message.setText("고객님의 등급이 "+user.getLevel().name()+"로 업그레이드 되었습니다.");
            Transport.send(message);
        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        //catch (UnsupportedEncodingException e) {
        //    throw new RuntimeException(e);
        //}
    }

    private MailSender mailSender;

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

}

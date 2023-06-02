package org.example.sixthchapter;

import org.example.fifthchapter.User;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class UserServiceTx implements UserService {

    UserService userService;
    PlatformTransactionManager transactionManager;
    public UserServiceTx(PlatformTransactionManager transactionManager, UserService userService){
        this.transactionManager = transactionManager;
        this.userService = userService;
    }

    public void UserService(UserService userService) {
        this.userService = userService;
    }

    public void add(User user) {userService.add(user);}
    public void upgradeLevels() {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            userService.upgradeLevels();
            this.transactionManager.commit(status);
        } catch (Exception e) {
            this.transactionManager.rollback(status);
            throw e;
        }

    }
}

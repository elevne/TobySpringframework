package org.example.fifthchapter;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;
import java.util.List;

public class MockMailSender implements MailSender {

    private List<String> requests = new ArrayList<>();

    public List<String> getRequests() {
        return this.requests;
    }

    public void send(SimpleMailMessage message) throws MailException {
        requests.add(message.getTo()[0]);
    }

    public void send(SimpleMailMessage[] messages) throws MailException {}
}

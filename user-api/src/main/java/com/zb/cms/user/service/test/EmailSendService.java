package com.zb.cms.user.service.test;

import com.zb.cms.user.client.MailgunClient;
import com.zb.cms.user.client.mailgun.SendMailForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSendService {
    private final MailgunClient mailgunClient;

    public String sendEmail(){

        SendMailForm form = SendMailForm.builder()
                .from("onlee1425@naver.com")
                .to("onlee1425@naver.com")
                .subject("Test email from zb cms")
                .text("Test text")
                .build();

        return mailgunClient.sendEmail(form).getBody();
    }
}

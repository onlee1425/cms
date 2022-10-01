package com.zb.cms.client.service;

import com.zb.cms.client.MailgunClient;
import com.zb.cms.client.mailgun.SendMailForm;
import feign.Response;
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

package top.rabbitbyte.nowblog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import top.rabbitbyte.nowblog.util.MailClient;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes =  NowblogApplication.class)
public class Mailtests {

    @Autowired
    private MailClient mailService;

    @Test
    public void testTextMail() {
         mailService.sendMail("2039152071@qq.com","TEST","TEST");
    }
    @Test
    public void testHtmlMail() {
        mailService.sendMailWithHTML("2039152071@qq.com","TEST","TEST");
    }
}

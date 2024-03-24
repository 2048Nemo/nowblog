package top.rabbitbyte.nowblog.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import top.rabbitbyte.nowblog.NowblogApplication;
import top.rabbitbyte.nowblog.entity.LoginTicket;

import java.sql.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes =  NowblogApplication.class)
public class loginTicketMapperTests {
    @Autowired
    LoginTicketMapper loginTicketMapper;

    @Test
    public void testTicketInsert(){
        LoginTicket ticket =  new LoginTicket();
        ticket.setTicket("123456");
        ticket.setStatus(0);
        ticket.setUserId(1);
        ticket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));

        int num  =   loginTicketMapper.insertLoginTicket(ticket);
        System.out.println("插入成功" + num);
    }
    @Test
    public void testTicketSelect(){
        LoginTicket ticket =  loginTicketMapper.selectByTicket("123456");
        System.out.println(ticket);

    }
    @Test
    public void testTicketUpdate(){

        int num  =   loginTicketMapper.updateStatus(1,"123456");
        System.out.println("更新成功" + num);

    }

    @Test
    public void testTicketDelete(){
    }

}

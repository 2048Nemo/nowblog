package top.rabbitbyte.nowblog.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import top.rabbitbyte.nowblog.NowblogApplication;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes =  NowblogApplication.class)
public class userMapperTests {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void getUserHeaderUrl(){
        System.out.println(userMapper.selectById(101));
    }
}

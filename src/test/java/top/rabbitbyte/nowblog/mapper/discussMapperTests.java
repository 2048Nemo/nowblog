package top.rabbitbyte.nowblog.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import top.rabbitbyte.nowblog.NowblogApplication;
import top.rabbitbyte.nowblog.entity.DiscussPost;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes =  NowblogApplication.class)
public class discussMapperTests {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public  void testselectDiscussPosts(){
        //System.out.println(discussPostMapper.selectDiscussPosts(0,0,10));
        List<DiscussPost> list =  discussPostMapper.selectDiscussPosts(0,0,10);
        for (DiscussPost post : list){
            System.out.println(post.getTitle());
            System.out.println(post);
        }
    }


}

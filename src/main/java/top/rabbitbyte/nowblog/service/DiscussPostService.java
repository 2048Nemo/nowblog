package top.rabbitbyte.nowblog.service;

import org.apache.ibatis.annotations.Param;
import top.rabbitbyte.nowblog.entity.DiscussPost;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【discuss_post】的数据库操作Service
* @createDate 2024-03-08 15:42:11
*/
public interface DiscussPostService extends IService<DiscussPost> {

    List<DiscussPost> findDiscussPosts(int userid, int offset, int limit);
    int selectDiscussPostRows(int userId);

    int updateCommentCount(int id, int commentCount);
}

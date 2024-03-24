package top.rabbitbyte.nowblog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.rabbitbyte.nowblog.entity.DiscussPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【discuss_post】的数据库操作Mapper
* @createDate 2024-03-08 15:42:11
* @Entity top.rabbitbyte.nowblog.entity.DiscussPost
*/
@Mapper
public interface DiscussPostMapper extends BaseMapper<DiscussPost> {
    List<DiscussPost> selectDiscussPosts(@Param("userId") int userId,@Param("offset") int offset,@Param("limit") int limit);
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(@Param("post") DiscussPost post);

    DiscussPost selectDiscussPostById(@Param("id") int id);

    int updateCommentCount(@Param("id") int id,@Param("commentCount") int commentCount);
}





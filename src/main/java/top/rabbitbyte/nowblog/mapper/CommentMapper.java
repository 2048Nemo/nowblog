package top.rabbitbyte.nowblog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.rabbitbyte.nowblog.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【comment】的数据库操作Mapper
* @createDate 2024-03-08 15:42:11
* @Entity top.rabbitbyte.nowblog.entity.Comment
*/
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    List<Comment> selectCommentsByEntity(@Param("entityType") int entityType,@Param("entityId") int entityId,@Param("offset") int offset,@Param("limit") int limit);

    int selectCountByEntity(@Param("entityType")int entityType,@Param("entityId") int entityId);

    int insertComment(@Param("comment") Comment comment);
}





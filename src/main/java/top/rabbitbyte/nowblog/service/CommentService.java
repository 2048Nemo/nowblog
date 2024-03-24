package top.rabbitbyte.nowblog.service;

import org.springframework.stereotype.Component;
import top.rabbitbyte.nowblog.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【comment】的数据库操作Service
* @createDate 2024-03-08 15:42:11
*/
@Component
public interface CommentService extends IService<Comment> {

}

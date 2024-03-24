package top.rabbitbyte.nowblog.service;

import top.rabbitbyte.nowblog.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【message】的数据库操作Service
* @createDate 2024-03-08 15:42:11
*/
public interface MessageService extends IService<Message> {

    int addMessage(Message message);

}

package top.rabbitbyte.nowblog.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.rabbitbyte.nowblog.entity.Comment;
import top.rabbitbyte.nowblog.entity.DiscussPost;
import top.rabbitbyte.nowblog.entity.Event;
import top.rabbitbyte.nowblog.event.EventProducer;
import top.rabbitbyte.nowblog.service.impl.CommentServiceImpl;
import top.rabbitbyte.nowblog.service.impl.DiscussPostServiceImpl;
import top.rabbitbyte.nowblog.util.CommunityConstant;
import top.rabbitbyte.nowblog.util.HostHolder;

import java.util.Date;

@Controller
@RequestMapping(path = "/comment")
public class CommentController  implements CommunityConstant {
    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private DiscussPostServiceImpl discussPostService;

    @RequestMapping(path = "/add/{discussPostId}",method = RequestMethod.POST)
    public  String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment){
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setTargetId(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        //出发comment事件
        Event event  = new Event();
        event.setTopic("comment");
        event.setUserId(hostHolder.getUser().getId());
        event.setEntityType(comment.getEntityType());
        event.setEntityId(comment.getEntityId());
        event.setData("post",discussPostId);

        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(Integer.parseInt(target.getUserId()));
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            Comment target = commentService.getById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        eventProducer.fireEvent(event);

        return  "redirect:/discuss/detail/" +  discussPostId;
    }
}

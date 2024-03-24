package top.rabbitbyte.nowblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;
import top.rabbitbyte.nowblog.entity.DiscussPost;
import top.rabbitbyte.nowblog.service.DiscussPostService;
import top.rabbitbyte.nowblog.mapper.DiscussPostMapper;
import org.springframework.stereotype.Service;
import top.rabbitbyte.nowblog.util.SensitiveFilter;

import java.util.List;

/**
* @author Administrator
* @description 针对表【discuss_post】的数据库操作Service实现
* @createDate 2024-03-08 15:42:11
*/
@Service
public class DiscussPostServiceImpl extends ServiceImpl<DiscussPostMapper, DiscussPost>
    implements DiscussPostService{
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Override
    public List<DiscussPost> findDiscussPosts(int userid, int offset, int limit) {

        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(userid,offset,limit);

        return discussPosts;
    }

    @Override
    public int selectDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }


    @Override
    public int updateCommentCount(int id, int commentCount) {
        return discussPostMapper.updateCommentCount(id,commentCount);
    }


    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 转义HTML标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        // 过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPost(post);
    }

    public DiscussPost findDiscussPostById(int id) {
        return discussPostMapper.selectDiscussPostById(id);
    }
}





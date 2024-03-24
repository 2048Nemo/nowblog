package top.rabbitbyte.nowblog.mapper;

import org.apache.ibatis.annotations.*;
import top.rabbitbyte.nowblog.entity.LoginTicket;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【login_ticket】的数据库操作Mapper
* @createDate 2024-03-08 15:42:11
* @Entity top.rabbitbyte.nowblog.entity.LoginTicket
*/
@Mapper
@Deprecated
public interface LoginTicketMapper extends BaseMapper<LoginTicket> {
    @Select("select * from login_ticket where ticket=#{ticket}")
    LoginTicket selectByTicket(@Param("ticket") String ticket);

    @Insert({"insert into login_ticket(user_id,ticket,status,expired)",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);


    @Update("update login_ticket set status=#{status} where ticket=#{ticket}")
    int updateStatus(@Param("status") int status,@Param("ticket") String ticket);

}





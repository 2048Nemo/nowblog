package top.rabbitbyte.nowblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.rabbitbyte.nowblog.util.RedisKeyUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DataService {
    @Autowired
    private RedisTemplate redisTemplate;
    private SimpleDateFormat  df = new SimpleDateFormat("yyyyMMdd");

    //ip写入redis
    public void recordUV(String ip){
        String redisKey = RedisKeyUtil.getUVKey(df.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(redisKey,ip);
    }

    //统计指定范围的数据
    public long calculateUV(Date start ,Date end){
        if(start ==  null ||end == null ){
            throw new IllegalArgumentException("参数不能为空");
        }
        List<String> keylist = new ArrayList<>() {};
        Calendar  calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)){

            String  key =  RedisKeyUtil.getUVKey(df.format(calendar.getTime()));
            keylist.add(key);
            calendar.add(Calendar.DATE,1);
        }
        //合并数据
        String  unionkey =  RedisKeyUtil.getUVKey(df.format(start),df.format(end));
        redisTemplate.opsForHyperLogLog().union(unionkey,keylist.toArray());

        return redisTemplate.opsForHyperLogLog().size(unionkey);
    }

    //ip写入redis
    public void recordDAU(Integer userid){
        String redisKey = RedisKeyUtil.getDAUKey(df.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(redisKey,userid);
    }

    //统计指定范围的数据
    public long calculateDAU(Date start ,Date end){
        if(start ==  null ||end == null ){
            throw new IllegalArgumentException("参数不能为空");
        }
        //bitmap运算需要使用byte数组进行运算
        List<byte[]> keylist = new ArrayList<>() {};
        Calendar  calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)){

            String  key =  RedisKeyUtil.getDAUKey(df.format(calendar.getTime()));
            keylist.add(key.getBytes());
            calendar.add(Calendar.DATE,1);
        }
        //统计数据
        return (long) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                String key = RedisKeyUtil.getDAUKey(df.format(start),df.format(end));
                connection.bitOp(RedisStringCommands.BitOperation.OR,key.getBytes(),keylist.toArray(new byte[0][0]));
                return connection.bitCount(key.getBytes());
            }
        });
    }
}

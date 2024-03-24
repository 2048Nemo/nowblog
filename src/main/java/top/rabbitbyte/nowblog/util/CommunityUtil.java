package top.rabbitbyte.nowblog.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.DigestUtils;
import org.thymeleaf.util.StringUtils;

import java.util.Map;
import java.util.UUID;

public class CommunityUtil {
    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

    //MD5加密
    public  static String  generateMD5(String key ){
        if (StringUtils.isEmpty(key)) {
            return  null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJSONString(int code, String msg, Map<String, Object> map ){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        if (map != null){
            for (String key : map.keySet()) {
                jsonObject.put( key,map.get(key));
            }
        }
        return jsonObject.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

}

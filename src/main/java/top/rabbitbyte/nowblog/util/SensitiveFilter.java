package top.rabbitbyte.nowblog.util;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {
    private static   final Logger logger  = LoggerFactory.getLogger(SensitiveFilter.class);

    private  static  final String  REPLACEMENT = "***";

    //根节点
    private final TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword  = reader.readLine()) != null){
                //添加敏感词文件行写入到前缀树中
                this.addKeyword(keyword);
            }
        }catch (IOException e ){
            logger.error("加载敏感词文件失败" + e.getMessage());
        }

    }

    //敏感词添加到前缀树中，逐行写入词
    private void addKeyword(String keyword) {
        //先创建一个临时
        TrieNode tempNode =  rootNode;

        //循环遍历节点
        for (int  i = 0;i < keyword.length() ;i++) {
            char  c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);

            if (subNode == null) {
                //初始化子节点
                subNode  = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }

            tempNode = subNode;

            if(i == keyword.length() - 1 ) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     *
     * @param text 过滤的文本
     * @return 过滤后的文本
     */
    public  String filter(String text) {
        if (StringUtils.isBlank(text)){
            return null;
        }

        //声明三个指针
        TrieNode tempNode  = rootNode;

        int begin = 0;
        int position = 0;

        //结果字符串
        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);

            //跳过符号
            if (isSymbol(c)) {
                // 若指针1处于根节点,将此符号计入结果,让指针2向下走一步
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                //指针三无论是否是在什么位置，碰到特殊符号都会跳过
                position++;
                continue;
            }
            //找到替换字符串的位置了，或者是当前字符正常，并没有被判断出来是特殊字符

            //如果当前字符是正常的并且当前subnode后面已经没有节点了，就说明这个疑似是敏感词的短语实际上是合格的，然后将这个字符串提交
            tempNode = tempNode.getSubNode(c);

            if (tempNode == null) {
                //情况一，当前前缀树分支结束，确定不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个字符位置
                begin++;
                //并同步一下字符串的位置
                position  = begin;
                //重新遍历前缀树
                tempNode = rootNode;
            }else if (tempNode.isKeywordEnd()){
                //第二种情况，没有结束字符串怀疑，并且当前前缀树分支刚好结束 ---- 证明是敏感词
                sb.append(REPLACEMENT);
                //进入下一个字符位置
                position++;
                //并同步一下字符串的位置
                 begin = position;
                //重新遍历前缀树
                tempNode = rootNode;
            } else {
                //第三种情况，没有结束字符串怀疑，并且当前前缀树分支也没有结束 ---- position继续增加
                //注意虽然上面有过position++的代码，但是请注意那个是用来排除未知字符的手段，这里才是主要position自增的代码
                position++;
            }
        }

        // 将最后一批字符计入结果
        sb.append(text.substring(begin));

        return sb.toString();
    }

    //判断是否是特殊字符
    private  boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c)&&(c < 0x2E80 || c > 0x9FFF);
    }


    //前缀树
    @Data
    private class  TrieNode{

        //关键词结束标志
        private  boolean isKeywordEnd = false;

        //子节点
        private Map<Character, TrieNode> subNode  = new HashMap<>();

        //添加子节点
        public void addSubNode(Character c,TrieNode node) {
            subNode.put(c,node);
        }

        // 获取子节点方法
        public TrieNode getSubNode(Character c) {
            return  subNode.get(c);
        }
    }
}

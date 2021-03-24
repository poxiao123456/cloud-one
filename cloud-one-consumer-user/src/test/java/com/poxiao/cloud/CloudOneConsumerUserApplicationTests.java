package com.poxiao.cloud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
@RunWith(JUnit4.class)
public class CloudOneConsumerUserApplicationTests {

    private Map<String, String> sensitiveWordMap;

    /**
     * 最小匹配规则
     * */
    private static final int minMatchTYpe = 1;
    
    @Test
    public void test01() {
        //添加到敏感词库
        Set<String> keyWordSet = new HashSet<> ();
        keyWordSet.add("中国人民");
        keyWordSet.add("中国男人");
        System.out.println("keyWordSet:"+keyWordSet);
        addSensitiveWordToHashMap(keyWordSet);
        System.out.println("sensitiveWordMap:"+sensitiveWordMap);
        //检查敏感词
    }

    /**
     * 添加到敏感词库
     * @param keyWordSet 敏感词集合
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        sensitiveWordMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
        String key = null;  //每一个敏感词
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        //迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while(iterator.hasNext()){
            key = iterator.next();    //关键字
            nowMap = sensitiveWordMap;
            for(int i = 0 ; i < key.length() ; i++){
                char keyChar = key.charAt(i);       //转换成char型
                Object wordMap = nowMap.get(keyChar);       //获取

                if(wordMap != null){        //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                }
                else{     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<String,String>();
                    newWorMap.put("isEnd", "0");     //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if(i == key.length() - 1){
                    nowMap.put("isEnd", "1");    //最后一个
                }
            }
        }
    }

    /**
     * 检查文字中是否包含敏感字符
     * @param txt 文字
     * @param beginIndex 开始匹配位置
     * @param matchType 匹配规则
     * @return 如果存在，则返回敏感词字符的长度，不存在返回0
     */
    @SuppressWarnings({ "rawtypes"})
    public int CheckSensitiveWord(String txt,int beginIndex,int matchType){
        boolean  flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0;     //匹配标识数默认为0
        char word = 0;
        Map nowMap = sensitiveWordMap;
        for(int i = beginIndex; i < txt.length() ; i++){
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);     //获取指定key
            if(nowMap != null){     //存在，则判断是否为最后一个
                matchFlag++;     //找到相应key，匹配标识+1
                if("1".equals(nowMap.get("isEnd"))){       //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true;       //结束标志位为true
//                    if(SensitivewordFilter.minMatchTYpe == matchType){    //最小规则，直接返回,最大规则还需继续查找
//                        break;
//                    }
                    if(minMatchTYpe == matchType){    //最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }
            }else{     //不存在，直接返回
                break;
            }
        }
//        if(matchFlag < 2 && !flag){
//            matchFlag = 0;
//        }
        if(!flag){
            matchFlag = 0;
        }
        return matchFlag;
    }
}

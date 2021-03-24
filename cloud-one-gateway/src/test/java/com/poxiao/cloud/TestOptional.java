package com.poxiao.cloud;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * @author qinqi
 * @date 2020/8/7
 * 测试Optional
 */
public class TestOptional {

    @Test
    public void test01() {
        //1.创建optional实例
        Optional<Object> empty = Optional.empty();
        System.out.println("empty:"+empty);
        Optional<Integer> integer = Optional.of(1);
        Optional<Integer> integer1 = Optional.ofNullable(1);
//        Assert.assertTrue(false);
        Assert.assertEquals(Integer.valueOf(2),integer1.get());
        System.out.println("111");
    }
}

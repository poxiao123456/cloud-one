package com.poxiao.cloud.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.poxiao.cloud.util.CommonResult;

/**
 * @author qq
 * @date 2021/1/19
 * 自定义限流处理逻辑
 */
public class CustomerBlockHandler {

    public static CommonResult handleException(BlockException exception) {
        return new CommonResult(2020, "自定义限流处理信息....CustomerBlockHandler");

    }

    public static CommonResult handleException2(BlockException exception) {
        return new CommonResult(2020, "自定义限流处理信息2....CustomerBlockHandler");

    }
}

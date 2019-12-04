package com.shirc.middleware.featuresconsumer.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description TODO
 * @Author shirenchuang
 * @Date 2019/12/1 10:20 PM
 **/
//@Activate(group = {Constants.CONSUMER})
public class LogFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger("dubbo");

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = null;
        long start = System.currentTimeMillis();
        String clientIP = RpcContext.getContext().getRemoteHost();
        //放入迭代版本信息
        RpcContext.getContext().setAttachment("devVersion","1.0.0");
        //入参数据在开始的时候转换，否则打印不准确
        String param = JSON.toJSONString(invocation.getArguments());

        try {
            result = invoker.invoke(invocation);
        } finally {
            String interfaceName = invoker.getInterface().getCanonicalName();
            String method = invocation.getMethodName();
            String methodFullName = interfaceName + "." + method;

            if (null != result && null != result.getException()) {
                Throwable exception = result.getException();
                if (exception instanceof Exception) {
                    logger.warn("准备调用接口" + methodFullName + "发生异常", result.getException());
                } else {
                    logger.error("准备调用接口" + methodFullName + "发生异常", result.getException());
                }
            }
            String resultStr = null == result ? "" : JSON.toJSONString(result.getValue());
            logger.info("clientIp:{},接口:{}, 入参:{} 耗时:{}ms", clientIP, methodFullName,
                    JSON.toJSONString(param), resultStr, System.currentTimeMillis() - start);

        }
        return result;
    }
}

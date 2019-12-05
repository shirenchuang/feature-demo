package com.shirc.middleware.features.featurescommon.filter;

import org.apache.dubbo.common.json.JSON;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description TODO
 * @Author shirenchuang
 * @Date 2019/12/1 10:20 PM
 **/
//@Activate(group = {Constants.PROVIDER})
public class LogFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger("dubbo");

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = null;
        long start = System.currentTimeMillis();
        String clientIP = RpcContext.getContext().getRemoteHost();
        String devVersion = RpcContext.getContext().getAttachment("devVersion");

        //入参数据在开始的时候转换，否则打印不准确
        //String param = JSON.toJSONString(invocation.getArguments());

        try {
            result = invoker.invoke(invocation);
        } finally {
            String interfaceName = invoker.getInterface().getCanonicalName();
            String method = invocation.getMethodName();
            String methodFullName = interfaceName + "." + method;

            if (null != result && null != result.getException()) {
                Throwable exception = result.getException();
                if (exception instanceof Exception) {
                    logger.warn("调用接口" + methodFullName + "发生异常", result.getException());
                } else {
                    logger.error("调用接口" + methodFullName + "发生异常", result.getException());
                }
            }


        }
        return result;
    }
}

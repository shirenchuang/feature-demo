package com.shirc.middleware.featuresconsumer.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 开发迭代信息版本号  发送到 服务提供者去;
 * @Author shirenchuang
 * @Date 2019/12/3 10:20 PM
 **/
@Activate(group = {Constants.CONSUMER})
public class DevVersionFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger("dubbo");

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = null;
        long start = System.currentTimeMillis();
        String clientIP = RpcContext.getContext().getRemoteHost();
        //放入迭代版本信息
        RpcContext.getContext().setAttachment("devVersion","1.0.0");

        try {
            result = invoker.invoke(invocation);
        } finally {
            String interfaceName = invoker.getInterface().getCanonicalName();
            String method = invocation.getMethodName();
            String methodFullName = interfaceName + "." + method;
            String resultStr = null == result ? "" : JSON.toJSONString(result.getValue());
            logger.info("接口:{}", methodFullName);
        }
        return result;
    }
}

package com.shirc.middleware.features.featuresservice.filter;

import com.alibaba.dubbo.rpc.*;
import com.shirc.middleware.features.featuresservice.dev.MyThreadLocal;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 消费别人服务的时候会走到这里
 *              要把 迭代版本号 放到参数里面传到 服务提供者
 * @Author shirenchuang
 * @Date 2019/12/1 10:20 PM
 **/
@Activate(group = {Constants.CONSUMER})
public class DevVersionConsumerFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger("dubbo");

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {


        try {
            String toDevVersion = MyThreadLocal.getDevVersion();
            RpcContext.getContext().setAttachment("devVersion",toDevVersion);
            return invoker.invoke(invocation);
        } finally {
            String interfaceName = invoker.getInterface().getCanonicalName();
            String method = invocation.getMethodName();
            String methodFullName = interfaceName + "." + method;
            logger.info("接口:{}", methodFullName);
        }
    }
}

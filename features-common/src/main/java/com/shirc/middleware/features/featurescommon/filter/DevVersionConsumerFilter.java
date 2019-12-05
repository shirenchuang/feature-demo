package com.shirc.middleware.features.featurescommon.filter;

import com.shirc.middleware.features.featurescommon.dev.MyThreadLocal;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
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


        String toDevVersion = MyThreadLocal.getDevVersion();
        try {
            RpcContext.getContext().setAttachment("devVersion",toDevVersion);
            return invoker.invoke(invocation);
        } finally {
            String interfaceName = invoker.getInterface().getCanonicalName();
            String method = invocation.getMethodName();
            String methodFullName = interfaceName + "." + method;
            logger.info("Consumer 接口:{},设置的迭代版本号:{}", methodFullName,toDevVersion);
        }
    }
}

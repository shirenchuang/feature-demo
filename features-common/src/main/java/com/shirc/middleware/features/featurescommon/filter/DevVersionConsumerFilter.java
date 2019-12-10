package com.shirc.middleware.features.featurescommon.filter;

import com.shirc.middleware.features.featurescommon.dev.MyThreadLocal;
import com.shirc.middleware.features.featurescommon.dev.TraceUtil;
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
    private static final Logger logger = LoggerFactory.getLogger("devVersion");

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        String traceId = TraceUtil.getTraceId();
        RpcContext.getContext().setAttachment("myTraceId",traceId);

        String toDevVersion = MyThreadLocal.getDevVersion();
        RpcContext.getContext().setAttachment("devVersion",toDevVersion);
        doLog(invoker,invocation,traceId);
        return invoker.invoke(invocation);

    }

    private void doLog(Invoker<?> invoker, Invocation invocation,String traceId){
        String interfaceName = invoker.getInterface().getCanonicalName();
        String method = invocation.getMethodName();
        String methodFullName = interfaceName + "." + method;
        StringBuffer sb = new StringBuffer();
        sb.append("==TraceId:").append(traceId)
        .append("=== ConsumerFilter:当前自身版本:").append(MyThreadLocal.localVersion)
                .append("; 接收传递版本:").append(MyThreadLocal.getFromVersion())
                .append("; 往后传递版本:").append(MyThreadLocal.getDevVersion())
        .append(" ;调用服务=> ").append(methodFullName);
        logger.info(sb.toString());
    }
}

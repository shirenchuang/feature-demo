package com.shirc.middleware.features.featurescommon.filter;

import com.shirc.middleware.features.featurescommon.dev.MyThreadLocal;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 当前服务提供者在被真正调用之前获取 消费者过来的迭代版本号
 *              然后保存在本地线程变量中,在调用其他dubbo服务的时候 要带上版本号
 * @Author shirenchuang
 * @Date 2019/12/1 10:20 PM
 **/
@Activate(group = {Constants.PROVIDER})
public class DevVersionProviderFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger("devVersion");

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {


        String fromDevVersion = RpcContext.getContext().getAttachment("devVersion");
        try {
            //放入到本地线程存放
            MyThreadLocal.devVersion.set(fromDevVersion);
            return invoker.invoke(invocation);
        } finally {
            String interfaceName = invoker.getInterface().getCanonicalName();
            String method = invocation.getMethodName();
            String methodFullName = interfaceName + "." + method;
            logger.info("Provider:接口:{},获取的迭代版本:{}", methodFullName,fromDevVersion);
        }
    }
}

package com.shirc.middleware.features.featuresservice.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import com.shirc.middleware.features.featuresservice.dev.MyThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 服务提供者在调用之前获取 迭代版本号
 *              然后保存在本地线程变量中,在调用其他dubbo服务的时候 要带上版本号
 * @Author shirenchuang
 * @Date 2019/12/1 10:20 PM
 **/
@Activate(group = {Constants.PROVIDER})
public class DevVersionProviderFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger("dubbo");

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {


        try {
            String fromDevVersion = RpcContext.getContext().getAttachment("devVersion");
            //放入到本地线程存放
            MyThreadLocal.devVersion.set(fromDevVersion);
            return invoker.invoke(invocation);
        } finally {
            String interfaceName = invoker.getInterface().getCanonicalName();
            String method = invocation.getMethodName();
            String methodFullName = interfaceName + "." + method;
            logger.info("接口:{}", methodFullName);
        }
    }
}

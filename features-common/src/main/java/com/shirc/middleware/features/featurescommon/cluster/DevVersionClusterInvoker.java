/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shirc.middleware.features.featurescommon.cluster;

import com.google.common.collect.Lists;
import com.shirc.middleware.features.featurescommon.dev.MyThreadLocal;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.registry.integration.RegistryDirectory;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.cluster.Directory;
import org.apache.dubbo.rpc.protocol.InvokerWrapper;

import java.util.List;

/**
 * @author shirenchuang
 * 2019/12/10
 * 集群扩展包装器
 * 参照 {@link org.apache.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker}
 */
public class DevVersionClusterInvoker<T> implements Invoker<T> {

    private static final Logger logger = LoggerFactory.getLogger(DevVersionClusterInvoker.class);

    private final Directory<T> directory;

    private final Invoker<T> invoker;

    public DevVersionClusterInvoker(Directory<T> directory, Invoker<T> invoker) {
        this.directory = directory;
        this.invoker = invoker;
    }

    @Override
    public URL getUrl() {
        return directory.getUrl();
    }

    @Override
    public boolean isAvailable() {
        return directory.isAvailable();
    }

    @Override
    public void destroy() {
        this.invoker.destroy();
    }

    @Override
    public Class<T> getInterface() {
        return directory.getInterface();
    }

    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        // 找到迭代版本号
        return doDevVersionInvoke(invocation, null);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Result doDevVersionInvoke(Invocation invocation, RpcException e) {
        Result result ;
        Invoker<T> minvoker;

        List<Invoker<T>> devVersionInvokers = selectDevVersionInvoker(invocation);
        if (CollectionUtils.isEmpty(devVersionInvokers)) {
            logger.error("没有找到服务啊~~~~ ");
            throw new RpcException("没有找到服务啊~~~~");
        } else {
            minvoker = devVersionInvokers.get(0);
        }
        try {
            result = minvoker.invoke(invocation);
        } catch (RpcException me) {
            if (me.isBiz()) {
                result = new RpcResult(me.getCause());
            } else {
                throw new RpcException(me.getCode(), getDevVersionExceptionMessage(e, me), me.getCause());
            }
        } catch (Throwable me) {
            throw new RpcException(getDevVersionExceptionMessage(e, me), me.getCause());
        }
        return result;
    }

    private String getDevVersionExceptionMessage(Throwable t, Throwable mt) {
        String msg = "devVersion error : " + mt.getMessage();
        if (t != null) {
            msg = msg + ", invoke error is :" + StringUtils.toString(t);
        }
        return msg;
    }


    /**
     * 获取对应迭代版本服务
     * @param invocation
     * @return
     */
    private List<Invoker<T>> selectDevVersionInvoker(Invocation invocation) {
        List<Invoker<T>> invokers = null;
        if (invocation instanceof RpcInvocation) {
            try {
                invokers = directory.list(invocation);
                //经过了dubbo的栓选之后,我们来找自己需要的Invokes
                String devVersion = MyThreadLocal.getDevVersion();
                List<Invoker<T>> newInvokers = Lists.newArrayList();
                for (Invoker invoker : invokers){
                    //获取应用名称
                    String applcation  = ((InvokerDelegate) invoker).getProviderUrl().getParameter(Constants.APPLICATION_KEY);

                    if(StringUtils.isEmpty(devVersion)){
                        if(applcation.indexOf(MyThreadLocal.spiltString)==-1){
                            //不是迭代过来或者本身不是迭代的请求    只能访问非迭代版本
                            newInvokers.add(invoker);
                        }
                    }else {
                        //是迭代的请求  就需要找对应的迭代服务
                        if(applcation.indexOf(MyThreadLocal.spiltString)!=-1){
                            applcation = applcation.substring(applcation.indexOf(MyThreadLocal.spiltString)+5);
                            if(applcation.equals(devVersion)){
                                newInvokers.add(invoker);
                            }
                        }
                    }
                    return invokers;
                }
            } catch (RpcException e) {

                logger.error("获取 迭代版本 的服务时 发生错误~~:"+ directory.getUrl().getServiceInterface() + ", method:" + invocation.getMethodName()
                        , e);
            }
        }
        return invokers;
    }

    /**
     *     手动把这个复制来 强制转换
     */
    private static class InvokerDelegate<T> extends InvokerWrapper<T> {
        private URL providerUrl;

        public InvokerDelegate(Invoker<T> invoker, URL url, URL providerUrl) {
            super(invoker, url);
            this.providerUrl = providerUrl;
        }

        public URL getProviderUrl() {
            return providerUrl;
        }
    }

    @Override
    public String toString() {
        return "invoker :" + this.invoker + ",directory: " + this.directory;
    }


    public static void main(String[] args) {

        String applcation = "maybach-go"+MyThreadLocal.spiltString+"1.0.1";
        boolean b = applcation.indexOf(MyThreadLocal.spiltString)==-1;
        applcation = applcation.substring(applcation.indexOf(MyThreadLocal.spiltString)+5);


    }
}

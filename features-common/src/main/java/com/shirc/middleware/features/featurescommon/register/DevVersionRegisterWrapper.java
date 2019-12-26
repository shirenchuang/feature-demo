package com.shirc.middleware.features.featurescommon.register;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.NotifyListener;
import org.apache.dubbo.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author shirenchuang
 * Registry 的包装类
 * 修改URL 中的Application
 * @date 2019/12/5  8:35 下午
 */

public class DevVersionRegisterWrapper implements Registry {

    private static final Logger logger = LoggerFactory.getLogger("devVersion");


    private Registry registry;

    /**
     * 注入Register
     * @param registry
     */
    public DevVersionRegisterWrapper(Registry registry) {
        this.registry = registry;
    }

    @Override
    public URL getUrl() {
        return DevVersionRegisterFactoryWrapper.changeApplication(registry.getUrl());
    }

    @Override
    public boolean isAvailable() {
        return registry.isAvailable();
    }

    @Override
    public void destroy() {
        registry.destroy();
    }

    @Override
    public void register(URL url) {
        registry.register(DevVersionRegisterFactoryWrapper.changeApplication((url)));
    }

    @Override
    public void unregister(URL url) {
        registry.register(DevVersionRegisterFactoryWrapper.changeApplication((url)));
    }

    @Override
    public void subscribe(URL url, NotifyListener listener) {
        registry.subscribe(DevVersionRegisterFactoryWrapper.changeApplication((url)),listener);
    }

    @Override
    public void unsubscribe(URL url, NotifyListener listener) {
        registry.unsubscribe(DevVersionRegisterFactoryWrapper.changeApplication((url)),listener);
    }

    @Override
    public List<URL> lookup(URL url) {
        return registry.lookup(DevVersionRegisterFactoryWrapper.changeApplication((url)));
    }
}

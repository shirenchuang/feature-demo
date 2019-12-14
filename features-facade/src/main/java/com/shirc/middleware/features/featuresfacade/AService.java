package com.shirc.middleware.features.featuresfacade;

/**
 * @author shirenchuang
 * @date 2019/12/5  4:27 下午
 */


public interface AService {

    public String call();

    /**
     * A=>B=C
     * @return
     */
    String A_callB_C();

    String featureDemo(String name);
}

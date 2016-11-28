package com.zheng.infras.springboot.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/27/027.
 */
public class DsrvFactory {

    private Map<Class,Object> dsrvCache = new HashMap<>();

    private ApplicationConfig applicationConfig;

    private RegistryConfig registryConfig;

    private String dubboVersion ;

    public DsrvFactory(String appName,String zkAddress,String dubboServiceVersion){
        applicationConfig = new ApplicationConfig();
        applicationConfig.setName(appName);

        registryConfig = new RegistryConfig();
        registryConfig.setAddress(zkAddress);

        this.dubboVersion = dubboServiceVersion;

    }

    public <T> T getDsrv (Class<T> clazz){
        if(dsrvCache.get(clazz) == null){
            ReferenceConfig<T> referenceConfig = new ReferenceConfig<>();
            referenceConfig.setApplication(applicationConfig);
            referenceConfig.setRegistry(registryConfig);
            referenceConfig.setInterface(clazz);
            referenceConfig.setVersion(dubboVersion);
            dsrvCache.put(clazz,referenceConfig.get());
        }
        return (T) dsrvCache.get(clazz);
    }
}

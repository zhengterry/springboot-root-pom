package com.zheng.infras.springboot.dubbo.postprocessor;

import com.zheng.infras.springboot.dubbo.DsrvFactory;
import com.zheng.infras.springboot.dubbo.annotation.Dsrv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/11/27/027.
 */
@Component
public class DubboAurowriteBeanPostProcessor implements BeanPostProcessor ,InitializingBean,EnvironmentAware,PriorityOrdered{

    private static final Logger logger = LoggerFactory.getLogger(DubboAurowriteBeanPostProcessor.class);

    private Environment env;

    private DsrvFactory dsrvFactory;

    /**
     * 读取配置文件
     * @param environment
     */
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    /**
     * 设置bean的参数
     *
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        String appName = env.getProperty("dubbo.appName");
        String zkAddress = env.getProperty("dubbo.zkAddress");
        String dubboVersion = env.getProperty("dubbo.dubboVersion");
        if(StringUtils.isEmpty(appName) || StringUtils.isEmpty(zkAddress)||StringUtils.isEmpty(dubboVersion)){
            logger.warn("appName and zkAddress and dubboVersion could not be empty");
            throw new IllegalStateException("appName and zkAddress and dubboVersion could not be empty");
        }
        this.dsrvFactory = new DsrvFactory(appName,zkAddress,dubboVersion);
    }

    /**
     * spring bean 实例化之前进行配置
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean == null){
            return bean;
        }
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields){
            if(field.isAnnotationPresent(Dsrv.class)){
                Class dsrvType = field.getType();
                field.setAccessible(true);
                try {
                    logger.info("[rsrv-dubbo] inject " + dsrvType.getSimpleName() + " to bean " + beanName);
                    field.set(bean,dsrvFactory.getDsrv(dsrvType));
                } catch (IllegalAccessException e) {
                    throw new BeanInstantiationException(bean.getClass(),e.getMessage(), e);
                }
            }
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String s) throws BeansException {
        return bean;
    }

    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}

package com.asiainfo.xwbo.xwbo.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @author Zhouce Chen
 * @version Jun 19, 2017
 */
@Component
public class BeanFactory implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    private static Logger LOG = LoggerFactory.getLogger(BeanFactory.class);
    private static DefaultListableBeanFactory defaultListableBeanFactory;

    public BeanFactory() {
    }


    public static <T> T getBean(Class<T> cls) {
        if (applicationContext != null) {
            return applicationContext.getBean(cls);
        } else {
            LOG.warn("Spring容器为空，无法获取beanClass：{}", cls);
            return null;
        }
    }

    public static Object getBean(String name) {
        if (applicationContext != null) {
            return applicationContext.getBean(name);
        } else {
            LOG.warn("Spring容器为空，无法获取beanName：{}", name);
            return null;
        }
    }

//    public static Map<String, Object> getBeansOfType(Class cls) {
//        if(applicationContext != null) {
//            return applicationContext.getBeansOfType(cls);
//        } else {
//            LOG.warn("Spring容器为空，无法获取bean type：{}", cls);
//            return null;
//        }
//    }

//    public static Map<String, Object> getBeansWithAnnotation(Class annotationType) {
//        if(applicationContext != null) {
//            return applicationContext.getBeansWithAnnotation(annotationType);
//        } else {
//            LOG.warn("Spring容器为空，无法获取annotationType：{}", annotationType);
//            return null;
//        }
//    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext1) throws BeansException {
        applicationContext = applicationContext1;
//        Configuration.getInstance().loadClasspathProperties(applicationContext1);
        boolean isWebApp = false;
        ConfigurableListableBeanFactory beanFactory = null;
        if (applicationContext instanceof GenericApplicationContext) {
            beanFactory = ((GenericApplicationContext) applicationContext).getBeanFactory();
        } else if (applicationContext instanceof FileSystemXmlApplicationContext) {
            beanFactory = ((FileSystemXmlApplicationContext) applicationContext).getBeanFactory();
        } else if (applicationContext instanceof XmlWebApplicationContext) {
            beanFactory = ((XmlWebApplicationContext) applicationContext).getBeanFactory();
            isWebApp = true;
        } else {
            LOG.warn("applicationContext的类型没有识别成功,为：{}", applicationContext1.getClass().getName());
        }

        if (beanFactory instanceof DefaultListableBeanFactory) {
            defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        } else {
//            LOG.warn("beanFactory的类型没有识别成功，为：{}", beanFactory.getClass().getName());
        }

//        if(defaultListableBeanFactory != null) {
//            this.initDynamicBeans(isWebApp);
//        } else {
//            LOG.warn("beanFactory的类型没有识别成功，为：{}", beanFactory.getClass().getName());
//        }

    }

    public static void registerBeanDefinition(String beanId, GenericBeanDefinition beanDefinition) {
        defaultListableBeanFactory.registerBeanDefinition(beanId, beanDefinition);

    }

//    public static BeanDefinition getBeanDefinition(String beanName) {
//        return defaultListableBeanFactory.getBeanDefinition(beanName);
//    }

//    private void initDynamicBeans(boolean isWebApp) {
//        Map<String, DataSourceDefine> defs = DataSourceHolder.getInstance().getDsDefs();
//        String dbName = "";
//        String dbCNName = "";
//
//        try {
//            Iterator iterator = defs.entrySet().iterator();
//
//            while(iterator.hasNext()) {
//                Map.Entry<String, DataSourceDefine> entry = (Map.Entry)iterator.next();
//                DataSourceDefine def = (DataSourceDefine)entry.getValue();
//                dbName = def.getId();
//                dbCNName = def.getName();
//                registerBeanDefinition(def.getId(), this.getBeanDefinition(def, isWebApp), def.getAlias());
//            }
//        } catch (Exception var8) {
//            LOG.info("数据源：[{}:{}]注入异常，请检查配置", dbName, dbCNName);
//            var8.printStackTrace();
//        }
//
//    }

//    private GenericBeanDefinition getBeanDefinition(DataSourceDefine def, boolean isWebApp) {
//        GenericBeanDefinition gbd = null;
//        if(isWebApp && def.getJndiName() != null && def.getJndiName().length() > 0) {
//            gbd = new GenericBeanDefinition();
//            gbd.setBeanClass(JndiObjectFactoryBean.class);
//            MutablePropertyValues mpv = new MutablePropertyValues();
//            mpv.add("jndiName", def.getJndiName());
//            gbd.setPropertyValues(mpv);
//        } else {
//            gbd = DataSourceHolder.getInstance().genDataSourceBeanDefinition(def.getUrl(), def.getDriverClassName(), def.getUsername(), def.getPassword());
//        }
//
//        return gbd;
//    }
}


package org.kolesnyk.utils;

import org.kolesnyk.facade.BookingFacade;
import org.kolesnyk.repository.StorageFiller;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class StoragePostProcessor implements BeanPostProcessor {

    public Object postProcessAfterInitialization(Object bean, String beanName) {

        if (bean instanceof StorageFiller) {
            ((StorageFiller) bean).initData();
        }

        return bean;
    }
}

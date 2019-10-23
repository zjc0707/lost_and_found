package com.jc.lost_and_found.cache;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

import java.util.Properties;

/**
 * 创建监听器工厂类
 * properties 可在xml文件中配置，获取指定监听器。
 *
 * @author InitUser
 */

public class MyCacheEventListenerFactory extends CacheEventListenerFactory {

    @Override
    public CacheEventListener createCacheEventListener(Properties properties) {
        return new CacheListener();
    }
}

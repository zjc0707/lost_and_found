package com.jc.lost_and_found.cache;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

/**
 * @author InitUser
 */
@Slf4j
public class CacheListener implements CacheEventListener {

    @Override
    public void notifyElementRemoved(Ehcache cache, Element element)
            throws CacheException {
        log.info("notifyElementRemoved");
        log.info("cache：" + cache.toString());
        log.info("element: " + element.toString());

    }

    @Override
    public void notifyElementPut(Ehcache cache, Element element)
            throws CacheException {
        log.info("notifyElementPut");
        log.info("cache：" + cache.toString());
        log.info("element: " + element.toString());
    }

    @Override
    public void notifyElementUpdated(Ehcache cache, Element element)
            throws CacheException {
        log.info("notifyElementUpdated");
        log.info("cache：" + cache.toString());
        log.info("element: " + element.toString());
    }

    @Override
    public void notifyElementExpired(Ehcache cache, Element element) {
        log.info("notifyElementExpired");
        log.info("cache：" + cache.toString());
        log.info("element: " + element.toString());
    }

    @Override
    public void notifyElementEvicted(Ehcache cache, Element element) {
        log.info("notifyElementExpired");
        log.info("cache：" + cache.toString());
        log.info("element: " + element.toString());
    }

    @Override
    public void notifyRemoveAll(Ehcache cache) {
        log.info("notifyElementExpired");
        log.info("cache：" + cache.toString());
//        cache.removeAll();
    }

    @Override
    public void dispose() {

    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

}

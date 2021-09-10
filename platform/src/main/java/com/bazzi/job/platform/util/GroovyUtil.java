package com.bazzi.job.platform.util;

import com.bazzi.job.platform.bean.DefineJob;
import groovy.lang.GroovyClassLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public final class GroovyUtil {
    private static final ConcurrentMap<Integer, String> md5Map = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Class<?>> groovyMap = new ConcurrentHashMap<>();

    private static final GroovyClassLoader groovyClassLoader;

    static {
        ClassLoader parent = AutowiredAnnotationBeanPostProcessor.class.getClassLoader();
        groovyClassLoader = new GroovyClassLoader(parent);
    }

    public static Class<?> loadClass(DefineJob defineJob) {
        String script = defineJob.getJobGroovy();
        String md5 = script.hashCode() + "";
        Integer id = defineJob.getJobId();
        if (md5Map.containsKey(id)) {
            String curMd5 = md5Map.get(id);
            if (StringUtils.equals(curMd5, md5)) {//加载过且脚本没有变动，直接获取
                return groovyMap.get(md5);
            } else {//加载过，但脚本变动，移除原有保存的记录
                md5Map.remove(id);
                groovyMap.remove(curMd5);
            }
        }
        Class<?> clazz = groovyClassLoader.parseClass(defineJob.getJobGroovy());
        groovyMap.put(md5, clazz);
        md5Map.put(id, md5);
        return clazz;
    }

    public static void remove(DefineJob defineJob) {
        Integer id = defineJob.getJobId();
        if (md5Map.containsKey(id)) {
            String md5 = md5Map.get(id);
            md5Map.remove(id);
            groovyMap.remove(md5);
        }
    }
}

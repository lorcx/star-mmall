package com.mmall.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;


/**
 * @Author lx
 * @Date 2017/11/18 15:02
 */
public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private static Properties props;

    static {
        String fileName = "mmall.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "UTF-8"));
        } catch (IOException e) {
            logger.error("配置文件错误", e);
        }
    }

    /**
     * 获取配置文件中的值
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim();
    }

    /**
     * 获取配置文件中的值，如果不存在返回默认值
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getProperty(String key, String defaultValue) {
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            value = defaultValue;
        }
        return value;
    }
}

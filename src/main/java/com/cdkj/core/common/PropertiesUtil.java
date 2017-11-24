package com.cdkj.core.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {

    private static Properties props = null;

    public static void init(String configFile) {
        props = new Properties();
        try {
            InputStream in = PropertiesUtil.class
                .getResourceAsStream(configFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            props.load(br);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        if (props == null) {
            // throw new Exception("配置文件初始化失败");
        }
        return props.getProperty(key);
    }

    public static final class Config {
        public static String PAY_BACK_URL = props.getProperty("PAY_BACK_URL");

        public static String USER_URL = props.getProperty("USER_URL");

        public static String ACCOUNT_URL = props.getProperty("ACCOUNT_URL");

        public static String MALL_URL = props.getProperty("MALL_URL");

        public static String USER_DB = props.getProperty("USER_DB");
    }
}
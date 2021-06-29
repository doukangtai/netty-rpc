package com.netty.rpc.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author 窦康泰
 * @date 2021/06/29
 */
public class PropertiesFileUtil {
    private static final Logger log = LoggerFactory.getLogger(PropertiesFileUtil.class);

    public static Properties readPropertiesFile(String fileName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        String path = url.getPath() + fileName;
        Properties properties = null;
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8)) {
            properties = new Properties();
            properties.load(inputStreamReader);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return properties;
    }
}

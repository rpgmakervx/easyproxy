package org.easyproxy.config;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;

import static org.easyproxy.constants.Const.*;

/**
 * Description :
 * Created by xingtianyu on 16-12-18
 * 下午3:53
 */

public class ConfigFactory {

    private static Config config;
    private static String configDir;

    private ConfigFactory() {
    }

    public static void init(){
        if (StringUtils.isEmpty(configDir)){
            configDir = CONF;
        }
        File file = new File(configDir);
        Set<String> configNames = new HashSet<>();
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String fileName = pathname.getName();
                String prefix = fileName.substring(0, fileName.lastIndexOf("."));
                return prefix.equals(CONFIGNAME);
            }
        });
        for (File f : files) {
            String fileName = f.getName();
            String suffix = fileName.substring(
                    fileName.lastIndexOf(".") + 1, fileName.length());
            configNames.add(suffix);
        }
        if (configNames.contains(PROPCONFIG)) {
            config = new PropertyConfig(CONF + DEFAULT_CONFIGFILE);
        } else if (configNames.contains(XMLCONFIG)) {
            config = new XmlConfig(CONF + XML_CONFIGFILE);
        }
    }
    public static Config getConfig() {
        return config;
    }

    /**
     * 请在AppInit初始化之前设置配置文件路径，初始化结束后设置无效
     * @param dir
     */
    public static void setConfigDir(String dir){
        configDir = dir;
    }

    public static void main(String[] args) {
        System.out.println("aaa.txt".lastIndexOf("."));
    }
}

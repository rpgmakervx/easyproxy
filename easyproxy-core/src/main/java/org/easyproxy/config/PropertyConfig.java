package org.easyproxy.config;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.easyproxy.api.app.pojo.HostVO;
import org.easyproxy.constants.LBStrategy;
import org.easyproxy.pojo.WeightHost;
import org.easyproxy.util.struct.PropertiesUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Description :
 * Created by xingtianyu on 16-12-16
 * 下午8:29
 */

public class PropertyConfig extends Config {

    private PropertiesUtil util;

    public PropertyConfig(String path){
        configPath = path;
        util = new PropertiesUtil(path);
        params = new JSONObject(util.getConfigMap());
        init();
    }

    public PropertyConfig(InputStream is){
        util = new PropertiesUtil(is);
        params = new JSONObject(util.getConfigMap());
        init();
    }

    @Override
    public String getString(String param) {
        String val = typeMapper.get(this.getClass()).getString(param);
        if (StringUtils.isEmpty(param)||val == null)
            return String.valueOf(ConfigEnum.getEnum(param).defVal);
        return val;
    }

    @Override
    public Boolean getBoolean(String param) {
        Boolean val = typeMapper.get(this.getClass()).getBoolean(param);
        if (StringUtils.isEmpty(param)||val == null)
            return Boolean.valueOf(String.valueOf(ConfigEnum.getEnum(param).defVal));
        return val;
    }

    @Override
    public Integer getInt(String param) {
        Integer val = typeMapper.get(this.getClass()).getInteger(param);
        if (StringUtils.isEmpty(param)||val == null)
            return Integer.valueOf(String.valueOf(ConfigEnum.getEnum(param).defVal));
        return val;
    }

    @Override
    public void setLBStrategy(String strategy) {
        params.put(ConfigEnum.LB_STRATEGY.key, strategy);
    }

    @Override
    public LBStrategy getLBStrategy() {
        String strategy = (String) params.get(ConfigEnum.LB_STRATEGY.key);
        return LBStrategy.getStrategy(strategy);
    }

    @Override
    public String getConfigPath() {
        return configPath;
    }

    public void listAll() {
        System.out.println(JSONObject.toJSONString(params));
    }

}

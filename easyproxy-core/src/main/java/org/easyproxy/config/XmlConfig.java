package org.easyproxy.config;/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午11:08
 */

import org.apache.commons.lang3.StringUtils;
import org.easyproxy.constants.LBStrategy;
import org.easyproxy.util.struct.JSONUtil;
import org.easyproxy.util.struct.XmlUtil;

import java.io.InputStream;


/**
 * Description :
 * Created by YangZH on 16-8-14
 * 下午11:08
 */

public class XmlConfig extends Config {
    private XmlUtil util;

    public XmlConfig(String path) {
        configPath = path;
        util = new XmlUtil(path);
        params = JSONUtil.str2Json(util.xml2Json());
        init();
    }

    public XmlConfig(InputStream is) {
        util = new XmlUtil(is);
        System.out.println("param: " + util.xml2Json());
        params = JSONUtil.str2Json(util.xml2Json());
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
        Integer val = typeMapper.get(this.getClass()).getIntValue(param);
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

}

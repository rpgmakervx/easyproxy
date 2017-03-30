package org.easyproxy.api.app.filter;

import org.easyarch.netcat.web.http.request.impl.HttpHandlerRequest;
import org.easyarch.netcat.web.http.response.impl.HttpHandlerResponse;
import org.easyarch.netcat.web.http.session.HttpSession;
import org.easyarch.netcat.web.mvc.action.filter.HttpFilter;
import org.easyproxy.config.Config;
import org.easyproxy.config.ConfigEnum;
import org.easyproxy.config.ConfigFactory;

/**
 * Created by xingtianyu on 17-3-31
 * 上午12:14
 * description:
 */

public class OperateFilter implements HttpFilter {

    private static final String STATUS = "status";

    @Override
    public boolean before(HttpHandlerRequest request, HttpHandlerResponse response) throws Exception {
        HttpSession session = request.getSession();
        Boolean status = (Boolean) session.getAttr(STATUS);
        if (status){
            return true;
        }
        Config config = ConfigFactory.getConfig();
        String admin = config.getString(ConfigEnum.API_ADMIN.key);
        String key = config.getString(ConfigEnum.API_KEY.key);
        String user = request.getParam("user");
        String passwd = request.getParam("passwd");
        if (admin.equals(user)&&key.equals(passwd)){
            request.getSession().setAttr(STATUS,true);
            return true;
        }
        return false;
    }

    @Override
    public void after(HttpHandlerRequest httpHandlerRequest, HttpHandlerResponse httpHandlerResponse) throws Exception {

    }
}

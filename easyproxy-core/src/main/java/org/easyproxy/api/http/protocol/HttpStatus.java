package org.easyproxy.api.http.protocol;

/**
 * Description :
 * Created by xingtianyu on 17-2-23
 * 下午8:49
 * description:
 */

public interface HttpStatus {

    int OK = 200;
    int CREATED = 201;
    int ACCEPTED = 202;
    int PARTIAL_INFO = 203;
    int NO_RESPONSE = 204;
    int MOVED = 301;
    int REDIRECT = 302;
    int METHOD = 303;
    int NOT_MODIFIED = 304;
    int BAD_REQUEST = 400;
    int UNAUTHORIZED = 401;
    int PAYMENT_REQUIRED = 402;
    int FORBIDDEN = 403;
    int NOT_FOUND = 404;
    int METHOD_NOT_ALLOWED = 405;
    int CONFLICT = 409;
    int INTERNAL_SERVER_ERROR = 500;
    int NOT_IMPLEMENTED = 501;
    int BAD_GATEWAY = 502;
    int GATEWAY_TIMEOUT = 503;

}

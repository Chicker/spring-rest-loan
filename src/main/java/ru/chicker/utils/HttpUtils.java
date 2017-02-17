package ru.chicker.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public final class HttpUtils {
    private static final String[] IP_HEADER_CANDIDATES = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR" };

    private HttpUtils() {
    }
    
    public static Optional<String> getClientIpAddress(HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        
        if (!clientIp.equals("0:0:0:0:0:0:0:1")) {
            return Optional.of(clientIp);
        } else {
            // if we have got loopback address, then we will try to extract the ip-address from 
            // headers
            for (String header : IP_HEADER_CANDIDATES) {
                String ip = request.getHeader(header);
                if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                    return Optional.of(ip);
                }
            }    
        }
        
        return Optional.empty();
    }
}

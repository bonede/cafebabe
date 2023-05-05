package app.cafebabe.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public abstract class IpUtils {
    private static final List<String> IP_HEADERS = Arrays.asList(
            "x-forwarded-for",
            "proxy-client-ip",
            "http-client-ip",
            "http-x-forwarded-for"
    );
    public static String getIp(HttpServletRequest request) {
        for(String header : IP_HEADERS){
            String ips = request.getHeader(header);
            if(StringUtils.isNoneBlank(ips)){
                String[] parts = ips.split(", ");
                return parts[0];
            }
        }
        return request.getRemoteAddr();
    }
}

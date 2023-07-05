package com.nimbleways.odinzeye.datacollector.services;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class CurrentRequestIDUtils {

    public static String getCurrentRequestID() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return  request.getHeader("requestID");
        } catch(Exception e){
            return null;
        }
    }

    public static String getCurrentClientID(){
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return  request.getHeader("clientID");
        } catch(Exception e){
            return null;
        }
    }
}

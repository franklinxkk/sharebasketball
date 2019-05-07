package com.bblanqiu.event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

/**
 * 
 * @author xkk
 *
 */
@Component
public class RequestMappingEventHandler implements ApplicationListener<ServletRequestHandledEvent> {

    private static Logger logger = LoggerFactory.getLogger(RequestMappingEventHandler.class);

    @Override
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        logger.info("uri: {}, method: {}, time: {}ms", event.getRequestUrl(), event.getMethod(), event.getProcessingTimeMillis());
    }

}

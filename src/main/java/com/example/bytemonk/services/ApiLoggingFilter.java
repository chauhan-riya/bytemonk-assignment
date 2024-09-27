package com.example.bytemonk.services;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.logging.Logger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ApiLoggingFilter implements Filter {

    private final Logger logger = Logger.getLogger(ApiLoggingFilter.class.getName());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, jakarta.servlet.FilterChain filterChain) throws IOException, ServletException {

        String method = null;
        String endpoint = null;

        if (servletRequest instanceof SecurityContextHolderAwareRequestWrapper) {
            SecurityContextHolderAwareRequestWrapper wrapper = (SecurityContextHolderAwareRequestWrapper) servletRequest;
            method = wrapper.getMethod();
            endpoint = wrapper.getRequestURI();
        }

        // Get current timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // Get client IP address
        String ipAddress = servletRequest.getRemoteAddr();

        // Log the request details
        String logMessage = String.format("%s - IP: %s, Method: %s, Endpoint: %s", timestamp, ipAddress, method, endpoint);
        logger.info(logMessage);

        // Proceed with the next filter in the chain
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {}
}

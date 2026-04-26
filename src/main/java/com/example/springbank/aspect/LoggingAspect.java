package com.example.springbank.aspect;

import com.example.springbank.annotation.LogController;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    @Around("@within(logController)")
    public Object logController(ProceedingJoinPoint pjp, LogController logController) throws Throwable {
        String methodName = resolveLabel(pjp, logController);

        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attrs != null ? attrs.getRequest() : null;

        String httpMethod = request != null ? request.getMethod() : "N/A";
        String uri = request != null ? request.getRequestURI() : "N/A";

        long start = System.currentTimeMillis();

        log.info("[HTTP {}] -> {} | args={}",
                httpMethod,
                uri,
                Arrays.toString(pjp.getArgs())
        );

            Object result = pjp.proceed();

            long duration = System.currentTimeMillis() - start;

            log.info("[HTTP {}] <- {} | {} ms",
                    httpMethod,
                    methodName,
                    duration
            );

            return result;
    }

    private String resolveLabel(ProceedingJoinPoint pjp, LogController annotation) {
        if (!annotation.value().isBlank()) {
            return annotation.value();
        }
        return pjp.getSignature().toShortString();
    }
}

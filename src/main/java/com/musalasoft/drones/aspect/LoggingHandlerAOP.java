package com.musalasoft.drones.aspect;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingHandlerAOP {

    private final Optional<HttpServletRequest> request;

    @Pointcut("execution(* com.musalasoft.drones.controller..*(..))")
    public void droneExecutionPointcut() throws UnsupportedOperationException {
    }

    @Pointcut("(@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public void httpMethodPointcut() throws UnsupportedOperationException {
    }

    @Around( "droneExecutionPointcut() && httpMethodPointcut()")
    public Object handleeLogging(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("#REQUEST_START [" + request.get().getRequestURI() + "] Started With Parameters " +
                Arrays.toString(joinPoint.getArgs()) + " #REQUEST_END");
        Object result = joinPoint.proceed();
        log.info("#RESPONSE_START [" + request.get().getRequestURI() + "] Ended Successfully With Response " +
                result + " #RESPONSE_END");
        return result;
    }




}

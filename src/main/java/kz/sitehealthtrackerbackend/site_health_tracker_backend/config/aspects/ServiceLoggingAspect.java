package kz.sitehealthtrackerbackend.site_health_tracker_backend.config.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class ServiceLoggingAspect {

    @Pointcut("execution(* kz.sitehealthtrackerbackend.site_health_tracker_backend.service.impls.*.*(..))")
    public void allServiceMethodsPointcut() {
    }

//    @Around("allServiceMethodsPointcut()")
//    public Object logMethodEntryAndExit(ProceedingJoinPoint joinPoint) throws Throwable {
//        String methodName = joinPoint.getSignature().getName();
//
//        Object[] args = joinPoint.getArgs();
//        log.info("Вызван метод {} с аргументами {}", methodName, Arrays.toString(args));
//        Object result = joinPoint.proceed();
//        log.info("Метод {} вернул: {}", methodName, result);
//        return result;
//    }

    @Before("allServiceMethodsPointcut()")
    public void logMethodEntry(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Вызван метод {} с аргументами {}", methodName , Arrays.toString(args));
    }

    @AfterReturning(pointcut = "allServiceMethodsPointcut()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        if (result == null) {
            log.info("Метод {} завершился успешно", methodName);
        } else {
            log.info("Метод {} вернул: {}", methodName, result);
        }
    }

    @AfterThrowing(pointcut = "allServiceMethodsPointcut()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        log.error("Выброс исключения в методе {}: ", methodName , ex);
    }
}

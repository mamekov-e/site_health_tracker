package kz.sitehealthtrackerbackend.site_health_tracker_backend.config.aspects;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class RestControllerAspect {

    @Pointcut("execution(* kz.sitehealthtrackerbackend.site_health_tracker_backend.web.controller.*.*(..)) && " +
            "(requestGetMappingMethods() ||" +
            "requestPostMappingMethods() ||" +
            "requestPutMappingMethods() ||" +
            "requestDeleteMappingMethods())")
    public void requestAllMappingMethods() {
    }

    @Pointcut("execution(* kz.sitehealthtrackerbackend.site_health_tracker_backend.config.exception.handler.*.*(..)) && " +
            "@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
    public void responseEntityMethods() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void requestGetMappingMethods() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void requestPostMappingMethods() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void requestPutMappingMethods() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void requestDeleteMappingMethods() {
    }


    @Before("requestAllMappingMethods()")
    public void controllerRequestAfterReturningLogAdvice(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Map<String, Object> parameters = getParameters(joinPoint);

        log.info("==> Путь(и): {}, Метод(ы): {}, Аргументы: {} ",
                request.getRequestURI(), request.getMethod(), parameters);
    }

    @AfterReturning(pointcut = "requestAllMappingMethods() || responseEntityMethods()", returning = "entity")
    public void exceptionHandlerLogAdvice(ResponseEntity<?> entity) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("<== Путь(и): {}, Метод(ы): {}, Ответ: {}",
                request.getRequestURL(), request.getMethod(), entity.getBody());
    }

    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        HashMap<String, Object> map = new HashMap<>();
        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], joinPoint.getArgs()[i]);
        }
        return map;
    }

}

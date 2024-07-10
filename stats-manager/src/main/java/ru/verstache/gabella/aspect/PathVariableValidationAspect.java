package ru.verstache.gabella.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import ru.verstache.gabella.exception.ReportListSizeException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
public class PathVariableValidationAspect {

    @Before("@annotation(ValidReportListSize)")
    public void validatePathVariable(JoinPoint joinPoint) {
        Method method = getMethodFromJoinPoint(joinPoint);
        if (method != null) {
            Object[] args = joinPoint.getArgs();
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                PathVariable pathVariableAnnotation = parameters[i].getAnnotation(PathVariable.class);
                if (pathVariableAnnotation != null && parameters[i].getType().equals(Integer.class)) {
                    Integer amount = (Integer) args[i];
                    if (amount < 1) {
                        throw new ReportListSizeException("Report list size should be greater than or equal to 1.");
                    }
                }
            }
        }
    }

    private Method getMethodFromJoinPoint(JoinPoint joinPoint) {
        try {
            String methodName = joinPoint.getSignature().getName();
            Class<?>[] parameterTypes = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getParameterTypes();
            return joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}


package codezap.global.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class MethodExecutionTimeAspect {

    @Around("execution(* codezap..*(..)) && " +
            "!within(codezap.global.logger.*) && " +
            "!within(codezap.global.exception.*)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!log.isInfoEnabled()) {
            return joinPoint.proceed();
        }

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        long executionTimeMillis = endTime - startTime;

        String className = joinPoint.getSignature()
                .getDeclaringType()
                .getSimpleName();
        String methodName = joinPoint.getSignature()
                .getName();

        log.info("{}.{} 실행 {}ms", className, methodName, executionTimeMillis);

        return result;
    }
}

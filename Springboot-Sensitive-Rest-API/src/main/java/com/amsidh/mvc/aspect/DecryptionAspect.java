
package com.amsidh.mvc.aspect;

import com.amsidh.mvc.annotation.DecryptBeforeValidation;
import com.amsidh.mvc.model.PersonRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class DecryptionAspect {

    @Around("@annotation(decryptBeforeValidation)")
    public Object decryptAndValidate(ProceedingJoinPoint joinPoint, DecryptBeforeValidation decryptBeforeValidation) throws Throwable {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof PersonRequest personRequest) {
                personRequest.decryptSensitiveData(); // Decrypt sensitive data
            }
        }
        // Proceed with validation performed by @Valid in the controller
        return joinPoint.proceed(args);
    }
}

package com.amsidh.mvc.aspect;

import com.amsidh.mvc.annotation.Decrypt;
import com.amsidh.mvc.util.CryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Aspect
@Component
@Slf4j
public class DecryptAspect {

    @Around("execution(* com.amsidh.mvc.controller.*.*(..))")
    public Object decryptAndValidate(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            decryptFields(arg);
        }

        return joinPoint.proceed(args);
    }

    private void decryptFields(Object arg) {
        Field[] fields = arg.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Decrypt.class)) {
                field.setAccessible(true);
                try {
                    String encryptedValue = (String) field.get(arg);
                    if (encryptedValue != null) {
                        String decryptedValue = CryptoUtil.decrypt(encryptedValue);
                        field.set(arg, decryptedValue);
                    }
                } catch (Exception e) {
                    log.error("Error accessing field value {}", e.getMessage(), e);
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }
}



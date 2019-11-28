package com.expedia.content.media.processing.pipeline.retry;

import com.expedia.content.media.processing.pipeline.util.FormattedLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * Implementation of the aspect that powers the {@code RetryableMethod} annotation.
 *
 * AspectJ will instantiate this class when the aspect is needed. This means that even if
 * we instantiate this in Spring AspectJ will not know about this and it will create its own
 * instance to implement the aspect. This is why we use the {@code @Configurable} annotation
 * as it allows us to inject dependencies even for objects not knows by Spring at start time.
 *
 * From Spring we need to inject the RetryTemplate which will do the retrying.
 *
 * @see RetryableMethod
 * @see Configurable
 * @see RetryTemplate
 */
@Aspect
@Configurable
public class RetryableAspect {

    private static final FormattedLogger LOGGER = new FormattedLogger(RetryableAspect.class);

    private RetryTemplate retryTemplate;

    public RetryableAspect() {
        LOGGER.info("Instantiating @RetryableMethod aspect from AspectJ.");
        this.retryTemplate = defaultNoRetryTemplate();
    }

    @Around("execution(* *.*(..)) && @annotation(com.expedia.content.media.processing.pipeline.retry.RetryableMethod)")
    public Object retryAdvice(final ProceedingJoinPoint joinPoint) throws Throwable {
        LOGGER.debug("Execute with retry joinPoint={}", joinPoint.toLongString());
        return retryTemplate.execute(new RetryCallback<Object, Throwable>() {
            @Override
            public Object doWithRetry(RetryContext retryContext) throws Throwable {
                return joinPoint.proceed();
            }
        });
    }

    @Autowired
    public void setRetryTemplate(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    private static RetryTemplate defaultNoRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(new NeverRetryPolicy());
        return retryTemplate;
    }
}

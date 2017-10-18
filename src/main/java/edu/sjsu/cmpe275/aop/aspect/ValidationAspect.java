package edu.sjsu.cmpe275.aop.aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;  // if needed
import org.aspectj.lang.annotation.Around;  // if needed
import org.aspectj.lang.ProceedingJoinPoint; // if needed
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

import java.util.Arrays;

@Aspect
@Order(1)
public class ValidationAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

    /**
     * Advice to validate value of each parameter of each method in BlogService.
     * @param joinPoint
     * @throws Exception
     */
	@Before("execution(public * edu.sjsu.cmpe275.aop.BlogService.*(..))")
	public void validationAdvice(JoinPoint joinPoint) throws Exception{
		//System.out.printf("Prior to the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		Object[] arguments = joinPoint.getArgs();

		if (joinPoint.getSignature().getName().equals("commentOnBlog"))
            arguments = Arrays.copyOf(joinPoint.getArgs(), arguments.length - 1);

        for (Object arg : arguments) {
			if( !isValidUserId((String) arg) ) throw new IllegalArgumentException("Invalid userId");
		}

		if(joinPoint.getSignature().getName().equals("commentOnBlog"))
			if( !isValidMessage((String) joinPoint.getArgs()[2])) throw new IllegalArgumentException("Invalid message length");

	}

    /**
     * Validates userId.
     * @param userId to be validated
     * @return
     */
	private boolean isValidUserId(String userId) {
		if (userId != null)
			return (userId.length() >= 3 && userId.length() <= 16 ? true : false );

		return false;
	}

    /**
     * Validated message.
     * @param message
     * @return
     */
	private boolean isValidMessage(String message) {
		if (message != null)
			return (message.length() > 0 && message.length() <= 100 ? true : false );

		return false;
	}
}
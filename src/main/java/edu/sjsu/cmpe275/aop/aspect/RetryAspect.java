package edu.sjsu.cmpe275.aop.aspect;
import edu.sjsu.cmpe275.aop.Blog;
import edu.sjsu.cmpe275.aop.exceptions.AccessDeniedExeption;
import edu.sjsu.cmpe275.aop.exceptions.NetworkException;
import org.aspectj.lang.annotation.Aspect;  // if needed
import org.aspectj.lang.annotation.Around;  // if needed
import org.aspectj.lang.ProceedingJoinPoint; // if needed
import org.springframework.core.annotation.Order;

@Aspect
@Order(0)
public class RetryAspect {

    /**
     * Advice to check for network exception around each method in BlogService.
     * @param joinPoint
     * @throws Throwable
     */
	@Around("execution(public * edu.sjsu.cmpe275.aop.BlogService.*(..))")
	public void networkAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		//System.out.printf("Prior to the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		int retries = 0;
		while (retries < 3) {
			try {
				joinPoint.proceed();
                if (retries > 0)
                    System.out.println("The " + joinPoint.getSignature().getName() +" method executed w/o any network error in Number of retires: "+retries);
				break;
			} catch (NetworkException e) {
                if(retries==2)
                    throw  new NetworkException(e.getMessage());
			} catch(AccessDeniedExeption e) {
                throw new AccessDeniedExeption(e.getMessage());
            } catch(IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            }

			retries++;
		}
	}

}
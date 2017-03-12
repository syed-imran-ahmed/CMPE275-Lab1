package edu.sjsu.cmpe275.aop.aspect;
import org.aspectj.lang.annotation.Aspect;  // if needed

import edu.sjsu.cmpe275.aop.Profile;
import edu.sjsu.cmpe275.aop.exceptions.AccessDeniedExeption;
import edu.sjsu.cmpe275.aop.exceptions.NetworkException;

import org.aspectj.lang.annotation.Around;  // if needed

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint; // if needed

@Aspect
public class RetryAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     * @throws Throwable 
     */

	@Around("execution(public void edu.sjsu.cmpe275.aop.ProfileService.*(..))")
	public void retryAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.printf("Around advice for %s\n", joinPoint.getSignature().getName());
		
		int retries = 0;
		Exception networkFailureException = null;
		
		while(retries<=2)
		{
			try{
				joinPoint.proceed();
				System.out.println("The function executed w/o any network error in Number of retires: "+retries);
				break;
			}
			catch(AccessDeniedExeption e)
			{
				throw new AccessDeniedExeption(e.toString());
			}
			catch(Exception ex)
			{
				networkFailureException=(NetworkException)ex;
				if(retries==2)
					throw networkFailureException;
			}
			retries++;
		}
	}
	
	@Around("execution(public * edu.sjsu.cmpe275.aop.ProfileService.readProfile(..))")
	public Object retryAdviceForReadProfile(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.printf("Around advice for %s\n", joinPoint.getSignature().getName());
		Object returnValue = null;
		int retries = 0;
		Exception networkFailureException = null;
		
		while(retries<=2)
		{
			try{
				returnValue = (Profile)joinPoint.proceed();
				System.out.println("The function executed w/o any network error in Number of retires: "+retries);
				break;
			}
			catch(AccessDeniedExeption e)
			{
				throw new AccessDeniedExeption(e.toString());
			}
			catch(Exception ex)
			{
				networkFailureException=(NetworkException)ex;
				if(retries==2)
					throw networkFailureException;
			}
			retries++;
		}
		return returnValue;
	}
}
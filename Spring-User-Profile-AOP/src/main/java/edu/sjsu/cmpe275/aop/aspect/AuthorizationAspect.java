package edu.sjsu.cmpe275.aop.aspect;

import org.springframework.beans.factory.annotation.Autowired;  // if needed

import edu.sjsu.cmpe275.aop.Profile;
import edu.sjsu.cmpe275.aop.ProfileService;
import edu.sjsu.cmpe275.aop.exceptions.AccessDeniedExeption;

import org.aspectj.lang.annotation.Aspect;  // if needed

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


import org.aspectj.lang.JoinPoint;  // if needed
import org.aspectj.lang.annotation.After;  // if needed
import org.aspectj.lang.annotation.Before;  // if needed


@Aspect
public class AuthorizationAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advises as needed.
     */

	Map<String, HashSet<String>> sharedUserProfile = new HashMap<String, HashSet<String>>();
	
	HashSet<String> userProfiles;
	
	@Autowired ProfileService profileService;
	
	@Before("execution(public * edu.sjsu.cmpe275.aop.ProfileService.readProfile(..)) && args(userId,profileUserId)")
	public void readProfileAdvice(JoinPoint joinPoint, String userId, String profileUserId) throws Exception {
		System.out.printf("Before the executuion of the method %s\n", joinPoint.getSignature().getName());
		String exceptionMessage = "The user "+userId+" does not have read access to the profile "+profileUserId;
		
		if(userId.equals(profileUserId))
			return;
		
		if(sharedUserProfile.containsKey(userId))
		{
			if(!sharedUserProfile.get(userId).contains((profileUserId)))
			{
				throw new AccessDeniedExeption(exceptionMessage);
			}
		}
		else
		{
			throw new AccessDeniedExeption(exceptionMessage);
		}
		
	}
	
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.ProfileService.shareProfile(..)) && args(userId,profileUserId,targetUserId)")
	public void beforeShareProfileAdvice(JoinPoint joinPoint, String userId, String profileUserId, String targetUserId) throws AccessDeniedExeption {
		System.out.printf("After the executuion of the method %s\n", joinPoint.getSignature().getName());
		String ErrorMsgString = userId+" cannot share "+profileUserId+" profile with "+targetUserId;
		if(userId==null || profileUserId==null || targetUserId==null)
			return;
		else if(userId=="" || profileUserId=="" || targetUserId=="")
			return;
			
		if(userId.equals(profileUserId) && profileUserId.equals(targetUserId))
			return;
		
		if(profileUserId.equals(targetUserId))
			throw new AccessDeniedExeption(ErrorMsgString);
		
		if((sharedUserProfile.get(userId)!=null && sharedUserProfile.get(userId).contains(profileUserId)) || (userId.equals(profileUserId)))
		{
			if(!sharedUserProfile.containsKey(targetUserId))
			{
				userProfiles = new HashSet<String>();
				userProfiles.add(profileUserId);
				sharedUserProfile.put(targetUserId,userProfiles);
			}
			else
			{	
				userProfiles = sharedUserProfile.get(targetUserId);
				userProfiles.add(profileUserId);
				sharedUserProfile.put(targetUserId, userProfiles);
			}
		}
		else
		{
			throw new AccessDeniedExeption(ErrorMsgString);
		}		
	}
	
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.ProfileService.unshareProfile(..)) && args(userId, targetUserId)")
	public void beforeUnshareProfileAdvice(JoinPoint joinPoint, String userId, String targetUserId) throws AccessDeniedExeption {
		System.out.printf("Before the executuion of the method %s\n", joinPoint.getSignature().getName());
		
		if(userId==null || targetUserId==null)
			return;
		else if(userId==""  || targetUserId=="")
			return;
		
		if(userId.equals(targetUserId))
			return;
		
		if(!sharedUserProfile.containsKey(targetUserId))
			throw new AccessDeniedExeption("There is no such user as "+targetUserId);
		else
		{
			if(sharedUserProfile.get(targetUserId).contains(userId))
			{
				userProfiles = sharedUserProfile.get(targetUserId);
				userProfiles.remove(userId);
				sharedUserProfile.put(targetUserId, userProfiles);
				System.out.println("The profile "+userId+" is now unshared with "+targetUserId);
			}
			else
				throw new AccessDeniedExeption("The profile "+userId+" is not shared with "+targetUserId);
		}
	}
	
	
	
	
	
	/*@After("execution(public void edu.sjsu.cmpe275.aop.ProfileService.shareProfile(..))")
	public void dummyAfterAdvice(JoinPoint joinPoint) {
		System.out.printf("After the executuion of the method %s\n", joinPoint.getSignature().getName());
		//profileService.shareProfile();
	}
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.ProfileService.unshareProfile(..))")
	public void dummyBeforeAdvice(JoinPoint joinPoint) {
		System.out.printf("Before the executuion of the method %s\n", joinPoint.getSignature().getName());
	}
	*/
}

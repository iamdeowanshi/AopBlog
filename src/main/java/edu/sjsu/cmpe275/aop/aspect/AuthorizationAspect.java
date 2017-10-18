package edu.sjsu.cmpe275.aop.aspect;

import edu.sjsu.cmpe275.aop.exceptions.AccessDeniedExeption;

import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.beans.factory.annotation.Autowired;  // if needed

import edu.sjsu.cmpe275.aop.BlogService;

import org.aspectj.lang.annotation.Aspect;  // if needed
import org.aspectj.lang.JoinPoint;  // if needed
import org.aspectj.lang.annotation.Before;  // if needed
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


@Aspect
@Order(2)
public class AuthorizationAspect {

	@Autowired BlogService blogService;

	private Map<String, HashSet<String>> sharedUserBlogs = new HashMap<String, HashSet<String>>();
	private HashSet<String> userBlogs;

	/**
	 * Authorization advice for reading a blog.
	 * @param joinPoint
	 * @throws AccessDeniedExeption
	 */
	@Before("execution(public * edu.sjsu.cmpe275.aop.BlogService.readBlog(..))")
	public void beforeReadAdvice(JoinPoint joinPoint) throws AccessDeniedExeption {
		String userId = (String) joinPoint.getArgs()[0];
		String blogUserId = (String) joinPoint.getArgs()[1];

		if (userId == blogUserId)
			return;

		if (!hasAccess(userId, blogUserId))
			throw new AccessDeniedExeption(userId + " not authorized to read " + blogUserId + "'s blog");

		System.out.printf("User %s has access to read user %s blog\n", userId, blogUserId);
	}

	/**
	 * Authorization advice for commenting on blog.
	 * @param joinPoint
	 * @throws AccessDeniedExeption
	 */
	@Before("execution(public void edu.sjsu.cmpe275.aop.BlogService.commentOnBlog(..))")
	public void beforeCommentAdvice(JoinPoint joinPoint) throws AccessDeniedExeption {
		String userId = (String) joinPoint.getArgs()[0];
		String blogUserId = (String) joinPoint.getArgs()[1];

		if (userId == blogUserId)
			return;

		if (!hasAccess(userId, blogUserId))
			throw new AccessDeniedExeption(userId + " not authorized to comment " + blogUserId + "'s blog");

		System.out.printf("User %s has access to comment on user %s blog\n", userId, blogUserId);
	}

	/**
	 * Authorization advice for before sharing a blog.
	 * @param joinPoint
	 * @throws AccessDeniedExeption
	 */
	@Before("execution(public void edu.sjsu.cmpe275.aop.BlogService.shareBlog(..))")
	public void beforeShareAdvice(JoinPoint joinPoint) throws AccessDeniedExeption {
		String userId = (String) joinPoint.getArgs()[0];
		String blogUserId = (String) joinPoint.getArgs()[1];
		String targetId = (String) joinPoint.getArgs()[2];

		if (userId == blogUserId) {
			System.out.printf("User %s has access to share %s's blog with user %s\n",userId, blogUserId, targetId);
			return;
		}

		if (blogUserId == targetId)
			return;

		if (!hasAccess(userId, blogUserId))
			throw new AccessDeniedExeption(userId + " not authorized to share " + blogUserId + "'s blog");

		System.out.printf("User %s has access to share %s's blog with user %s\n",userId, blogUserId, targetId);
	}

	/**
	 * Check authorization before unsharing blog.
	 * @param joinPoint
	 * @throws AccessDeniedExeption
	 */
	@Before("execution(public void edu.sjsu.cmpe275.aop.BlogService.unshareBlog(..))")
	public void beforeUnsharedAdvice(JoinPoint joinPoint) throws AccessDeniedExeption {
		//System.out.printf("Before the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		String userId = (String) joinPoint.getArgs()[0];
		String blogUserId = (String) joinPoint.getArgs()[1];

		if (userId == blogUserId)
			return;

		if (!hasAccess(blogUserId, userId))
			throw new AccessDeniedExeption(userId + " is not sharing his/her blog with " + blogUserId);

		System.out.printf("User %s has access to unshares his/her own blog with user %s\n", userId, blogUserId);
	}

	/**
	 * Removes access after unshareBlog executed successfully.
	 * @param joinPoint
	 */
	@AfterReturning("execution(public void edu.sjsu.cmpe275.aop.BlogService.unshareBlog(..))")
	public void afterUnshared(JoinPoint joinPoint) {
		String userId = (String) joinPoint.getArgs()[0];
		String blogUserId = (String) joinPoint.getArgs()[1];

		unsharedBlog(blogUserId, userId);

		System.out.printf("User %s successfully unshared his/her own blog with user %s\n", userId, blogUserId);
	}

	/**
	 * Adds access after share method executes successfully.
	 * @param joinPoint
	 */
	@AfterReturning("execution(public void edu.sjsu.cmpe275.aop.BlogService.shareBlog(..))")
	public void  afterShare(JoinPoint joinPoint) {
		String userId = (String) joinPoint.getArgs()[0];
		String blogUserId = (String) joinPoint.getArgs()[1];
		String targetId = (String) joinPoint.getArgs()[2];

		shareBlog(blogUserId, targetId);


		System.out.printf("User %s successfully shared %s's blog with user %s\n",userId, blogUserId, targetId);
	}

	/**
	 * Adding user to database.
	 * @param from
	 * @param to
	 */
	private void shareBlog(String from, String to) {
		userBlogs = new HashSet<String>();
		userBlogs.add(from);
		sharedUserBlogs.put(to, userBlogs);
	}

	/**
	 * Removing user access from database.
	 * @param blogfrom
	 * @param blogOf
	 */
	private void unsharedBlog(String blogfrom, String blogOf) {
		userBlogs = sharedUserBlogs.get(blogfrom);
		userBlogs.remove(blogOf);
		sharedUserBlogs.put(blogfrom, userBlogs);
	}

	/**
	 * Checks for access on blog.
	 * @param requester
	 * @param provider
	 * @return
	 */
	private boolean hasAccess(String requester, String provider) {
		if (!sharedUserBlogs.containsKey(requester)) 
			return false;
		
		if (!sharedUserBlogs.get(requester).contains(provider))
			return false;
		
		return true;
	}
	
}

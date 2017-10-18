package edu.sjsu.cmpe275.aop;

import edu.sjsu.cmpe275.aop.exceptions.AccessDeniedExeption;
import edu.sjsu.cmpe275.aop.exceptions.NetworkException;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BlogServiceTest {

    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
    BlogService blogService = (BlogService) ctx.getBean("blogService");
    /***
     * These are dummy test cases. You may add test cases based on your own need.
     */

    @Test
    public void testOne() throws NetworkException, AccessDeniedExeption{
        blogService.shareBlog("Alice", "Alice", "Bob");
        blogService.shareBlog("Bob", "Alice", "Tom");
    }

    @Test
    public void testCaseN() throws NetworkException, AccessDeniedExeption {
        try {
            blogService.shareBlog("Alice", "Alice", "alex");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            //blogService.readBlog("alex", "Alice");
        }
    }

    @Test
    public void testCase5() throws NetworkException, AccessDeniedExeption {
        blogService.commentOnBlog("Bob", "Alice", "Nice work!Nice work!Nice work!Nice work!Nice work!Nice work!Nice work!Nice work!Nice");
    }
    @Test
    public void testCase6() throws Exception {
        blogService.shareBlog("Alice", "Alice", null);
        //blogService.commentOnBlog(null, "Alice", "he");
    }
}
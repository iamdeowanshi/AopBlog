CMPE275- LAB1-AOP BlogService
===================


###Goal
The purpose of this assignment is to implement advices for BlogService methods.



Description
-------------

Application should implement following requirements and throw appropriate exception. 

> **Requirement:**

> 1. Once can share his blog with anybody.
> 2. One can only read blogs that are shared with him, or his own blog. In any other case, an AccessDeniedExeption is thrown.
> 3. If Alice shares her blog with Bob, Bob can further share Alice’s blog with Carl. If Alice attempts to share Bob’s blog with Carl while Bob’s blog is not shared with Alice in the first place, Alice gets an AccessDeniedExeption.
> 4. One can only unshare his own blog. When unsharing a blog with Bob that the blog is not shared by any means with Bob in the first place, the operation throws an AccessDeniedExeption. 
> 5. Both sharing and unsharing of Alice’s blog with Alice have no effect; i.e. Alice always has access to her own blog, and can share and unshare with herself without encountering any exception, even these operations do not take any effect.
> 6. For all the methods, every parameters for user ID must of a string of at least 3 and maximum 16 unicode characters, or an IllegalArgumentException is thrown.  
> 7. For any blog that is shared with Alice, she can comment on it with a message that is up to 100 unicode characters. If the message is longer than 100, or null, or empty, an IllegalArgumentException is thrown.
> 8. All the methods in BlogService can also run into network failures, in which case, an NetworkException will be thrown, and the method takes no effect at all. Actually, since network failure happens relatively frequently, you are asked to add the feature to automatically retry for up to two times for a network failure (indicated by an NetworkException). Please note the two retries are in addition to the original failed invocation; if you still encounter 

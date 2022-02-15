# Spring 5 - JSF 2.3 - Spring Security
## Skeleton

This is a repository intended to serve as a starting point if you want to bootstrap a Spring 5 project with JSF 2.3 and Maven.

It provides several .xhtml pages to test the roles and check that only an authenticated user can access to them. You can test the endpoints using the credentials from the user john.doe/1234 whose role is USER and the user jane.doe/5678 whose role is ADMIN. Spring Security has been configure to use an inMemoryAuthentication method.
```
/login - Demonstrates the login page where an unauthenticated user is redirect.
/dashboard - Users with role ROLE_USER or ROLE_ADMIN.
/setting - Users with role ROLE_ADMIN.
```

Include supports for Spring Security, Spring AOP, Spring Schedule and Spring Data JPA.

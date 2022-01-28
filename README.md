# Spring 5 - JSF 2.3 - Spring Security
## Skeleton

This is a repository intended to serve as a starting point if you want to bootstrap a Spring 5 project with JSF 2.3 and Maven.

It provides several .xhmtl pages to test the roles and check that only an authenticated user can access to them. You can test the endpoints using the credentials from the user john.doe/1234 whose role is USER and the user jane.doe/5678 whose role is ADMIN. Both users are configured through inMemoryAuthentication.

/login - Demonstrates the login page where the user no authenticated are redirect.
/dashboard - Users who have the role ROLE_USER.
/setting - Users who have the role ROLE_ADMIN.

# Simple contact management tool in Spring and Vaadin

## Used techologies
* [Spring framework](http://spring.io/) 3.2.13.RELEASE
* [Vaadin](https://vaadin.com/) 7.4.1
* [Spring Vaadin Integration](http://vaadin.xpoft.ru/) 3.1
* [PostgreSQL](http://www.postgresql.org/) 9.3
* [Hibernate](http://hibernate.org/) 4.2.5.Final
* [Maven](http://maven.apache.org/what-is-maven.html) 3.1

## UI design patterns
* [Model-View-Presenter](https://vaadin.com/web/magi/home/-/blogs/model-view-presenter-pattern-with-vaadin)

## How to start
Create new empty database `contacts` on your PostreSQL server. 
Change username and password in `src/main/resources/config.properties`.

* build: `mvn install`
* run: `mvn jetty:run`

Initialize table `country` with some data before using the application: run script `db/countries.sql`

* navigate to [http://localhost:8080/spring-vaadin-contacts/](http://localhost:8080/spring-vaadin-contacts/)

## Author
Martin Mecera
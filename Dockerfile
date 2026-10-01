FROM tomcat:9-jdk21-temurin-noble AS build
WORKDIR /build
COPY LibraryManagementSystem/src ./src
COPY LibraryManagementSystem/web ./web
COPY LibraryManagementSystem/lib ./lib
RUN mkdir -p web/WEB-INF/classes web/WEB-INF/lib
RUN find src -name "*.java" > sources.txt && javac -cp "lib/mysql-connector-j-9.5.0.jar:lib/servlet-api.jar:lib/gson-2.10.1.jar" -d web/WEB-INF/classes @sources.txt
RUN cp lib/mysql-connector-j-9.5.0.jar web/WEB-INF/lib/
RUN cp lib/gson-2.10.1.jar web/WEB-INF/lib/
RUN jar -cf LibraryManagement.war -C web .
FROM tomcat:9-jdk21-temurin-noble
WORKDIR /usr/local/tomcat
RUN rm -rf webapps/ROOT webapps/docs webapps/examples webapps/host-manager webapps/manager
COPY --from=build /build/LibraryManagement.war webapps/LibraryManagement.war
EXPOSE 8080

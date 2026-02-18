@echo off
echo ========================================
echo Deploying to Tomcat
echo ========================================

REM Create WEB-INF structure
if not exist "web\WEB-INF" mkdir web\WEB-INF
if not exist "web\WEB-INF\classes" mkdir web\WEB-INF\classes
if not exist "web\WEB-INF\lib" mkdir web\WEB-INF\lib

echo Step 1: Copying compiled classes...
robocopy src\com web\WEB-INF\classes\com /E /NFL /NDL /NJH /NJS

echo Step 2: Copying JAR files...
copy lib\mysql-connector-j-9.5.0.jar web\WEB-INF\lib\ > nul
copy lib\gson-2.10.1.jar web\WEB-INF\lib\ > nul

echo Step 3: Creating WAR file...
cd web
jar -cvf LibraryManagement.war *

echo Step 4: Deploying to Tomcat...
copy LibraryManagement.war C:\apache-tomcat-9.0.XX\webapps\ > nul

echo.
echo ========================================
echo Deployment Complete!
echo ========================================
echo.
echo Next steps:
echo 1. Start Tomcat: C:\Program Files\apache-tomcat-9.0.113\bin\startup.bat
echo 2. Open browser: http://localhost:8080/LibraryManagement/
echo.
pause
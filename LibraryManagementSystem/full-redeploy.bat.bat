@echo off
echo ========================================
echo Full Recompile and Redeploy
echo ========================================
echo.

REM Step 1: Recompile everything
echo [1/8] Recompiling all Java files...
cd src
javac -cp ".;../lib/mysql-connector-j-9.5.0.jar;../lib/servlet-api.jar;../lib/gson-2.10.1.jar" com/library/util/*.java com/library/model/*.java com/library/dao/*.java com/library/service/*.java com/library/servlet/*.java

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)
echo    Compilation successful!

REM Step 2: Copy classes to WEB-INF
echo [2/8] Copying compiled classes...
cd ..
robocopy src\com web\WEB-INF\classes\com /E /NFL /NDL /NJH /NJS

REM Step 3: Copy JARs
echo [3/8] Copying JAR files...
copy lib\mysql-connector-j-9.5.0.jar web\WEB-INF\lib\ >nul 2>&1
copy lib\gson-2.10.1.jar web\WEB-INF\lib\ >nul 2>&1

REM Step 4: Create WAR
echo [4/8] Creating WAR file...
cd web
if exist LibraryManagement.war del LibraryManagement.war
jar -cvf LibraryManagement.war * >nul

if not exist LibraryManagement.war (
    echo ERROR: WAR file not created!
    pause
    exit /b 1
)
echo    WAR file created successfully!

REM Step 5: Stop Tomcat
echo [5/8] Stopping Tomcat...
taskkill /F /IM "java.exe" /FI "WINDOWTITLE eq Tomcat" 2>nul
timeout /t 3 /nobreak >nul

REM Step 6: Clean old deployment
echo [6/8] Cleaning old deployment...
if exist "C:\Program Files\apache-tomcat-9.0.113\webapps\LibraryManagement" (
    rmdir /S /Q "C:\Program Files\apache-tomcat-9.0.113\webapps\LibraryManagement" 2>nul
)
if exist "C:\Program Files\apache-tomcat-9.0.113\webapps\LibraryManagement.war" (
    del "C:\Program Files\apache-tomcat-9.0.113\webapps\LibraryManagement.war" 2>nul
)

REM Step 7: Deploy new WAR
echo [7/8] Deploying new WAR file...
copy LibraryManagement.war "C:\Program Files\apache-tomcat-9.0.113\webapps\" >nul

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to copy WAR file! Run as Administrator.
    pause
    exit /b 1
)
echo    WAR deployed successfully!

REM Step 8: Start Tomcat
echo [8/8] Starting Tomcat...
cd "C:\Program Files\apache-tomcat-9.0.113\bin"
start "" startup.bat

echo.
echo ========================================
echo Deployment Complete!
echo ========================================
echo.
echo Tomcat is starting... Please wait 30 seconds
echo Then open: http://localhost:8080/LibraryManagement/
echo.
echo Press any key to open the application...
pause >nul

start http://localhost:8080/LibraryManagement/
```

**Right-click → Run as Administrator**

---

## 🎯 After Running the Script

Wait 30 seconds, then check the **new Tomcat logs**. You should now see:
```
LoginServlet initialized
BookServlet initialized
RegisterServlet initialized
IssueBookServlet initialized
Database connected successfully!
```

---

## ✅ Verification

After deployment, test these URLs:

### 1. Tomcat Welcome Page
```
http://localhost:8080
```
Should show Tomcat page ✅

### 2. Your Application
```
http://localhost:8080/LibraryManagement/
```
Should show login page ✅

### 3. Test Books API
```
http://localhost:8080/LibraryManagement/api/books
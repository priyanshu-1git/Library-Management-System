@echo off
echo ========================================
echo Compiling Library Management System
echo For Web Deployment
echo ========================================
echo.

cd src

echo Step 1: Compiling Util...
javac -cp ".;../lib/mysql-connector-j-9.5.0.jar" com/library/util/*.java

echo Step 2: Compiling Models...
javac -cp ".;../lib/mysql-connector-j-9.5.0.jar" com/library/model/*.java

echo Step 3: Compiling DAOs...
javac -cp ".;../lib/mysql-connector-j-9.5.0.jar" com/library/dao/*.java

echo Step 4: Compiling Services...
javac -cp ".;../lib/mysql-connector-j-9.5.0.jar" com/library/service/*.java

echo Step 5: Compiling Servlets...
javac -cp ".;../lib/mysql-connector-j-9.5.0.jar;../lib/servlet-api.jar;../lib/gson-2.10.1.jar" com/library/servlet/*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Compilation Successful!
    echo ========================================
) else (
    echo.
    echo ========================================
    echo Compilation Failed!
    echo ========================================
    pause
    exit /b 1
)

echo.
pause
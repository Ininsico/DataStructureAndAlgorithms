@echo off
TITLE Adaptive Recommendation Engine Launcher

echo ==================================================
echo      Adaptive Recommendation Engine Launcher
echo ==================================================
echo.

:: Check for Java
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Java is not found in your PATH.
    echo Please install JDK 21+ and try again.
    pause
    exit /b
)
java -version
echo.

:: Check for Maven
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Maven (mvn) is not found in your PATH.
    echo.
    echo This application requires Apache Maven to manage JavaFX dependencies.
    echo 1. Download Maven from https://maven.apache.org/download.cgi
    echo 2. Extract it and add the 'bin' directory to your System PATH environment variable.
    echo 3. Restart this script.
    echo.
    pause
    exit /b
)

echo [INFO] Found Maven. compiling and starting application...
echo.
call mvn clean javafx:run

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Build failed. Please check the error messages above.
    pause
)

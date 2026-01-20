@echo off
echo ========================================
echo Maven Installation Script for Windows
echo ========================================
echo.

REM Check if Maven is already installed
where mvn >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo Maven is already installed!
    mvn -version
    goto :END
)

echo Maven is not installed.
echo.
echo Please choose installation method:
echo.
echo 1. Install with Chocolatey (if you have it)
echo    Command: choco install maven
echo.
echo 2. Manual Download from Apache
echo    URL: https://maven.apache.org/download.cgi
echo    Download: apache-maven-3.9.6-bin.zip
echo    Extract to: C:\Program Files\Apache\maven
echo    Add to PATH: C:\Program Files\Apache\maven\bin
echo.
echo 3. Use Maven Wrapper (included in this project)
echo    Just run: cd backend && mvnw spring-boot:run
echo.

:END
echo.
echo After installing Maven, run:
echo    cd backend
echo    mvn spring-boot:run
echo.
pause

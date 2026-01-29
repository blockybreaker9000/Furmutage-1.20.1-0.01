@echo off
REM Build Furmutage JAR using Adoptium JDK
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.1.8-hotspot"
call gradlew.bat build
if %ERRORLEVEL% equ 0 (
    echo.
    echo Build succeeded. JAR is in: build\libs\
    dir /b build\libs\*.jar 2>nul | findstr /v sources
) else (
    echo Build failed. Check errors above.
)
pause

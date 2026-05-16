@echo off
setlocal enabledelayedexpansion

set MAVEN_VERSION=3.9.6
set MAVEN_HOME=%USERPROFILE%\.mvnw\apache-maven-%MAVEN_VERSION%
set MAVEN_ZIP=%USERPROFILE%\.mvnw\apache-maven-%MAVEN_VERSION%-bin.zip
set MAVEN_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip

if exist "%MAVEN_HOME%\bin\mvn.cmd" goto :run_maven

echo.
echo =========================================================
echo  Maven Wrapper - Descargando Apache Maven %MAVEN_VERSION%
echo =========================================================
echo.

if not exist "%USERPROFILE%\.mvnw" mkdir "%USERPROFILE%\.mvnw"

powershell -Command ^
  "Write-Host 'Descargando Maven, espera un momento...'; " ^
  "$ProgressPreference = 'SilentlyContinue'; " ^
  "Invoke-WebRequest -Uri '%MAVEN_URL%' -OutFile '%MAVEN_ZIP%' -UseBasicParsing; " ^
  "Write-Host 'Extrayendo...'; " ^
  "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%USERPROFILE%\.mvnw' -Force; " ^
  "Remove-Item '%MAVEN_ZIP%' -ErrorAction SilentlyContinue; " ^
  "Write-Host 'Maven listo.'"

if errorlevel 1 (
    echo.
    echo ERROR: No se pudo descargar Maven. Verifica tu conexion a internet.
    exit /b 1
)

:run_maven
"%MAVEN_HOME%\bin\mvn.cmd" %*
endlocal

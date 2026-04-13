@echo off
setlocal enabledelayedexpansion
chcp 65001 >nul

:: ══════════════════════════════════════════════
::   GESTOBAR - Docker PostgreSQL Manager
:: ══════════════════════════════════════════════

:: CONFIG FIJA
set CONTAINER_NAME=gestobar-postgres
set DB_NAME=gestobar
set DB_PORT=5432
set PG_VERSION=16

:: ── Credenciales ──
cls
echo.
echo  ╔══════════════════════════════════════════╗
echo  ║      GESTOBAR - DB Docker Manager        ║
echo  ║         Introduce tus credenciales       ║
echo  ╚══════════════════════════════════════════╝
echo.
set /p DB_USER="  Usuario de PostgreSQL: "

:: Captura password oculto con PowerShell a fichero temporal
set TEMP_FILE=%TEMP%\gestobar_tmp.txt
echo  Contrasena de PostgreSQL (no se mostrara):
powershell -command "$p = Read-Host -AsSecureString; $plain = [Runtime.InteropServices.Marshal]::PtrToStringAuto([Runtime.InteropServices.Marshal]::SecureStringToBSTR($p)); Set-Content -Path '%TEMP_FILE%' -Value $plain -NoNewline"
set /p DB_PASSWORD=<"%TEMP_FILE%"
del "%TEMP_FILE%" >nul 2>&1

if "%DB_USER%"=="" (
    echo.
    echo  [ERROR] El usuario no puede estar vacio.
    pause
    exit /b 1
)
if "%DB_PASSWORD%"=="" (
    echo.
    echo  [ERROR] La contrasena no puede estar vacia.
    pause
    exit /b 1
)

echo.
echo  [OK] Credenciales recibidas. Entrando al menu...
timeout /t 1 >nul

:MENU
cls
echo.
echo  ╔══════════════════════════════════════════╗
echo  ║      GESTOBAR - DB Docker Manager        ║
echo  ╠══════════════════════════════════════════╣
echo  ║  Usuario activo: %DB_USER%
echo  ╠══════════════════════════════════════════╣
echo  ║  [1] Iniciar contenedor                  ║
echo  ║  [2] Detener contenedor                  ║
echo  ║  [3] Reiniciar contenedor                ║
echo  ║  [4] Ver logs                            ║
echo  ║  [5] Ver estado del contenedor           ║
echo  ║  [6] Eliminar contenedor (reset total)   ║
echo  ║  [7] Conectar a psql                     ║
echo  ║  [0] Salir                               ║
echo  ╚══════════════════════════════════════════╝
echo.
set /p OPTION="  Selecciona una opcion: "

if "%OPTION%"=="1" goto RUN
if "%OPTION%"=="2" goto STOP
if "%OPTION%"=="3" goto RESTART
if "%OPTION%"=="4" goto LOGS
if "%OPTION%"=="5" goto STATUS
if "%OPTION%"=="6" goto RESET
if "%OPTION%"=="7" goto PSQL
if "%OPTION%"=="0" goto EXIT

echo  [!] Opcion no valida.
timeout /t 2 >nul
goto MENU


:: ══════════════════════════════════════════════
:RUN
cls
echo.
echo  [*] Comprobando si Docker Desktop esta corriendo...
docker info >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo  [ERROR] Docker Desktop no esta corriendo. Arrancalo primero.
    echo.
    pause
    goto MENU
)

echo  [*] Comprobando si el contenedor ya existe...
docker ps -a --format "{{.Names}}" | findstr /i "%CONTAINER_NAME%" >nul 2>&1

if %ERRORLEVEL%==0 (
    echo.
    echo  [!] El contenedor ya existe con sus credenciales originales.
    echo  [!] Si quieres cambiar las credenciales usa la opcion [6] Reset total.
    echo.
    echo  [*] Iniciando contenedor existente...
    docker start %CONTAINER_NAME%
) else (
    echo  [*] Creando contenedor nuevo...
    docker run -d ^
        --name %CONTAINER_NAME% ^
        -e POSTGRES_DB=%DB_NAME% ^
        -e POSTGRES_USER=%DB_USER% ^
        -e POSTGRES_PASSWORD=%DB_PASSWORD% ^
        -p %DB_PORT%:5432 ^
        --restart unless-stopped ^
        postgres:%PG_VERSION%
)

if %ERRORLEVEL%==0 (
    echo.
    echo  [OK] Contenedor iniciado correctamente.
    echo.
    echo  ┌─────────────────────────────────────┐
    echo  │  Host          : localhost           │
    echo  │  Puerto        : %DB_PORT%               │
    echo  │  Base de datos : %DB_NAME%           │
    echo  │  Usuario       : %DB_USER%           │
    echo  │  Password      : ********            │
    echo  └─────────────────────────────────────┘
) else (
    echo  [ERROR] No se pudo iniciar el contenedor.
)
echo.
pause
goto MENU


:: ══════════════════════════════════════════════
:STOP
cls
echo.
echo  [*] Deteniendo contenedor %CONTAINER_NAME%...
docker stop %CONTAINER_NAME%
if %ERRORLEVEL%==0 (
    echo  [OK] Contenedor detenido.
) else (
    echo  [ERROR] No se pudo detener.
)
echo.
pause
goto MENU


:: ══════════════════════════════════════════════
:RESTART
cls
echo.
echo  [*] Reiniciando contenedor %CONTAINER_NAME%...
docker restart %CONTAINER_NAME%
if %ERRORLEVEL%==0 (
    echo  [OK] Contenedor reiniciado correctamente.
) else (
    echo  [ERROR] No se pudo reiniciar.
)
echo.
pause
goto MENU


:: ══════════════════════════════════════════════
:LOGS
cls
echo.
echo  [*] Mostrando logs de %CONTAINER_NAME%...
echo  [!] Pulsa Ctrl+C para salir de los logs.
echo.
docker logs -f %CONTAINER_NAME%
echo.
pause
goto MENU


:: ══════════════════════════════════════════════
:STATUS
cls
echo.
echo  [*] Estado del contenedor:
echo.
docker ps -a --filter "name=%CONTAINER_NAME%" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
echo.
pause
goto MENU


:: ══════════════════════════════════════════════
:RESET
cls
echo.
echo  ╔══════════════════════════════════════════╗
echo  ║  ADVERTENCIA: Esto eliminara el          ║
echo  ║  contenedor Y todos los datos de la BD   ║
echo  ╚══════════════════════════════════════════╝
echo.
set /p CONFIRM="  iEsta seguro? Escribe SI para confirmar: "
if /i "!CONFIRM!"=="SI" (
    echo  [*] Deteniendo y eliminando contenedor...
    docker stop %CONTAINER_NAME% >nul 2>&1
    docker rm %CONTAINER_NAME%
    if %ERRORLEVEL%==0 (
        echo  [OK] Contenedor eliminado.
        echo.
        set /p RECREATE="  iDeseas crearlo ahora con las credenciales actuales? (SI/NO): "
        if /i "!RECREATE!"=="SI" goto RUN
    )
) else (
    echo  [!] Operacion cancelada.
)
echo.
pause
goto MENU


:: ══════════════════════════════════════════════
:PSQL
cls
echo.
echo  [*] Conectando a psql en el contenedor...
echo  [!] Escribe \q para salir de psql.
echo.
docker exec -it %CONTAINER_NAME% psql -U %DB_USER% -d %DB_NAME%
echo.
pause
goto MENU


:: ══════════════════════════════════════════════
:EXIT
cls
set DB_USER=
set DB_PASSWORD=
echo.
echo  Hasta luego!
echo.
exit /b 0
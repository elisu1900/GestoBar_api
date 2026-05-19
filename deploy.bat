@echo off
setlocal enabledelayedexpansion
chcp 65001 >nul

:: ══════════════════════════════════════════════
::   GESTOBAR - Docker Manager
:: ══════════════════════════════════════════════

:: ── Config ──
set CONTAINER_NAME=gestobar-postgres
set DB_NAME=gestobar
set DB_PORT=5432
set PG_VERSION=16
set API_CONTAINER_NAME=gestobar-api
set API_IMAGE_NAME=gestobar-api
set API_PORT=8080

:: ── Credenciales ──
cls
echo.
echo  ╔══════════════════════════════════════════╗
echo  ║       GESTOBAR - Docker Manager          ║
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


:: ══════════════════════════════════════════════
:MAIN_MENU
cls
echo.
echo  ╔══════════════════════════════════════════╗
echo  ║       GESTOBAR - Docker Manager          ║
echo  ╠══════════════════════════════════════════╣
echo  ║  Usuario activo: %DB_USER%
echo  ╠══════════════════════════════════════════╣
echo  ║  [1] Gestionar base de datos             ║
echo  ║  [2] Gestionar API backend               ║
echo  ║  [3] Stack completo (docker-compose)     ║
echo  ║  [0] Salir                               ║
echo  ╚══════════════════════════════════════════╝
echo.
set /p OPTION="  Selecciona una opcion: "

if "%OPTION%"=="1" goto DB_MENU
if "%OPTION%"=="2" goto API_MENU
if "%OPTION%"=="3" goto STACK_MENU
if "%OPTION%"=="0" goto EXIT

echo  [!] Opcion no valida.
timeout /t 2 >nul
goto MAIN_MENU


:: ══════════════════════════════════════════════
:DB_MENU
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
echo  ║  [8] Volver al menu principal            ║
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
if "%OPTION%"=="8" goto MAIN_MENU
if "%OPTION%"=="0" goto EXIT

echo  [!] Opcion no valida.
timeout /t 2 >nul
goto DB_MENU


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
    goto DB_MENU
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
goto DB_MENU


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
goto DB_MENU


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
goto DB_MENU


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
goto DB_MENU


:: ══════════════════════════════════════════════
:STATUS
cls
echo.
echo  [*] Estado del contenedor:
echo.
docker ps -a --filter "name=%CONTAINER_NAME%" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
echo.
pause
goto DB_MENU


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
goto DB_MENU


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
goto DB_MENU


:: ══════════════════════════════════════════════
:API_MENU
cls
echo.
echo  ╔══════════════════════════════════════════╗
echo  ║    GESTOBAR - API Backend Manager        ║
echo  ╠══════════════════════════════════════════╣
echo  ║  Usuario activo: %DB_USER%
echo  ╠══════════════════════════════════════════╣
echo  ║  [1] Construir imagen                    ║
echo  ║  [2] Iniciar contenedor                  ║
echo  ║  [3] Detener contenedor                  ║
echo  ║  [4] Reiniciar contenedor                ║
echo  ║  [5] Ver logs                            ║
echo  ║  [6] Ver estado                          ║
echo  ║  [7] Eliminar contenedor                 ║
echo  ║  [8] Volver al menu principal            ║
echo  ║  [0] Salir                               ║
echo  ╚══════════════════════════════════════════╝
echo.
set /p OPTION="  Selecciona una opcion: "

if "%OPTION%"=="1" goto API_BUILD
if "%OPTION%"=="2" goto API_RUN
if "%OPTION%"=="3" goto API_STOP
if "%OPTION%"=="4" goto API_RESTART
if "%OPTION%"=="5" goto API_LOGS
if "%OPTION%"=="6" goto API_STATUS
if "%OPTION%"=="7" goto API_RESET
if "%OPTION%"=="8" goto MAIN_MENU
if "%OPTION%"=="0" goto EXIT

echo  [!] Opcion no valida.
timeout /t 2 >nul
goto API_MENU


:: ══════════════════════════════════════════════
:API_BUILD
cls
echo.
echo  [*] Comprobando si Docker Desktop esta corriendo...
docker info >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo  [ERROR] Docker Desktop no esta corriendo. Arrancalo primero.
    echo.
    pause
    goto API_MENU
)
echo.
echo  [*] Compilando JAR con Gradle (puede tardar unos minutos)...
call gradlew.bat bootJar -x test
if %ERRORLEVEL% neq 0 (
    echo.
    echo  [ERROR] La compilacion fallo. Revisa los errores de Gradle.
    echo.
    pause
    goto API_MENU
)
echo.
echo  [OK] JAR compilado correctamente.
echo.
echo  [*] Construyendo imagen Docker "%API_IMAGE_NAME%"...
docker build -t %API_IMAGE_NAME% .
if %ERRORLEVEL%==0 (
    echo.
    echo  [OK] Imagen "%API_IMAGE_NAME%" construida correctamente.
) else (
    echo.
    echo  [ERROR] No se pudo construir la imagen Docker.
)
echo.
pause
goto API_MENU


:: ══════════════════════════════════════════════
:API_RUN
cls
echo.
echo  [*] Comprobando si Docker Desktop esta corriendo...
docker info >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo  [ERROR] Docker Desktop no esta corriendo. Arrancalo primero.
    echo.
    pause
    goto API_MENU
)

echo  [*] Verificando que la imagen existe...
docker image inspect %API_IMAGE_NAME% >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo.
    echo  [!] La imagen "%API_IMAGE_NAME%" no existe.
    echo  [!] Usa la opcion [1] Construir imagen primero.
    echo.
    pause
    goto API_MENU
)

echo  [*] Comprobando si el contenedor ya existe...
docker ps -a --format "{{.Names}}" | findstr /i "%API_CONTAINER_NAME%" >nul 2>&1

if %ERRORLEVEL%==0 (
    echo  [*] Iniciando contenedor existente...
    docker start %API_CONTAINER_NAME%
) else (
    echo  [*] Creando contenedor nuevo...
    docker run -d ^
        --name %API_CONTAINER_NAME% ^
        -e DB_USER=%DB_USER% ^
        -e DB_PASSWORD=%DB_PASSWORD% ^
        -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:%DB_PORT%/%DB_NAME% ^
        -p %API_PORT%:8080 ^
        --restart unless-stopped ^
        %API_IMAGE_NAME%
)

if %ERRORLEVEL%==0 (
    echo.
    echo  [OK] API iniciada correctamente.
    echo.
    echo  [INFO] URL    : http://localhost:%API_PORT%
    echo  [INFO] Swagger: http://localhost:%API_PORT%/swagger-ui.html
    echo.
    echo  [!] Asegurate de que la BD este corriendo ^(opcion 1 del menu^).
) else (
    echo  [ERROR] No se pudo iniciar el contenedor.
)
echo.
pause
goto API_MENU


:: ══════════════════════════════════════════════
:API_STOP
cls
echo.
echo  [*] Deteniendo contenedor %API_CONTAINER_NAME%...
docker stop %API_CONTAINER_NAME%
if %ERRORLEVEL%==0 (
    echo  [OK] Contenedor detenido.
) else (
    echo  [ERROR] No se pudo detener (puede que ya este parado).
)
echo.
pause
goto API_MENU


:: ══════════════════════════════════════════════
:API_RESTART
cls
echo.
echo  [*] Reiniciando contenedor %API_CONTAINER_NAME%...
docker restart %API_CONTAINER_NAME%
if %ERRORLEVEL%==0 (
    echo  [OK] Contenedor reiniciado correctamente.
) else (
    echo  [ERROR] No se pudo reiniciar.
)
echo.
pause
goto API_MENU


:: ══════════════════════════════════════════════
:API_LOGS
cls
echo.
echo  [*] Mostrando logs de %API_CONTAINER_NAME%...
echo  [!] Pulsa Ctrl+C para salir de los logs.
echo.
docker logs -f %API_CONTAINER_NAME%
echo.
pause
goto API_MENU


:: ══════════════════════════════════════════════
:API_STATUS
cls
echo.
echo  [*] Estado del contenedor:
echo.
docker ps -a --filter "name=%API_CONTAINER_NAME%" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
echo.
pause
goto API_MENU


:: ══════════════════════════════════════════════
:API_RESET
cls
echo.
echo  ╔══════════════════════════════════════════╗
echo  ║  ADVERTENCIA: Esto eliminara el          ║
echo  ║  contenedor de la API                    ║
echo  ╚══════════════════════════════════════════╝
echo.
set /p CONFIRM="  iEsta seguro? Escribe SI para confirmar: "
if /i "!CONFIRM!"=="SI" (
    echo  [*] Deteniendo y eliminando contenedor...
    docker stop %API_CONTAINER_NAME% >nul 2>&1
    docker rm %API_CONTAINER_NAME%
    if %ERRORLEVEL%==0 (
        echo  [OK] Contenedor eliminado.
        echo.
        set /p DEL_IMAGE="  iEliminar tambien la imagen Docker? (SI/NO): "
        if /i "!DEL_IMAGE!"=="SI" (
            docker rmi %API_IMAGE_NAME%
            if %ERRORLEVEL%==0 (
                echo  [OK] Imagen eliminada.
            ) else (
                echo  [ERROR] No se pudo eliminar la imagen.
            )
        )
    ) else (
        echo  [ERROR] No se pudo eliminar el contenedor.
    )
) else (
    echo  [!] Operacion cancelada.
)
echo.
pause
goto API_MENU


:: ══════════════════════════════════════════════
:STACK_MENU
cls
echo.
echo  ╔══════════════════════════════════════════╗
echo  ║    GESTOBAR - Stack Completo             ║
echo  ║    (docker-compose: DB + API)            ║
echo  ╠══════════════════════════════════════════╣
echo  ║  Usuario activo: %DB_USER%
echo  ╠══════════════════════════════════════════╣
echo  ║  [1] Levantar stack (build + up)         ║
echo  ║  [2] Detener stack                       ║
echo  ║  [3] Reiniciar stack                     ║
echo  ║  [4] Ver logs del stack                  ║
echo  ║  [5] Ver estado de los servicios         ║
echo  ║  [6] Eliminar stack y volumenes (reset)  ║
echo  ║  [8] Volver al menu principal            ║
echo  ║  [0] Salir                               ║
echo  ╚══════════════════════════════════════════╝
echo.
echo  [!] NOTA: Usa este menu O los menus individuales, no ambos a la vez.
echo.
set /p OPTION="  Selecciona una opcion: "

if "%OPTION%"=="1" goto STACK_UP
if "%OPTION%"=="2" goto STACK_DOWN
if "%OPTION%"=="3" goto STACK_RESTART
if "%OPTION%"=="4" goto STACK_LOGS
if "%OPTION%"=="5" goto STACK_STATUS
if "%OPTION%"=="6" goto STACK_RESET
if "%OPTION%"=="8" goto MAIN_MENU
if "%OPTION%"=="0" goto EXIT

echo  [!] Opcion no valida.
timeout /t 2 >nul
goto STACK_MENU


:: ══════════════════════════════════════════════
:STACK_UP
cls
echo.
echo  [*] Comprobando si Docker Desktop esta corriendo...
docker info >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo  [ERROR] Docker Desktop no esta corriendo. Arrancalo primero.
    echo.
    pause
    goto STACK_MENU
)
echo.
echo  [*] Compilando JAR con Gradle (puede tardar unos minutos)...
call gradlew.bat bootJar -x test
if %ERRORLEVEL% neq 0 (
    echo.
    echo  [ERROR] La compilacion fallo. Revisa los errores de Gradle.
    echo.
    pause
    goto STACK_MENU
)
echo.
echo  [OK] JAR compilado correctamente.
echo.
echo  [*] Levantando el stack con docker compose (build + up)...
docker compose up -d --build
if %ERRORLEVEL%==0 (
    echo.
    echo  [OK] Stack levantado correctamente.
    echo.
    echo  [INFO] API URL : http://localhost:8080
    echo  [INFO] Swagger : http://localhost:8080/swagger-ui.html
    echo  [INFO] DB Port : 5432
    echo.
    echo  [!] La API puede tardar ~30s mientras Flyway ejecuta las migraciones.
) else (
    echo.
    echo  [ERROR] No se pudo levantar el stack. Revisa los logs con la opcion [4].
)
echo.
pause
goto STACK_MENU


:: ══════════════════════════════════════════════
:STACK_DOWN
cls
echo.
echo  [*] Deteniendo el stack...
docker compose down
if %ERRORLEVEL%==0 (
    echo  [OK] Stack detenido. Los datos de la BD se conservan en el volumen.
) else (
    echo  [ERROR] No se pudo detener el stack.
)
echo.
pause
goto STACK_MENU


:: ══════════════════════════════════════════════
:STACK_RESTART
cls
echo.
echo  [*] Reiniciando el stack...
docker compose restart
if %ERRORLEVEL%==0 (
    echo  [OK] Stack reiniciado.
) else (
    echo  [ERROR] No se pudo reiniciar.
)
echo.
pause
goto STACK_MENU


:: ══════════════════════════════════════════════
:STACK_LOGS
cls
echo.
echo  [*] Mostrando logs del stack...
echo  [!] Pulsa Ctrl+C para salir de los logs.
echo.
docker compose logs -f
echo.
pause
goto STACK_MENU


:: ══════════════════════════════════════════════
:STACK_STATUS
cls
echo.
echo  [*] Estado de los servicios:
echo.
docker compose ps
echo.
pause
goto STACK_MENU


:: ══════════════════════════════════════════════
:STACK_RESET
cls
echo.
echo  ╔══════════════════════════════════════════╗
echo  ║  ADVERTENCIA: Esto eliminara todos los   ║
echo  ║  contenedores Y VOLUMENES del stack.     ║
echo  ║  Se perderan TODOS los datos de la BD.   ║
echo  ╚══════════════════════════════════════════╝
echo.
set /p CONFIRM="  iEsta seguro? Escribe SI para confirmar: "
if /i "!CONFIRM!"=="SI" (
    echo  [*] Eliminando stack y volumenes...
    docker compose down -v
    if %ERRORLEVEL%==0 (
        echo  [OK] Stack y volumenes eliminados.
    ) else (
        echo  [ERROR] No se pudo eliminar el stack.
    )
) else (
    echo  [!] Operacion cancelada.
)
echo.
pause
goto STACK_MENU


:: ══════════════════════════════════════════════
:EXIT
cls
set DB_USER=
set DB_PASSWORD=
echo.
echo  Hasta luego!
echo.
exit /b 0

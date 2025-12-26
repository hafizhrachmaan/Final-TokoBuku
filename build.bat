@echo off
setlocal EnableDelayedExpansion

:: ======================================================
::            J A V A R K   B U I L D E R
:: ======================================================

:: Mendapatkan karakter ESC untuk warna ANSI
for /F %%a in ('echo prompt $E ^| cmd') do set "ESC=%%a"

:: Palette Warna
set "cR=%ESC%[91m"   &:: Merah Terang
set "cG=%ESC%[92m"   &:: Hijau Terang
set "cY=%ESC%[93m"   &:: Kuning Terang
set "cB=%ESC%[94m"   &:: Biru Terang
set "cC=%ESC%[96m"   &:: Cyan Terang
set "cW=%ESC%[97m"   &:: Putih Terang
set "cX=%ESC%[0m"    &:: Reset

:: Badge Styles
set "bERR=%ESC%[41m%cW% ERROR %cX%%cR%"
set "bOK=%ESC%[42m%cW%  OK   %cX%%cG%"
set "bINF=%ESC%[44m%cW% INFO  %cX%%cC%"
set "bWRN=%ESC%[43m%ESC%[30m WARN  %cX%%cY%"

:: ====================================================
:: 2. KONFIGURASI PROYEK
:: ====================================================
set "JAR_NAME=hrd-app-0.0.1-SNAPSHOT.jar"
set "CLI_CLASS=com.example.hrdapp.CliTaskRunner"

:: ====================================================
:: 3. SYSTEM CHECK
:: ====================================================
cls
:: Cek apakah Maven terinstall
where mvn >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo %bERR% Maven tidak terdeteksi!
    echo         Pastikan 'mvn' bisa dijalankan di CMD.
    pause
    exit
)

:: ====================================================
:: 4. DASHBOARD MENU
:: ====================================================
:menu
cls
echo.
echo  %cB%======================================================%cX%
echo  %cW%           J A V A R K   B U I L D E R              %cX%
echo  %cB%======================================================%cX%
echo.
echo  %cW%[1]%cX% %cG%WEB MODE%cX% : Build JAR + Run Web App
echo  %cW%[2]%cX% %cY%CLI MODE%cX% : Compile + Run CLI Tasks
echo  %cW%[0]%cX% %cR%KELUAR%cX%
echo.
echo  %cB%------------------------------------------------------%cX%
set /p "pilih= %cC%>> Pilih Menu (0-2): %cX%"

if "%pilih%"=="1" goto :web_mode
if "%pilih%"=="2" goto :cli_mode
if "%pilih%"=="0" exit
goto :menu

:: ====================================================
:: 5. CORE LOGIC
:: ====================================================

:web_mode
echo.
call :header "MEMULAI WEB MODE"
echo %bINF% Building JAR dengan Maven...
mvn clean package -q
if %ERRORLEVEL% NEQ 0 (
    echo %bERR% Build gagal! Cek kode Anda.
    pause
    goto :menu
)
echo %bOK%  Build berhasil.
echo %bINF% Menjalankan aplikasi web...
java -jar target\%JAR_NAME%
goto :end

:cli_mode
echo.
call :header "MEMULAI CLI MODE"
echo %bINF% Mengkompilasi dengan Maven...
mvn compile -q
if %ERRORLEVEL% NEQ 0 (
    echo %bERR% Kompilasi gagal! Cek kode Anda.
    pause
    goto :menu
)
echo %bOK%  Kompilasi berhasil.
echo %bINF% Menjalankan CLI tasks...
java -cp target\classes %CLI_CLASS%
echo.
call :header "CLI SELESAI"
pause
goto :menu

:: ====================================================
:: HELPER
:: ====================================================
:header
echo %cB%------------------------------------------------------%cX%
echo %cW%  %~1%cX%
echo %cB%------------------------------------------------------%cX%
goto :eof

:end
pause

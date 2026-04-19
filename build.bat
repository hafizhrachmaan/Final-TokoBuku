@echo off
setlocal EnableDelayedExpansion

:: ======================================================
::     H A F I Z H R A C H M A N   B U I L D E R
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
set "CLI_CLASS=com.tokobuku.nitnot.CliTaskRunner"

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
echo  %cW%     H A F I Z H R A C H M A N   B U I L D E R      %cX%
echo  %cB%======================================================%cX%
echo.
echo  %cW%[1]%cX% %cG%WEB ONLY%cX% : Jalankan hanya aplikasi web
echo  %cW%[2]%cX% %cY%CLI ONLY%cX% : Jalankan hanya tugas antarmuka baris perintah
echo  %cW%[0]%cX% %cR%KELUAR%cX%
echo.
echo  %cB%------------------------------------------------------%cX%
set /p "pilih= %cC%>> Pilih Menu (0-2): %cX%"

if "%pilih%"=="1" goto :web_only_mode
if "%pilih%"=="2" goto :cli_only_mode
if "%pilih%"=="0" exit
goto :menu

:: ====================================================
:: 5. CORE LOGIC
:: ====================================================

:web_only_mode
echo.
call :header "MEMULAI WEB ONLY MODE"
echo %bINF% Menjalankan aplikasi web dengan Maven...
call mvn spring-boot:run
if %ERRORLEVEL% NEQ 0 (
    echo %bERR% Eksekusi gagal! Periksa output untuk kesalahan.
    pause
)
goto :menu

:cli_only_mode
echo.
call :header "MEMULAI CLI ONLY MODE"
echo %bINF% Menjalankan hanya tugas CLI dengan Maven...
call mvn spring-boot:run -Dspring-boot.run.profiles=cli -Dspring-boot.run.arguments="--spring.main.web-application-type=none"
if %ERRORLEVEL% NEQ 0 (
    echo %bERR% Eksekusi gagal! Periksa output untuk kesalahan.
    pause
)
echo.
call :header "TUGAS CLI SELESAI"
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

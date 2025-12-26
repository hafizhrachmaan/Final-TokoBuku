#!/bin/bash

# Palette Warna
cR='\e[91m' # Merah Terang
cG='\e[92m' # Hijau Terang
cY='\e[93m' # Kuning Terang
cB='\e[94m' # Biru Terang
cC='\e[96m' # Cyan Terang
cW='\e[97m' # Putih Terang
cX='\e[0m'  # Reset

# Menu
function menu() {
    clear
    echo
    echo -e " $cB======================================================$cX"
    echo -e " $cW    H A F I Z H R A C H M A N   B U I L D E R     $cX"
    echo -e " $cB======================================================$cX"
    echo
    echo -e " $cW[1]$cX $cG""WEB ONLY$cX : Jalankan hanya aplikasi web"
    echo -e " $cW[2]$cX $cY""CLI ONLY$cX : Jalankan hanya tugas antarmuka baris perintah"
    echo -e " $cW[3]$cX $cR""Detach Screen$cX : Keluar dari sesi screen VPS"
    echo -e " $cW[0]$cX $cR""KELUAR$cX"
    echo
    echo -e " $cB------------------------------------------------------$cX"
    read -p " >> Pilih Menu (0-3): " pilih

    case $pilih in
        1) web_only_mode ;;
        2) cli_only_mode ;;
        3) detach_screen ;;
        0) exit ;;
        *) menu ;;
    esac
}

# Functions for modes
function web_only_mode() {
    echo
    echo -e " $cB------------------------------------------------------$cX"
    echo -e " $cW  MEMULAI WEB ONLY MODE$cX"
    echo -e " $cB------------------------------------------------------$cX"
    echo -e " $cC INFO  $cX Menjalankan aplikasi web dengan Maven..."
    mvn spring-boot:run
    if [ $? -ne 0 ]; then
        echo -e " $cR ERROR $cX Eksekusi gagal! Periksa output untuk kesalahan."
        read -p "Tekan [Enter] untuk kembali ke menu..."
    fi
    menu
}

function cli_only_mode() {
    echo
    echo -e " $cB------------------------------------------------------$cX"
    echo -e " $cW  MEMULAI CLI ONLY MODE$cX"
    echo -e " $cB------------------------------------------------------$cX"
    echo -e " $cC INFO  $cX Menjalankan hanya tugas CLI dengan Maven..."
    mvn spring-boot:run -Dspring-boot.run.profiles=cli -Dspring-boot.run.arguments="--spring.main.web-application-type=none"
    if [ $? -ne 0 ]; then
        echo -e " $cR ERROR $cX Eksekusi gagal! Periksa output untuk kesalahan."
    fi
    echo
    echo -e " $cB------------------------------------------------------$cX"
    echo -e " $cW  TUGAS CLI SELESAI$cX"
    echo -e " $cB------------------------------------------------------$cX"
    read -p "Tekan [Enter] untuk kembali ke menu..."
    menu
}

function detach_screen() {
    screen -d >/dev/null 2>&1
}

# Main execution
whereis mvn >/dev/null 2>&1
if [ $? -ne 0 ]; then
    clear
    echo -e " $cR ERROR $cX Maven tidak terdeteksi!"
    echo "        Pastikan 'mvn' bisa dijalankan di shell."
    exit
fi

menu
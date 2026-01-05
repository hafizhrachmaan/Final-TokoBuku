#!/bin/bash

# Auto-start in screen if not already in one
if [ -z "$STY" ]; then
    echo "Memulai sesi screen 'hrdapp' baru dan menjalankan builder di dalamnya..."
    # Relaunch this script inside a new screen session, then leave the user in a bash shell
    screen -S hrdapp bash -c "'$0'; exec bash"
    echo "Sesi 'hrdapp' telah dibuat. Untuk masuk, ketik: screen -r hrdapp"
    exit 0
fi

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
    while true; do
        clear
        echo
        echo -e " $cB======================================================$cX"
        echo -e " $cW    H A F I Z H R A C H M A N   B U I L D E R     $cX"
        echo -e " $cB======================================================$cX"
        echo
        echo -e " $cW  Window Aktif:$cX"
        screen -Q windows
        echo
        echo -e " $cW[1]$cX $cG""WEB ONLY$cX : Jalankan server di window baru"
        echo -e " $cW[2]$cX $cY""CLI ONLY$cX : Jalankan CLI di window baru"
        echo -e " $cW[3]$cX $cR""Detach$cX     : Keluar dari sesi screen (proses tetap jalan)"
        echo -e " $cW[4]$cX $cR""Kill All$cX  : Matikan semua proses & window"
        echo -e " $cW[0]$cX $cR""KELUAR$cX     : Tutup builder & terminasi sesi screen"
        echo
        echo -e " $cB------------------------------------------------------$cX"
        echo -e " $cW""Gunakan Ctrl+A, [nomor] untuk pindah window (misal: Ctrl+A, 1)$cX"
        echo -e " $cW""Gunakan Ctrl+A, 0 untuk kembali ke menu ini kapan saja$cX"
        echo -e " $cB------------------------------------------------------$cX"
        read -p " >> Pilih Menu (0-4): " pilih

        case $pilih in
            1) web_only_mode ;;
            2) cli_only_mode ;;
            3) detach_screen ;;
            4) kill_all ;;
            0) exit ;;
            *) continue ;;
        esac
    done
}

# Functions for modes
function web_only_mode() {
    echo "Membuat window baru '1:Web Server' dan menjalankan..."
    # The 'read' command at the end keeps the window open after the process finishes
    screen -X screen -t "Web Server" 1 bash -c 'mvn spring-boot:run; echo; echo "Proses server telah berhenti. Tekan [Enter] untuk menutup window ini."; read'
    echo "Server web sedang dimulai di window 1."
    read -p "Tekan [Enter] untuk kembali ke menu."
}

function cli_only_mode() {
    echo "Membuat window baru '2:CLI Task' dan menjalankan..."
    screen -X screen -t "CLI Task" 2 bash -c 'mvn spring-boot:run -Dspring-boot.run.profiles=cli -Dspring-boot.run.arguments="--spring.main.web-application-type=none"; echo; echo "Tugas CLI telah selesai. Tekan [Enter] untuk menutup window ini."; read'
    echo "Tugas CLI sedang dimulai di window 2."
    read -p "Tekan [Enter] untuk kembali ke menu."
}

function detach_screen() {
    screen -d >/dev/null 2>&1
}

function kill_all() {
    echo "Mematikan semua window dan proses..."
    # This will kill all windows except the current one (0)
    screen -X kill
    sleep 1
    echo "Semua proses telah dimatikan."
    read -p "Tekan [Enter] untuk kembali ke menu."
}


# Main execution
whereis mvn >/dev/null 2>&1
if [ $? -ne 0 ]; then
    clear
    echo -e " $cR ERROR $cX Maven tidak terdeteksi!"
    echo "        Pastikan 'mvn' bisa dijalankan di shell."
    exit
fi

# Set the title for the main menu window
screen -X title "Menu Utama"

menu
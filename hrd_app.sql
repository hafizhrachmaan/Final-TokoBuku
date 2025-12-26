-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Waktu pembuatan: 20 Des 2025 pada 04.58
-- Versi server: 8.0.30
-- Versi PHP: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Basis data: `hrd_app`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `products`
--

CREATE TABLE `products` (
  `id` bigint NOT NULL,
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` double NOT NULL,
  `stock` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `products`
--

INSERT INTO `products` (`id`, `description`, `name`, `price`, `stock`) VALUES
(4, 'Pulpen 0.5', 'Bolpoin HI-TECH', 30000, 90),
(5, 'Kertas Folio Garis', 'Kertas Folio', 500, 992),
(9, 'Pulpen tinta hitam', 'Pulpen Standard AE7', 3000, 195),
(10, 'Pulpen tinta biru', 'Pulpen Pilot G2', 12000, 72),
(11, 'Pensil HB', 'Pensil Faber Castell HB', 2500, 178),
(12, 'Pensil 2B', 'Pensil Faber Castell 2B', 3000, 148),
(13, 'Penghapus putih', 'Penghapus Joyko ER-12', 2000, 157),
(14, 'Penghapus karet', 'Penghapus Staedtler', 5000, 88),
(15, 'Rautan plastik', 'Rautan Joyko', 2500, 139),
(16, 'Rautan besi', 'Rautan Kenko', 4000, 109),
(17, 'Penggaris 30 cm', 'Penggaris Butterfly 30cm', 3500, 129),
(18, 'Spidol hitam', 'Spidol Snowman Board Marker', 8500, 68),
(19, 'Spidol warna', 'Spidol Snowman Color', 9000, 64),
(20, 'Stabilo kuning', 'Stabilo Joyko HL-1', 6000, 94),
(21, 'Stabilo hijau', 'Stabilo Joyko HL-2', 6000, 90),
(22, 'Kertas HVS A4 80gsm', 'Kertas HVS Sinar Dunia', 55000, 40),
(23, 'Kertas Folio 70gsm', 'Kertas Folio Paperline', 45000, 50),
(24, 'Map plastik', 'Map Plastik Bening', 2000, 210),
(25, 'Map kertas', 'Map Kertas Snelhecter', 3500, 170),
(26, 'Amplop coklat', 'Amplop Coklat A4', 1500, 300),
(27, 'Lem cair', 'Lem Fox Cair', 7000, 85),
(28, 'Lem stik', 'Lem UHU Stick', 10000, 60),
(29, 'Correction tape', 'Tip-Ex Joyko', 8500, 70),
(30, 'Isi ulang correction', 'Refill Tip-Ex Joyko', 5000, 90),
(32, 'Sticky note besar', 'Post-it 3x5', 8500, 95),
(33, 'Binder clip kecil', 'Binder Clip No.107', 4000, 140),
(34, 'Binder clip besar', 'Binder Clip No.155', 7000, 100),
(35, 'Stapler kecil', 'Stapler Joyko Mini', 15000, 45),
(37, 'Lakban bening', 'Lakban Bening Daimaru', 8000, 70),
(38, 'Lakban hitam', 'Lakban Hitam Daimaru', 9000, 65);

-- --------------------------------------------------------

--
-- Struktur dari tabel `transactions`
--

CREATE TABLE `transactions` (
  `id` bigint NOT NULL,
  `total_price` double NOT NULL,
  `transaction_date` datetime(6) NOT NULL,
  `user_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `transactions`
--

INSERT INTO `transactions` (`id`, `total_price`, `transaction_date`, `user_id`) VALUES
(1, 10000, '2025-12-16 12:55:51.096991', 27),
(2, 15000, '2025-12-16 01:44:49.602893', 29),
(3, 5000, '2025-12-16 15:50:28.559267', 30),
(4, 20000, '2025-12-16 15:51:27.319601', 30),
(5, 5000, '2025-12-16 15:55:38.992174', 30),
(6, 500000, '2025-12-16 17:57:19.117340', 52),
(7, 250000, '2025-12-16 17:59:37.764920', 52),
(8, 5500, '2025-12-16 19:45:21.700142', 30),
(9, 500, '2025-12-16 19:56:23.681001', 30),
(10, 5500, '2025-12-16 20:06:50.402914', 30),
(11, 5500, '2025-12-16 20:20:32.520498', 30),
(15, 137500, '2025-12-17 16:12:35.617031', 52),
(16, 30500, '2025-12-17 19:15:52.545138', 30),
(17, 35000, '2025-12-17 19:28:47.527937', 30),
(18, 5000, '2025-12-18 21:40:58.345356', 30),
(19, 4500, '2025-12-18 21:43:06.795364', 30),
(20, 500, '2025-12-18 22:00:43.295177', 30),
(21, 7000, '2025-12-18 23:45:57.059743', 30),
(22, 13000, '2025-12-19 06:04:13.410891', 126);

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaction_details`
--

CREATE TABLE `transaction_details` (
  `id` bigint NOT NULL,
  `price` double NOT NULL,
  `quantity` int NOT NULL,
  `product_id` bigint NOT NULL,
  `transaction_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `transaction_details`
--

INSERT INTO `transaction_details` (`id`, `price`, `quantity`, `product_id`, `transaction_id`) VALUES
(26, 2500, 1, 15, 15),
(27, 4000, 1, 16, 15),
(28, 3500, 1, 17, 15),
(29, 8500, 2, 18, 15),
(30, 9000, 1, 19, 15),
(31, 6000, 1, 20, 15),
(32, 30000, 1, 4, 16),
(33, 500, 1, 5, 16),
(34, 7000, 5, 8, 17),
(35, 4500, 1, 6, 18),
(36, 500, 1, 5, 18),
(37, 4500, 1, 6, 19),
(38, 500, 1, 5, 20),
(39, 7000, 1, 8, 21),
(40, 7000, 1, 8, 22),
(41, 3000, 2, 9, 22);

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` bigint NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` enum('HRD','KASIR','STOCKER') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('PENDING','VERIFIED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_education` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `work_experience` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `password`, `role`, `username`, `status`, `address`, `email`, `full_name`, `last_education`, `phone`, `work_experience`) VALUES
(26, '$2a$10$er8JCHVh7udusjOvL.8jW.1RkE2Ivsb/9iRFVxhLRB.dzIbjvWq2W', 'STOCKER', 'hafizh16', 'VERIFIED', 'Sedati Raya Blok H 16', 'hafizhrachman@gmail.com', 'Hafizh Syarifuddin Rachman', 'SMA', '081234567812', 'Saya pernah bekerja sebagai leader selama 2 tahun'),
(27, '$2a$10$ftOUWpY.IZrXt2G5wJCF6eMKgpc1lt.qLUrmkyWePePhLNt.TQjwi', 'KASIR', 'rijal', 'VERIFIED', 'Semolo Waru', 'rizalsultono@gmail.com', 'Rizal Sultoni', 'S1 Kasir', '981276578576', 'dyddydr'),
(29, '$2a$10$rnGiTcUGdrS7/djbjWn6vecv9a9JHPmyzA5D3G0QG2UsBZusatpFu', 'KASIR', 'diaz43', 'VERIFIED', 'mawar 12', 'diaz@mail.com', 'diaz azriel purnomo', 'SMK', '09787867767', '1 tahun system administrator'),
(30, '$2a$10$8kAUTo1BhK.i5EuML8OwJuYf9IJgdEo8YY2kqwzEZOmuBsB2g59FG', 'KASIR', 'Nitnot', 'VERIFIED', 'yhi', 'jeng@gmail.com', 'jeng', 'kdj', '87', 'hs8'),
(52, '$2a$10$2zjoJG3.SpQQgDL0LapyhurEwZvR0KZ.6J.ggLLzofAgwhebAnpr2', 'KASIR', 'semprul', 'VERIFIED', 'Jl in aja', 'maftuh@gmail.com', 'Maftuh Afandi', 'S5 tank', '0812345', 'Kerja Kelompok'),
(65, '$2a$10$BE03q66x6AJvYLmBVQiJpuTpA3vYaFDLgdVxFiZfJ4IIftM.TODQ2', 'HRD', 'hafizh', 'VERIFIED', NULL, NULL, NULL, NULL, NULL, NULL),
(126, '$2a$10$M1QnMhBtBnOQkr3Z4q5kY.tMq.AWZMb9/QrWmpH1Z4NWDFBS8oTjW', 'KASIR', 'amre', 'VERIFIED', 'Patra', 'amrepitutur@gmail.com', 'Amre Pitutur', 'S3', '23456', 'Kerja Di warkop'),
(128, '$2a$10$cM9PInhMxlN1AirIC48E3.VijjX5tulsLGoF7uL6poZp5ay47/5o.', 'HRD', 'hrd', 'VERIFIED', NULL, NULL, NULL, NULL, NULL, NULL);

--
-- Indeks untuk tabel yang dibuang
--

--
-- Indeks untuk tabel `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKqwv7rmvc8va8rep7piikrojds` (`user_id`);

--
-- Indeks untuk tabel `transaction_details`
--
ALTER TABLE `transaction_details`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKd37lutuhg56s6tq30oowjk8js` (`product_id`),
  ADD KEY `FKm5vjjt9jqvf7y69innpgqnipr` (`transaction_id`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `products`
--
ALTER TABLE `products`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=45;

--
-- AUTO_INCREMENT untuk tabel `transactions`
--
ALTER TABLE `transactions`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT untuk tabel `transaction_details`
--
ALTER TABLE `transaction_details`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=129;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `FKqwv7rmvc8va8rep7piikrojds` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Ketidakleluasaan untuk tabel `transaction_details`
--
ALTER TABLE `transaction_details`
  ADD CONSTRAINT `FKd37lutuhg56s6tq30oowjk8js` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  ADD CONSTRAINT `FKm5vjjt9jqvf7y69innpgqnipr` FOREIGN KEY (`transaction_id`) REFERENCES `transactions` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 27, 2023 at 10:43 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `scolarite`
--

-- --------------------------------------------------------

--
-- Table structure for table `control`
--

use a0ynjpp42dcgrgjq;

CREATE TABLE `control` (
  `id` bigint(20) NOT NULL,
  `date_control` datetime DEFAULT NULL,
  `etat` bit(1) DEFAULT NULL,
  `control_point_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `control_ckeck_point`
--

CREATE TABLE `control_ckeck_point` (
  `id` bigint(20) NOT NULL,
  `range_level` double DEFAULT NULL,
  `type` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `enseignants`
--

CREATE TABLE `enseignants` (
  `id` bigint(20) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `matricule` varchar(255) NOT NULL,
  `status_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `etudiants`
--

CREATE TABLE `etudiants` (
  `id` bigint(20) NOT NULL,
  `classe` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `matricule` varchar(255) NOT NULL,
  `montant_pay` varchar(255) DEFAULT NULL,
  `total_pension` varchar(255) DEFAULT NULL,
  `status_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(1),
(1),
(1),
(1),
(1),
(1),
(1);

-- --------------------------------------------------------

--
-- Table structure for table `pointage_etudiant`
--

CREATE TABLE `pointage_etudiant` (
  `id` bigint(20) NOT NULL,
  `date` date DEFAULT NULL,
  `get_time_in1` time DEFAULT NULL,
  `get_time_in2` time DEFAULT NULL,
  `get_time_in3` time DEFAULT NULL,
  `get_time_in4` time DEFAULT NULL,
  `get_time_in5` time DEFAULT NULL,
  `get_time_out1` time DEFAULT NULL,
  `get_time_out2` time DEFAULT NULL,
  `get_time_out3` time DEFAULT NULL,
  `get_time_out4` time DEFAULT NULL,
  `get_time_out5` time DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `etudiant_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pointage_professeur`
--

CREATE TABLE `pointage_professeur` (
  `id` bigint(20) NOT NULL,
  `date` date DEFAULT NULL,
  `get_time_in1` time DEFAULT NULL,
  `get_time_in2` time DEFAULT NULL,
  `get_time_in3` time DEFAULT NULL,
  `get_time_in4` time DEFAULT NULL,
  `get_time_in5` time DEFAULT NULL,
  `get_time_out1` time DEFAULT NULL,
  `get_time_out2` time DEFAULT NULL,
  `get_time_out3` time DEFAULT NULL,
  `get_time_out4` time DEFAULT NULL,
  `get_time_out5` time DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `enseignant_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `statut`
--

CREATE TABLE `statut` (
  `id` bigint(20) NOT NULL,
  `name` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `control`
--
ALTER TABLE `control`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKmuaf5e6t1tx64q15slmt5t7yi` (`control_point_id`);

--
-- Indexes for table `control_ckeck_point`
--
ALTER TABLE `control_ckeck_point`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `enseignants`
--
ALTER TABLE `enseignants`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_f2mtugyb88xrnvy1ktrslksxm` (`matricule`),
  ADD KEY `FKtae6auwpgsm71f946fi55n1q0` (`status_id`);

--
-- Indexes for table `etudiants`
--
ALTER TABLE `etudiants`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_7dcho6ioi2rdfjm2fjtsm1xcy` (`matricule`),
  ADD KEY `FK1koq0ixn9lxe43wvmh33t3cqw` (`status_id`);

--
-- Indexes for table `pointage_etudiant`
--
ALTER TABLE `pointage_etudiant`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKq44ypq5369aiyrxwodybcio8f` (`etudiant_id`);

--
-- Indexes for table `pointage_professeur`
--
ALTER TABLE `pointage_professeur`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK2vbfefaj71dm357aetuw7yon0` (`enseignant_id`);

--
-- Indexes for table `statut`
--
ALTER TABLE `statut`
  ADD PRIMARY KEY (`id`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `control`
--
ALTER TABLE `control`
  ADD CONSTRAINT `FKmuaf5e6t1tx64q15slmt5t7yi` FOREIGN KEY (`control_point_id`) REFERENCES `control_ckeck_point` (`id`);

--
-- Constraints for table `enseignants`
--
ALTER TABLE `enseignants`
  ADD CONSTRAINT `FKtae6auwpgsm71f946fi55n1q0` FOREIGN KEY (`status_id`) REFERENCES `statut` (`id`);

--
-- Constraints for table `etudiants`
--
ALTER TABLE `etudiants`
  ADD CONSTRAINT `FK1koq0ixn9lxe43wvmh33t3cqw` FOREIGN KEY (`status_id`) REFERENCES `statut` (`id`);

--
-- Constraints for table `pointage_etudiant`
--
ALTER TABLE `pointage_etudiant`
  ADD CONSTRAINT `FKq44ypq5369aiyrxwodybcio8f` FOREIGN KEY (`etudiant_id`) REFERENCES `etudiants` (`id`);

--
-- Constraints for table `pointage_professeur`
--
ALTER TABLE `pointage_professeur`
  ADD CONSTRAINT `FK2vbfefaj71dm357aetuw7yon0` FOREIGN KEY (`enseignant_id`) REFERENCES `enseignants` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

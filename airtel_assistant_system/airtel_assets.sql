-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 25, 2026 at 10:52 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `airtel_assets`
--

-- --------------------------------------------------------

--
-- Table structure for table `assets`
--

CREATE TABLE `assets` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `serial_number` varchar(100) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'AVAILABLE',
  `asset_id` int(11) NOT NULL,
  `asset_tag` varchar(255) DEFAULT NULL,
  `brand` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `condition_status` varchar(255) DEFAULT NULL,
  `device_name` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `purchase_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `assets`
--

INSERT INTO `assets` (`id`, `name`, `serial_number`, `type`, `status`, `asset_id`, `asset_tag`, `brand`, `category`, `condition_status`, `device_name`, `model`, `purchase_date`) VALUES
(1, 'laptop', 'zxcvbn1234567', 'personal', 'ASSIGNED', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(2, 'phone', 'mnbvcxz76543211', 'desktop', 'AVAILABLE', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(3, 'tablet', 'mmbv2345678nn', 'personal', 'AVAILABLE', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(4, 'speaker', '0987654jhgfdbvc', 'large', 'AVAILABLE', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(5, 'bluetooth', '234567iuytxcvb', 'personal', 'ASSIGNED', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `assignments`
--

CREATE TABLE `assignments` (
  `id` int(11) NOT NULL,
  `asset_id` int(11) DEFAULT NULL,
  `employee_name` varchar(100) DEFAULT NULL,
  `department` varchar(100) DEFAULT NULL,
  `assign_date` date DEFAULT NULL,
  `expected_return` date DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `assigned_date` date DEFAULT NULL,
  `assign_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `assignments`
--

INSERT INTO `assignments` (`id`, `asset_id`, `employee_name`, `department`, `assign_date`, `expected_return`, `status`, `assigned_date`, `assign_id`) VALUES
(1, 2, 'Blando', 'IT', '2026-04-23', NULL, 'ACTIVE', NULL, 0),
(2, 3, 'noella', 'IT', '2026-04-23', NULL, 'ACTIVE', NULL, 0),
(3, 5, 'lea', 'IT', '2026-04-25', NULL, 'ACTIVE', NULL, 0),
(5, 1, 'brice', 'IT', '2026-04-26', NULL, 'ACTIVE', NULL, 0);

-- --------------------------------------------------------

--
-- Table structure for table `audit_logs`
--

CREATE TABLE `audit_logs` (
  `id` int(11) NOT NULL,
  `asset_id` int(11) DEFAULT NULL,
  `action` varchar(100) DEFAULT NULL,
  `action_date` datetime DEFAULT NULL,
  `user` varchar(100) DEFAULT NULL,
  `log_id` int(11) NOT NULL,
  `done_by` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `audit_logs`
--

INSERT INTO `audit_logs` (`id`, `asset_id`, `action`, `action_date`, `user`, `log_id`, `done_by`) VALUES
(2, 3, 'Assigned to noella in IT', '2026-04-23 21:55:11', 'admin', 0, NULL),
(3, 3, 'Status updated to AVAILABLE', '2026-04-23 21:57:43', 'admin', 0, NULL),
(5, 5, 'Assigned to lea in IT', '2026-04-24 13:54:48', 'admin', 0, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `departments`
--

CREATE TABLE `departments` (
  `dept_id` int(11) NOT NULL,
  `dept_name` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `departments`
--

INSERT INTO `departments` (`dept_id`, `dept_name`) VALUES
(1, 'HR'),
(2, 'Finance'),
(3, 'IT'),
(4, 'Sales'),
(5, 'Operations');

-- --------------------------------------------------------

--
-- Table structure for table `returns`
--

CREATE TABLE `returns` (
  `id` int(11) NOT NULL,
  `asset_id` int(11) NOT NULL,
  `return_date` date NOT NULL,
  `condition_status` varchar(100) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `returns`
--

INSERT INTO `returns` (`id`, `asset_id`, `return_date`, `condition_status`, `status`) VALUES
(1, 2, '2026-04-23', 'it was stolen', 'RETURNED'),
(2, 3, '2026-04-23', 'noella didn\'t pay for it', 'RETURNED');

-- --------------------------------------------------------

--
-- Table structure for table `returns_table`
--

CREATE TABLE `returns_table` (
  `return_id` int(11) NOT NULL,
  `assign_id` int(11) DEFAULT NULL,
  `return_date` date DEFAULT NULL,
  `condition_on_return` varchar(50) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `fullname` varchar(100) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `fullname`, `username`, `password`, `role`) VALUES
(1, 'System Admin', 'admin', 'admin123', 'Admin'),
(2, 'nsabimana', 'brice', '', ''),
(3, 'uwajeneza', 'noella', 'airtel123', 'User');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `assets`
--
ALTER TABLE `assets`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `assignments`
--
ALTER TABLE `assignments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `asset_id` (`asset_id`);

--
-- Indexes for table `audit_logs`
--
ALTER TABLE `audit_logs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `asset_id` (`asset_id`);

--
-- Indexes for table `departments`
--
ALTER TABLE `departments`
  ADD PRIMARY KEY (`dept_id`);

--
-- Indexes for table `returns`
--
ALTER TABLE `returns`
  ADD PRIMARY KEY (`id`),
  ADD KEY `asset_id` (`asset_id`);

--
-- Indexes for table `returns_table`
--
ALTER TABLE `returns_table`
  ADD PRIMARY KEY (`return_id`),
  ADD KEY `assign_id` (`assign_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `assets`
--
ALTER TABLE `assets`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `assignments`
--
ALTER TABLE `assignments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `audit_logs`
--
ALTER TABLE `audit_logs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `departments`
--
ALTER TABLE `departments`
  MODIFY `dept_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `returns`
--
ALTER TABLE `returns`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `returns_table`
--
ALTER TABLE `returns_table`
  MODIFY `return_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `assignments`
--
ALTER TABLE `assignments`
  ADD CONSTRAINT `assignments_ibfk_1` FOREIGN KEY (`asset_id`) REFERENCES `assets` (`id`);

--
-- Constraints for table `audit_logs`
--
ALTER TABLE `audit_logs`
  ADD CONSTRAINT `audit_logs_ibfk_1` FOREIGN KEY (`asset_id`) REFERENCES `assets` (`id`);

--
-- Constraints for table `returns`
--
ALTER TABLE `returns`
  ADD CONSTRAINT `returns_ibfk_1` FOREIGN KEY (`asset_id`) REFERENCES `assets` (`id`);

--
-- Constraints for table `returns_table`
--
ALTER TABLE `returns_table`
  ADD CONSTRAINT `returns_table_ibfk_1` FOREIGN KEY (`assign_id`) REFERENCES `assignments` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1:3307
-- Thời gian đã tạo: Th10 07, 2024 lúc 03:11 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `library_management_system_uet`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `admins`
--

CREATE TABLE `admins` (
  `email` varchar(40) NOT NULL,
  `password` varchar(28) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `admins`
--

INSERT INTO `admins` (`email`, `password`) VALUES
('admin', '1');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `books`
--

CREATE TABLE `books` (
  `id` int(11) NOT NULL,
  `isbn` varchar(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `author` varchar(255) DEFAULT NULL,
  `publisher` varchar(60) DEFAULT NULL,
  `addedDate` datetime DEFAULT current_timestamp(),
  `description` text DEFAULT NULL,
  `linkCoverImage` text DEFAULT NULL,
  `lastUpdateDate` datetime DEFAULT current_timestamp(),
  `avgRate` decimal(3,2) DEFAULT NULL,
  `language` varchar(40) NOT NULL DEFAULT 'Unknown',
  `publisherDate` datetime DEFAULT NULL,
  `pageCount` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `books`
--

INSERT INTO `books` (`id`, `isbn`, `name`, `author`, `publisher`, `addedDate`, `description`, `linkCoverImage`, `lastUpdateDate`, `avgRate`, `language`, `publisherDate`, `pageCount`) VALUES
(100000, '0', 'test', NULL, NULL, '2024-10-21 23:48:41', NULL, '', NULL, NULL, 'Unknown', NULL, NULL),
(100001, '1', 'hello world', NULL, NULL, '2024-10-22 11:29:16', NULL, NULL, '2024-10-22 11:29:16', NULL, 'Unknown', NULL, NULL),
(100002, '2', 'g', NULL, NULL, '2024-10-22 11:29:16', NULL, NULL, '2024-10-22 11:29:16', NULL, 'Unknown', NULL, NULL),
(100003, '3', 'f', NULL, NULL, '2024-10-22 11:29:16', NULL, NULL, '2024-10-22 11:29:16', NULL, 'Unknown', NULL, NULL),
(100004, '1', 'a b', NULL, NULL, '2024-10-22 11:31:49', NULL, NULL, '2024-10-22 11:31:49', NULL, 'Unknown', NULL, NULL),
(100005, '2', 'a c', NULL, NULL, '2024-10-22 11:31:49', NULL, NULL, '2024-10-22 11:31:49', NULL, 'Unknown', NULL, NULL),
(100006, '3', 'a c', NULL, NULL, '2024-10-22 11:31:49', NULL, NULL, '2024-10-22 11:31:49', NULL, 'Unknown', NULL, NULL),
(100007, 'CHI:096622646', 'Report', 'Michigan. Adjutant-General\'s Office', 'Unknown', '2024-10-23 23:49:25', 'Unknown', 'http://books.google.com/books/content?id=H09GAQAAMAAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '2024-10-23 23:49:25', 0.00, 'en', NULL, 278),
(100008, '9781472136886', 'Sherlock Holmes\'s School for Detection', 'Simon Clark', 'Hachette UK', '2024-10-23 23:53:01', 'It\'s 1890. Sherlock Holmes and Doctor Watson return to Baker Street after a night pursuing a vicious criminal. Inspector Lestrade is waiting for Holmes with a proposition of national importance. Lestrade tells Holmes that a school of detection has been formed to train a new breed of modern investigators that will serve in Great Britain and the Empire. Most students will become police officers. Some, however, will become bodyguards and spies. Holmes begins instructing his decidedly curious assortment of students from home and abroad. He does so with his customary gusto and inventiveness. Scotland Yard, in the main, allocates crimes to solve and Holmes mentors his students. Occasionally, he shadows them in disguise in order to assess or even directly test their abilities with creative scenarios he devises. Certain crimes investigated by the students might appear trivial, such as the re-positioning of an ornament atop a garden wall, yet it will transpire an assassin has moved the ornament to create good sightlines in order to commit murder with a sniper\'s rifle. Other mysteries are considered outside the domain of the police. For example, the inexplicable disappearance of a stone gargoyle, which is linked to an ancient family curse. Or a man suffering from amnesia who discovers that not only has he acquired a secret life but also gained an implacable enemy, too. Holmes, with the ever- trustworthy Doctor Watson in his wake, is kept busy with his students\' cases, ranging from minor to serious, sometimes rectifying their mistakes and saving them from a variety of disasters. These eleven wonderful new adventures and intrigues include tales such as \'The Gargoyles of Killfellen House\', \'Sherlock Holmes and the Four Kings of Sweden\' and \'The Case of the Cannibal Club\'.', 'http://books.google.com/books/content?id=6BsvDQAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '2024-10-23 23:53:01', 0.00, 'en', NULL, 412),
(100009, '9781472136886', 'Sherlock Holmes\'s School for Detection', 'Simon Clark', 'Hachette UK', '2024-10-23 23:55:25', 'It\'s 1890. Sherlock Holmes and Doctor Watson return to Baker Street after a night pursuing a vicious criminal. Inspector Lestrade is waiting for Holmes with a proposition of national importance. Lestrade tells Holmes that a school of detection has been formed to train a new breed of modern investigators that will serve in Great Britain and the Empire. Most students will become police officers. Some, however, will become bodyguards and spies. Holmes begins instructing his decidedly curious assortment of students from home and abroad. He does so with his customary gusto and inventiveness. Scotland Yard, in the main, allocates crimes to solve and Holmes mentors his students. Occasionally, he shadows them in disguise in order to assess or even directly test their abilities with creative scenarios he devises. Certain crimes investigated by the students might appear trivial, such as the re-positioning of an ornament atop a garden wall, yet it will transpire an assassin has moved the ornament to create good sightlines in order to commit murder with a sniper\'s rifle. Other mysteries are considered outside the domain of the police. For example, the inexplicable disappearance of a stone gargoyle, which is linked to an ancient family curse. Or a man suffering from amnesia who discovers that not only has he acquired a secret life but also gained an implacable enemy, too. Holmes, with the ever- trustworthy Doctor Watson in his wake, is kept busy with his students\' cases, ranging from minor to serious, sometimes rectifying their mistakes and saving them from a variety of disasters. These eleven wonderful new adventures and intrigues include tales such as \'The Gargoyles of Killfellen House\', \'Sherlock Holmes and the Four Kings of Sweden\' and \'The Case of the Cannibal Club\'.', 'http://books.google.com/books/content?id=6BsvDQAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '2024-10-23 23:55:25', 0.00, 'en', NULL, 412),
(100010, 'UOM:39015086851550', 'Stories of Sherlock Holmes', 'Arthur Conan Doyle', 'Unknown', '2024-10-24 20:47:12', 'Unknown', 'http://books.google.com/books/content?id=ewI-AQAAMAAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', '2024-10-24 20:47:12', 0.00, 'en', NULL, 330),
(100011, '0486270556', 'Six Great Sherlock Holmes Stories', 'Arthur Conan Doyle', 'Courier Corporation', '2024-10-25 13:40:07', 'A selection of six of the finest of the Sherlock Holmes mystery stories.', 'http://books.google.com/books/content?id=dnQz427Lc0wC&printsec=frontcover&img=1&zoom=1&source=gbs_api', '2024-10-25 13:40:07', 4.00, 'en', NULL, 116);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(12) NOT NULL,
  `password` varchar(30) NOT NULL,
  `name` varchar(28) DEFAULT NULL,
  `email` varchar(40) NOT NULL,
  `phonenumber` varchar(10) NOT NULL,
  `question` varchar(60) DEFAULT NULL,
  `answer` varchar(30) DEFAULT NULL,
  `registered_date` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `name`, `email`, `phonenumber`, `question`, `answer`, `registered_date`) VALUES
(1, 'tohuyy', 'Hiban1411@', NULL, 'toh@gmail.com', '1234567891', NULL, NULL, '2024-10-10 11:26:10'),
(2, 'hhhhuyy', 'Hiban1411@', NULL, 'tohuy2@gmail.com', '0123222222', NULL, NULL, '2024-10-10 11:28:34'),
(5, 'hibann', 'Hiban1411@', NULL, 'hi@gmail.com', '1234567892', NULL, NULL, '2024-10-10 11:29:53'),
(6, 'thuyaa', 'Hiban1411@', NULL, 'a@gmail.com', '0000000000', NULL, NULL, '2024-10-10 11:30:56'),
(7, 'tohuy2710', 'Hiban1411@', NULL, 'huy2710@gmail.com', 'Huy2710', NULL, NULL, '2024-10-10 11:41:00'),
(8, 'gogogogo', 'Hiban1411@', NULL, 'go@hggg.com', '0987654678', NULL, NULL, '2024-10-10 15:07:00'),
(9, 'h', '2', NULL, '1', '1', NULL, NULL, '2024-10-10 15:24:33'),
(10, 'thuyyy', 'Huy2710@', NULL, 'thy@gmail.com', '0989789879', 'How many of ex do you have?', '0', '2024-10-10 16:03:15'),
(11, 'toquanghuy', 'Hiban1411@', NULL, 'th2710@gmail.com', '0376868627', 'How many of ex do you have?', '0', '2024-10-10 20:58:26'),
(12, 'lehsan', 'San1111@', NULL, 'san@gmail.com', '0989898989', 'Who is your crush?', 'huy', '2024-10-11 07:18:16'),
(13, 'Manh123', 'Manh123@#', NULL, 'manh123@mgia.com', '0123456123', 'How many of ex do you have?', '0', '2024-10-11 22:15:59');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `usersrequest`
--

CREATE TABLE `usersrequest` (
  `id` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `bookId` int(11) NOT NULL,
  `createdTime` datetime NOT NULL DEFAULT current_timestamp(),
  `lastUpdatedTime` datetime NOT NULL DEFAULT current_timestamp(),
  `status` enum('AC','DE','CA','EX','OD','CP','RS') NOT NULL DEFAULT 'RS'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `usersrequest`
--

INSERT INTO `usersrequest` (`id`, `userId`, `bookId`, `createdTime`, `lastUpdatedTime`, `status`) VALUES
(14, 1, 100003, '2023-11-07 10:30:00', '2023-11-07 10:30:00', 'RS'),
(15, 2, 100004, '2023-11-07 11:00:00', '2023-11-07 11:00:00', 'AC'),
(16, 5, 100007, '2023-11-07 12:30:00', '2023-11-07 12:30:00', 'EX'),
(17, 6, 100008, '2023-11-07 13:00:00', '2023-11-07 13:00:00', 'OD'),
(18, 7, 100009, '2023-11-07 13:30:00', '2023-11-07 13:30:00', 'CP'),
(19, 8, 100010, '2023-11-07 14:00:00', '2023-11-07 14:00:00', 'RS'),
(20, 9, 100011, '2023-11-07 14:30:00', '2023-11-07 14:30:00', 'AC'),
(21, 10, 100003, '2023-11-07 15:00:00', '2023-11-07 15:00:00', 'DE'),
(22, 11, 100004, '2023-11-07 15:30:00', '2023-11-07 15:30:00', 'CA'),
(23, 12, 100005, '2023-11-07 16:00:00', '2023-11-07 16:00:00', 'EX'),
(24, 13, 100006, '2023-11-07 16:30:00', '2023-11-07 16:30:00', 'OD');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `indexes_username` (`username`),
  ADD UNIQUE KEY `indexes_email` (`email`),
  ADD UNIQUE KEY `indexes_phonenumber` (`phonenumber`);

--
-- Chỉ mục cho bảng `usersrequest`
--
ALTER TABLE `usersrequest`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_userid` (`userId`),
  ADD KEY `fk_bookid` (`bookId`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `books`
--
ALTER TABLE `books`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=100012;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT cho bảng `usersrequest`
--
ALTER TABLE `usersrequest`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `usersrequest`
--
ALTER TABLE `usersrequest`
  ADD CONSTRAINT `fk_bookid` FOREIGN KEY (`bookId`) REFERENCES `books` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_userid` FOREIGN KEY (`userId`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th10 21, 2024 lúc 05:48 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `library`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `admins`
--

CREATE TABLE `admins` (
  `Full name` varchar(50) NOT NULL,
  `Email` varchar(50) NOT NULL,
  `Password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `admins`
--

INSERT INTO `admins` (`Full name`, `Email`, `Password`) VALUES
('', '', 'aaa'),
('', 'dien@gmail.com', 'dien'),
('Hoang Dung', 'dung@gmail.com', '123456'),
('', 'duong@gmail.com', 'duong'),
('Hoang', 'hoang@gmail.com', '12345');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `book`
--

CREATE TABLE `book` (
  `isbn` varchar(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `author` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `book`
--

INSERT INTO `book` (`isbn`, `name`, `author`, `quantity`) VALUES
('978-0-123456-01-1', 'To Kill a Mockingbird', 'Harper Lee', 4),
('978-0-123456-02-2', '1984', 'George Orwell', 19),
('978-0-123456-03-3', 'The Great Gatsby', 'F. Scott Fitzgerald', 10),
('978-0-123456-04-4', 'Pride and Prejudice', 'Jane Austen', 25),
('978-0-123456-05-5', 'The Catcher in the Rye', 'J.D. Salinger', 12),
('978-0-123456-06-6', 'Moby Dick', 'Herman Melville', 17),
('978-0-123456-07-7', 'The Lord of the Rings', 'J.R.R. Tolkien', 30),
('978-0-123456-08-8', 'Jane Eyre', 'Charlotte Brontë', 22),
('978-0-123456-09-9', 'The Chronicles of Narnia', 'C.S. Lewis', 27),
('978-0-123456-10-0', 'The Hobbit', 'J.R.R. Tolkien', 35),
('978-0-123456-11-1', 'War and Peace', 'Leo Tolstoy', 40),
('978-0-123456-12-2', 'Ulysses', 'James Joyce', 15),
('978-0-123456-13-3', 'Crime and Punishment', 'Fyodor Dostoevsky', 12),
('978-0-123456-14-4', 'The Odyssey', 'Homer', 18),
('978-0-123456-15-5', 'The Iliad', 'Homer', 22),
('978-0-123456-16-6', 'Anna Karenina', 'Leo Tolstoy', 30),
('978-0-123456-17-7', 'A Tale of Two Cities', 'Charles Dickens', 25),
('978-0-123456-18-8', 'Great Expectations', 'Charles Dickens', 20),
('978-0-123456-19-9', 'Wuthering Heights', 'Emily Brontë', 28),
('978-0-123456-20-0', 'Don Quixote', 'Miguel de Cervantes', 35),
('978-0-123456-21-1', 'Les Misérables', 'Victor Hugo', 11),
('978-0-123456-22-2', 'The Brothers Karamazov', 'Fyodor Dostoevsky', 15),
('978-0-123456-23-3', 'The Divine Comedy', 'Dante Alighieri', 10),
('978-0-123456-24-4', 'Madame Bovary', 'Gustave Flaubert', 14),
('978-0-123456-25-5', 'One Hundred Years of Solitude', 'Gabriel García Márquez', 19),
('978-0-123456-26-6', 'The Sound and the Fury', 'William Faulkner', 11),
('978-0-123456-27-7', 'Beloved', 'Toni Morrison', 13),
('978-0-123456-28-8', 'Middlemarch', 'George Eliot', 17),
('978-0-123456-29-9', 'Frankenstein', 'Mary Shelley', 20),
('978-0-123456-30-0', 'Dracula', 'Bram Stoker', 30),
('978-0-123456-31-1', 'The Scarlet Letter', 'Nathaniel Hawthorne', 15),
('978-0-123456-32-2', 'Heart of Darkness', 'Joseph Conrad', 18),
('978-0-123456-33-3', 'The Stranger', 'Albert Camus', 22),
('978-0-123456-34-4', 'Brave New World', 'Aldous Huxley', 25),
('978-0-123456-35-5', 'The Kite Runner', 'Khaled Hosseini', 28),
('978-0-123456-36-6', 'Memoirs of a Geisha', 'Arthur Golden', 32),
('978-0-123456-37-7', 'Life of Pi', 'Yann Martel', 14),
('978-0-123456-38-8', 'The Road', 'Cormac McCarthy', 20),
('978-0-123456-39-9', 'The Book Thief', 'Markus Zusak', 35),
('978-0-123456-40-0', 'Harry Potter and the Philosopher\'s Stone', 'J.K. Rowling', 50),
('978-0-123456-41-1', 'The Hunger Games', 'Suzanne Collins', 45),
('978-0-123456-42-2', 'Dune', 'Frank Herbert', 18),
('978-0-123456-43-3', 'Fahrenheit 451', 'Ray Bradbury', 25),
('978-0-123456-44-4', 'The Shining', 'Stephen King', 20),
('978-0-123456-45-5', 'Carrie', 'Stephen King', 18),
('978-0-123456-46-6', 'It', 'Stephen King', 15),
('978-0-123456-47-7', 'Misery', 'Stephen King', 12),
('978-0-123456-48-8', 'The Stand', 'Stephen King', 20),
('978-0-123456-49-9', 'Pet Sematary', 'Stephen King', 18),
('978-0-123456-50-0', 'The Alchemist', 'Paulo Coelho', 40);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `student`
--

CREATE TABLE `student` (
  `ID` int(11) NOT NULL,
  `Name` varchar(100) DEFAULT NULL,
  `University` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `student`
--

INSERT INTO `student` (`ID`, `Name`, `University`) VALUES
(1, 'Nguyễn Văn An', 'Đại học Quốc gia Hà Nội'),
(2, 'Trần Thị Bích', 'Đại học Quốc gia Thành phố Hồ Chí Minh'),
(3, 'Lê Văn Cường', 'Trường Đại học Bách Khoa Hà Nội'),
(4, 'Phạm Thị Dung', 'Trường Đại học Kinh tế Quốc dân'),
(5, 'Hoàng Văn Đức', 'Trường Đại học Ngoại thương Hà Nội'),
(6, 'Vũ Thị Hoa', 'Trường Đại học Sư phạm Hà Nội'),
(7, 'Đỗ Văn Minh', 'Trường Đại học Thủy lợi'),
(8, 'Bùi Thị Hạnh', 'Trường Đại học Giao thông Vận tải Hà Nội'),
(9, 'Ngô Văn Hải', 'Trường Đại học Y Hà Nội'),
(10, 'Dương Thị Lan', 'Trường Đại học Luật Hà Nội'),
(11, 'Nguyễn Thị Mai', 'Trường Đại học Công nghệ thông tin và Truyền thông Thái Nguyên'),
(12, 'Lê Văn Khánh', 'Trường Đại học Nông nghiệp Hà Nội'),
(13, 'Phạm Thị Huệ', 'Trường Đại học Công nghiệp Hà Nội'),
(14, 'Hoàng Thị Hà', 'Trường Đại học Văn hóa Hà Nội'),
(15, 'Nguyễn Văn Thành', 'Trường Đại học Kiến trúc Hà Nội'),
(16, 'Trần Thị Hương', 'Trường Đại học Mỏ - Địa chất'),
(17, 'Đặng Văn Sơn', 'Trường Đại học Đà Nẵng'),
(18, 'Lê Thị Tuyết', 'Trường Đại học Tây Nguyên'),
(19, 'Phạm Minh Tuấn', 'Trường Đại học Huế'),
(20, 'Nguyễn Thị Ngọc', 'Trường Đại học Cần Thơ'),
(21, 'Lê Thị Hồng', 'Trường Đại học Sư phạm Thành phố Hồ Chí Minh'),
(22, 'Hoàng Văn Hùng', 'Trường Đại học Kinh tế Thành phố Hồ Chí Minh'),
(23, 'Nguyễn Thị Hạnh', 'Trường Đại học Y Dược Thành phố Hồ Chí Minh'),
(24, 'Phạm Văn Long', 'Trường Đại học Khoa học Tự nhiên Thành phố Hồ Chí Minh'),
(25, 'Đặng Văn Phúc', 'Trường Đại học Tôn Đức Thắng'),
(26, 'Nguyễn Văn Đạt', 'Trường Đại học Quốc tế Thành phố Hồ Chí Minh'),
(27, 'Bùi Thị Tâm', 'Trường Đại học Kinh tế - Luật Thành phố Hồ Chí Minh'),
(28, 'Đỗ Thị Thảo', 'Trường Đại học Hùng Vương'),
(29, 'Nguyễn Thị Diễm', 'Trường Đại học Công nghiệp Thực phẩm Thành phố Hồ Chí Minh'),
(30, 'Trần Văn Kiên', 'Trường Đại học Tài chính - Marketing'),
(31, 'Lê Thị Kim', 'Trường Đại học Ngân hàng Thành phố Hồ Chí Minh'),
(32, 'Phạm Văn Hòa', 'Trường Đại học Mở Thành phố Hồ Chí Minh'),
(33, 'Hoàng Minh Anh', 'Trường Đại học Sài Gòn'),
(34, 'Nguyễn Hoàng Hải', 'Trường Đại học Hoa Sen'),
(35, 'Phạm Thị Lan Anh', 'Trường Đại học Việt Đức'),
(36, 'Lê Thị Mỹ Linh', 'Trường Đại học Văn Lang'),
(37, 'Trần Quốc Vương', 'Trường Đại học Quốc tế RMIT'),
(38, 'Hoàng Văn Trung', 'Trường Đại học Công nghệ Thành phố Hồ Chí Minh (HUTECH)'),
(39, 'Phạm Văn Đông', 'Trường Đại học Duy Tân'),
(40, 'Nguyễn Thị Thu Hà', 'Trường Đại học Đông Á'),
(41, 'Lê Quốc Anh', 'Trường Đại học FPT'),
(42, 'Phạm Thị Quỳnh', 'Trường Đại học Kinh tế Đà Nẵng'),
(43, 'Nguyễn Thị Hồng Nhung', 'Trường Đại học Sư phạm Đà Nẵng'),
(44, 'Trần Văn Nam', 'Trường Đại học Vinh'),
(45, 'Đặng Thị Lệ', 'Trường Đại học Quy Nhơn'),
(46, 'Nguyễn Văn Lâm', 'Trường Đại học Nha Trang'),
(47, 'Phạm Văn Tài', 'Trường Đại học Tài nguyên và Môi trường Hà Nội'),
(48, 'Trần Thị Yến', 'Trường Đại học Tài nguyên và Môi trường Thành phố Hồ Chí Minh'),
(49, 'Đặng Thị Hiền', 'Trường Đại học Phú Yên'),
(50, 'Lê Thị Giang', 'Trường Đại học Khánh Hòa'),
(51, 'Nguyễn Quốc Khánh', 'Trường Đại học Hồng Đức'),
(52, 'Hoàng Văn Lộc', 'Trường Đại học Thái Bình'),
(53, 'Phạm Thị Hương', 'Trường Đại học Nam Cần Thơ'),
(54, 'Ngô Văn Phú', 'Trường Đại học Bình Dương'),
(55, 'Nguyễn Văn Tài', 'Trường Đại học An Giang'),
(56, 'Đỗ Văn Tùng', 'Trường Đại học Trà Vinh'),
(57, 'Nguyễn Thị Ngọc Hà', 'Trường Đại học Tiền Giang'),
(58, 'Lê Văn Toàn', 'Trường Đại học Công nghệ Đồng Nai'),
(59, 'Hoàng Văn Việt', 'Trường Đại học Đồng Tháp'),
(60, 'Nguyễn Văn Nghĩa', 'Trường Đại học Bạc Liêu'),
(61, 'Phạm Thị Tuyết Nhung', 'Trường Đại học Võ Trường Toản'),
(62, 'Lê Quốc Toàn', 'Trường Đại học Kỹ thuật - Công nghệ Cần Thơ'),
(63, 'Nguyễn Văn Đình', 'Trường Đại học Cửu Long'),
(64, 'Trần Quốc Khánh', 'Trường Đại học Quốc tế Miền Đông'),
(65, 'Đỗ Thị Mai', 'Trường Đại học Yersin Đà Lạt'),
(66, 'Lê Văn Bình', 'Trường Đại học Lạc Hồng'),
(67, 'Nguyễn Thị Hoa', 'Trường Đại học Tây Đô'),
(68, 'Phạm Văn Lâm', 'Trường Đại học Thủ Dầu Một');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `student issued book`
--

CREATE TABLE `student issued book` (
  `Student ID` int(11) NOT NULL,
  `ISBN` varchar(20) NOT NULL,
  `Issue date` date NOT NULL,
  `Due date` date NOT NULL,
  `STATUS` varchar(21) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `student issued book`
--

INSERT INTO `student issued book` (`Student ID`, `ISBN`, `Issue date`, `Due date`, `STATUS`) VALUES
(1, '978-0-123456-01-1', '2024-11-01', '2024-11-23', 'Borrowing'),
(2, '978-0-123456-01-1', '2024-11-01', '2024-11-23', 'Borrowing'),
(3, '978-0-123456-02-2 ', '2024-11-01', '2024-11-23', 'Borrowing'),
(5, '978-0-123456-06-6', '2024-11-01', '2024-11-23', 'Borrowing'),
(6, '978-0-123456-21-1', '2024-11-01', '2024-10-11', 'Borrowing');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`Email`);

--
-- Chỉ mục cho bảng `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`isbn`);

--
-- Chỉ mục cho bảng `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `student`
--
ALTER TABLE `student`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=69;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

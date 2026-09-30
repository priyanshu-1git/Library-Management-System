-- =====================================================
-- Library Management System - Demo Data
-- =====================================================

-- Clear existing data
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE issued_books;
TRUNCATE TABLE books;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- Sample Data - Users
-- =====================================================
INSERT INTO users (user_id, username, password, full_name, email, role, student_id) VALUES
(1, 'admin', 'Admin123', 'System Administrator', 'admin@library.com', 'ADMIN', NULL),
(2, 'priyanshu', 'Priyanshu123', 'Priyanshu', 'priyanshu@student.com', 'STUDENT', 'STU0002'),
(3, 'vaibhav', 'Vaibhav123', 'Vaibhav', 'vaibhav@student.com', 'STUDENT', 'STU0003'),
(4, 'kshitij', 'Kshitij123', 'Kshitij', 'kshitij@student.com', 'STUDENT', 'STU0004'),
(5, 'developer', 'developer123', 'Developer', 'dev@example.com', 'DEVELOPER', NULL);

-- =====================================================
-- Sample Data - Books (~50 realistic books)
-- =====================================================
INSERT INTO books (book_id, title, author, isbn, publisher, publication_year, category, total_copies, available_copies) VALUES
(1, 'Clean Code', 'Robert C. Martin', '978-0132350884', 'Prentice Hall', 2008, 'Programming', 5, 5),
(2, 'Effective Java', 'Joshua Bloch', '978-0134685991', 'Addison-Wesley', 2017, 'Programming', 3, 3),
(3, 'Design Patterns', 'Gang of Four', '978-0201633612', 'Addison-Wesley', 1994, 'Software Engineering', 4, 4),
(4, 'Head First Java', 'Kathy Sierra', '978-0596009205', 'O Reilly Media', 2005, 'Programming', 6, 6),
(5, 'Java: The Complete Reference', 'Herbert Schildt', '978-1260440232', 'McGraw-Hill', 2018, 'Programming', 4, 4),
(6, 'Introduction to Algorithms', 'Thomas H. Cormen', '978-0262033844', 'MIT Press', 2009, 'Computer Science', 3, 3),
(7, 'Database System Concepts', 'Abraham Silberschatz', '978-0078022159', 'McGraw-Hill', 2019, 'Database', 3, 3),
(8, 'Computer Networks', 'Andrew S. Tanenbaum', '978-0132126953', 'Pearson', 2010, 'Networking', 2, 2),
(9, 'Operating System Concepts', 'Abraham Silberschatz', '978-1118063330', 'Wiley', 2012, 'Operating Systems', 3, 3),
(10, 'Artificial Intelligence', 'Stuart Russell', '978-0136042594', 'Pearson', 2009, 'AI', 2, 2),
(11, 'The Pragmatic Programmer', 'Andrew Hunt', '978-0135957059', 'Addison-Wesley', 1999, 'Programming', 5, 5),
(12, 'Code Complete', 'Steve McConnell', '978-0735619678', 'Microsoft Press', 2004, 'Software Engineering', 4, 4),
(13, 'Structure and Interpretation of Computer Programs', 'Harold Abelson', '978-0262510875', 'MIT Press', 1996, 'Computer Science', 2, 2),
(14, 'Refactoring', 'Martin Fowler', '978-0134757599', 'Addison-Wesley', 2018, 'Programming', 3, 3),
(15, 'Domain-Driven Design', 'Eric Evans', '978-0321125217', 'Addison-Wesley', 2003, 'Software Engineering', 3, 3),
(16, 'Working Effectively with Legacy Code', 'Michael Feathers', '978-0131177055', 'Prentice Hall', 2004, 'Programming', 2, 2),
(17, 'Patterns of Enterprise Application Architecture', 'Martin Fowler', '978-0321127426', 'Addison-Wesley', 2002, 'Software Engineering', 4, 4),
(18, 'Designing Data-Intensive Applications', 'Martin Kleppmann', '978-1449373320', 'O Reilly Media', 2017, 'Database', 6, 6),
(19, 'The Mythical Man-Month', 'Frederick P. Brooks Jr.', '978-0201835953', 'Addison-Wesley', 1995, 'Software Engineering', 2, 2),
(20, 'Cracking the Coding Interview', 'Gayle Laakmann McDowell', '978-0984782857', 'CareerCup', 2015, 'Computer Science', 8, 8),
(21, 'Grokking Algorithms', 'Aditya Bhargava', '978-1617292231', 'Manning', 2016, 'Computer Science', 5, 5),
(22, 'Elements of Reusable Object-Oriented Software', 'Erich Gamma', '978-0201633613', 'Addison-Wesley', 1994, 'Software Engineering', 4, 4),
(23, 'Clean Architecture', 'Robert C. Martin', '978-0134494166', 'Prentice Hall', 2017, 'Software Engineering', 5, 5),
(24, 'JavaScript: The Good Parts', 'Douglas Crockford', '978-0596517748', 'O Reilly Media', 2008, 'Programming', 3, 3),
(25, 'Eloquent JavaScript', 'Marijn Haverbeke', '978-1593279509', 'No Starch Press', 2018, 'Programming', 4, 4),
(26, 'You Don''t Know JS', 'Kyle Simpson', '978-1491904152', 'O Reilly Media', 2014, 'Programming', 3, 3),
(27, 'Python Crash Course', 'Eric Matthes', '978-1593279288', 'No Starch Press', 2019, 'Programming', 6, 6),
(28, 'Fluent Python', 'Luciano Ramalho', '978-1491946008', 'O Reilly Media', 2015, 'Programming', 4, 4),
(29, 'Automate the Boring Stuff with Python', 'Al Sweigart', '978-1593279929', 'No Starch Press', 2019, 'Programming', 5, 5),
(30, 'C Programming Language', 'Brian W. Kernighan', '978-0131103627', 'Prentice Hall', 1988, 'Programming', 2, 2),
(31, 'The C++ Programming Language', 'Bjarne Stroustrup', '978-0321563842', 'Addison-Wesley', 2013, 'Programming', 3, 3),
(32, 'Effective C++', 'Scott Meyers', '978-0321334879', 'Addison-Wesley', 2005, 'Programming', 3, 3),
(33, 'Deep Learning', 'Ian Goodfellow', '978-0262035613', 'MIT Press', 2016, 'AI', 4, 4),
(34, 'Machine Learning Yearning', 'Andrew Ng', '978-0999247101', 'DeepLearning.AI', 2018, 'AI', 3, 3),
(35, 'Hands-On Machine Learning', 'Aurélien Géron', '978-1492032649', 'O Reilly Media', 2019, 'AI', 5, 5),
(36, 'TCP/IP Illustrated', 'W. Richard Stevens', '978-0321336316', 'Addison-Wesley', 2011, 'Networking', 2, 2),
(37, 'Unix Network Programming', 'W. Richard Stevens', '978-0131411555', 'Prentice Hall', 2003, 'Networking', 2, 2),
(38, 'Database Management Systems', 'Raghu Ramakrishnan', '978-0072465631', 'McGraw-Hill', 2002, 'Database', 3, 3),
(39, 'SQL Performance Explained', 'Markus Winand', '978-3950307825', 'Markus Winand', 2012, 'Database', 3, 3),
(40, 'Seven Databases in Seven Weeks', 'Luc Perkins', '978-1680502534', 'Pragmatic Bookshelf', 2018, 'Database', 4, 4),
(41, 'Linux Kernel Development', 'Robert Love', '978-0672329463', 'Addison-Wesley', 2010, 'Operating Systems', 2, 2),
(42, 'Modern Operating Systems', 'Andrew S. Tanenbaum', '978-0133591620', 'Pearson', 2014, 'Operating Systems', 3, 3),
(43, 'Kubernetes Up & Running', 'Kelsey Hightower', '978-1492046530', 'O Reilly Media', 2019, 'Networking', 4, 4),
(44, 'Site Reliability Engineering', 'Betsy Beyer', '978-1491929124', 'O Reilly Media', 2016, 'Software Engineering', 3, 3),
(45, 'Continuous Delivery', 'Jez Humble', '978-0321601919', 'Addison-Wesley', 2010, 'Software Engineering', 3, 3),
(46, 'The DevOps Handbook', 'Gene Kim', '978-1942788003', 'IT Revolution Press', 2016, 'Software Engineering', 4, 4),
(47, 'Release It!', 'Michael T. Nygard', '978-1680502398', 'Pragmatic Bookshelf', 2018, 'Software Engineering', 3, 3),
(48, 'Building Microservices', 'Sam Newman', '978-1491950357', 'O Reilly Media', 2015, 'Software Engineering', 4, 4),
(49, 'Data Science from Scratch', 'Joel Grus', '978-1492041139', 'O Reilly Media', 2019, 'Computer Science', 5, 5),
(50, 'The Art of Computer Programming', 'Donald E. Knuth', '978-0201485417', 'Addison-Wesley', 2011, 'Computer Science', 2, 2);

-- =====================================================
-- Sample Data - Issued Books & History
-- =====================================================

-- Priyanshu (user_id = 2)
INSERT INTO issued_books (issue_id, book_id, user_id, issue_date, due_date, return_date, status, fine_amount) VALUES
(1, 4, 2, '2026-09-25', '2026-10-09', NULL, 'ISSUED', 0.00),  -- Head First Java (Book 4)
(2, 1, 2, '2026-09-01', '2026-09-15', NULL, 'ISSUED', 0.00),  -- Clean Code (Book 1) - OVERDUE
(3, 10, 2, '2026-07-01', '2026-07-10', '2026-07-10', 'RETURNED', 0.00); -- Artificial Intelligence (Book 10)

UPDATE books SET available_copies = available_copies - 1 WHERE book_id IN (4, 1);

-- Vaibhav (user_id = 3)
INSERT INTO issued_books (issue_id, book_id, user_id, issue_date, due_date, return_date, status, fine_amount) VALUES
(4, 18, 3, '2026-09-28', '2026-10-12', NULL, 'ISSUED', 0.00),  -- Designing Data-Intensive Applications (Book 18)
(5, 7, 3, '2026-09-20', '2026-10-04', NULL, 'ISSUED', 0.00),   -- Database System Concepts (Book 7)
(6, 5, 3, '2026-07-10', '2026-07-24', '2026-07-20', 'RETURNED', 0.00); -- Java: The Complete Reference (Book 5)

UPDATE books SET available_copies = available_copies - 1 WHERE book_id IN (18, 7);

-- Kshitij (user_id = 4)
INSERT INTO issued_books (issue_id, book_id, user_id, issue_date, due_date, return_date, status, fine_amount) VALUES
(7, 35, 4, '2026-09-01', '2026-09-20', NULL, 'ISSUED', 0.00),  -- Hands-On Machine Learning (Book 35) - OVERDUE
(8, 33, 4, '2026-09-05', '2026-09-20', NULL, 'ISSUED', 0.00),  -- Deep Learning (Book 33) - OVERDUE
(9, 49, 4, '2026-06-20', '2026-07-04', '2026-07-01', 'RETURNED', 0.00), -- Data Science from Scratch (Book 49)
(10, 42, 4, '2026-06-01', '2026-06-15', '2026-06-14', 'RETURNED', 0.00); -- Modern Operating Systems (Book 42)

UPDATE books SET available_copies = available_copies - 1 WHERE book_id IN (35, 33);

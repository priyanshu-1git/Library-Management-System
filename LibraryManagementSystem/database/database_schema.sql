-- =====================================================
-- Library Management System - Database Schema
-- =====================================================

-- Drop existing tables if they exist
DROP TABLE IF EXISTS issued_books;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;

-- =====================================================
-- Table: users
-- Stores information about system users (Admin & Student)
-- =====================================================
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role ENUM('ADMIN', 'STUDENT') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- Table: books
-- Stores information about library books
-- =====================================================
CREATE TABLE books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    publisher VARCHAR(100),
    publication_year INT,
    category VARCHAR(50),
    total_copies INT NOT NULL DEFAULT 1,
    available_copies INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_title (title),
    INDEX idx_author (author),
    INDEX idx_isbn (isbn),
    CHECK (available_copies >= 0),
    CHECK (available_copies <= total_copies)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- Table: issued_books
-- Tracks book issuing and return information
-- =====================================================
CREATE TABLE issued_books (
    issue_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    user_id INT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE DEFAULT NULL,
    status ENUM('ISSUED', 'RETURNED') NOT NULL DEFAULT 'ISSUED',
    fine_amount DECIMAL(10,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_book_id (book_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- Sample Data - Users
-- Password: admin123 and student123 (plain text for demo)
-- In production, use hashed passwords
-- =====================================================
INSERT INTO users (username, password, full_name, email, role) VALUES
('admin', 'admin123', 'System Administrator', 'admin@library.com', 'ADMIN'),
('john_doe', 'student123', 'John Doe', 'john.doe@student.com', 'STUDENT'),
('jane_smith', 'student123', 'Jane Smith', 'jane.smith@student.com', 'STUDENT'),
('mike_wilson', 'student123', 'Mike Wilson', 'mike.wilson@student.com', 'STUDENT');

-- =====================================================
-- Sample Data - Books
-- =====================================================
INSERT INTO books (title, author, isbn, publisher, publication_year, category, total_copies, available_copies) VALUES
('Clean Code', 'Robert C. Martin', '978-0132350884', 'Prentice Hall', 2008, 'Programming', 5, 5),
('Effective Java', 'Joshua Bloch', '978-0134685991', 'Addison-Wesley', 2017, 'Programming', 3, 3),
('Design Patterns', 'Gang of Four', '978-0201633612', 'Addison-Wesley', 1994, 'Software Engineering', 4, 4),
('Head First Java', 'Kathy Sierra', '978-0596009205', 'O Reilly Media', 2005, 'Programming', 6, 6),
('Java: The Complete Reference', 'Herbert Schildt', '978-1260440232', 'McGraw-Hill', 2018, 'Programming', 4, 4),
('Introduction to Algorithms', 'Thomas H. Cormen', '978-0262033844', 'MIT Press', 2009, 'Computer Science', 3, 3),
('Database System Concepts', 'Abraham Silberschatz', '978-0078022159', 'McGraw-Hill', 2019, 'Database', 3, 3),
('Computer Networks', 'Andrew S. Tanenbaum', '978-0132126953', 'Pearson', 2010, 'Networking', 2, 2),
('Operating System Concepts', 'Abraham Silberschatz', '978-1118063330', 'Wiley', 2012, 'Operating Systems', 3, 3),
('Artificial Intelligence', 'Stuart Russell', '978-0136042594', 'Pearson', 2009, 'AI', 2, 2);

-- =====================================================
-- Sample Data - Issued Books
-- =====================================================
INSERT INTO issued_books (book_id, user_id, issue_date, due_date, status) VALUES
(1, 2, '2024-12-15', '2024-12-29', 'ISSUED'),
(3, 3, '2024-12-20', '2025-01-03', 'ISSUED');

-- Update available copies for issued books
UPDATE books SET available_copies = available_copies - 1 WHERE book_id IN (1, 3);

-- =====================================================
-- Verification Queries
-- =====================================================
-- View all users
SELECT * FROM users;

-- View all books
SELECT * FROM books;

-- View issued books with details
SELECT 
    ib.issue_id,
    b.title,
    b.author,
    u.full_name,
    ib.issue_date,
    ib.due_date,
    ib.status
FROM issued_books ib
JOIN books b ON ib.book_id = b.book_id
JOIN users u ON ib.user_id = u.user_id;

-- =====================================================
-- End of Schema
-- =====================================================
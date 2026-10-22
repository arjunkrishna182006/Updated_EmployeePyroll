
CREATE DATABASE IF NOT EXISTS payroll_system_fresh_2026;

USE payroll_system_fresh_2026;

CREATE TABLE IF NOT EXISTS employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(100),
    designation VARCHAR(100),
    basic_salary DECIMAL(10,2) NOT NULL DEFAULT 0.00
);

CREATE TABLE IF NOT EXISTS salary_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    salary_month VARCHAR(7) NOT NULL,
    leave_days INT NOT NULL DEFAULT 0,
    basic_salary DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    deduction DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    tax_rate DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    tax_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    net_salary DECIMAL(10,2) NOT NULL DEFAULT 0.00,

    overtime_hours DECIMAL(7,2) NOT NULL DEFAULT 0.00,
    overtime_pay DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    performance_rating DECIMAL(3,2) NOT NULL DEFAULT 0.00,
    performance_bonus DECIMAL(10,2) NOT NULL DEFAULT 0.00,

    CONSTRAINT fk_salary_employee
        FOREIGN KEY (employee_id)
        REFERENCES employees(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_employee_month
        UNIQUE (employee_id, salary_month)
);

SHOW TABLES;
DESCRIBE employees;
DESCRIBE salary_records;

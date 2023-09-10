INSERT INTO wallet(name, type, balance)
VALUES
("Ví tiền", "Cash", 9000),
("Viettel Money", "Bank", 200000),
("MoMo", "Bank", 30000);

INSERT INTO income_category(name, description)
VALUES
("Tiền lương", "Tiền lương đi làm"),
("Tiền đòi nợ", "Tiền cho người khác mượn");

INSERT INTO expense_category(name, description, limit_amount)
VALUES
("Tiền sinh hoạt", "Tiền sinh tồn", 3000000),
("Tiền mua sắm", "Tiền chill", 1000000),
("Tiền vui chơi", "Tiền chill", NULL);

INSERT INTO target(name, description, target_amount, current_balance)
VALUES
("", "", 1500000, 120000);

INSERT INTO debt(full_name, description, debt_amount, phone)
VALUES
("Nguyễn A", "An Nhơn", 71000, "09xxxxxxxx"),
("Thị B", "Tây Sơn", -50000, "078xxxxxxx");

INSERT INTO trans(date_time, description, amount, type, source_wallet)
VALUES
("2023-08-01 15:13:12", "Rút tiền", 100000, "Transfer", 2),
("2023-08-02 17:34:56", "Rút tiền", 150000, "Transfer", 3),
("2023-08-03 19:14:19", "Ăn tối", 25000, "Expense", 1),
("2023-08-04 11:35:39", "Ăn trưa", 25000, "Expense", 2),
("2023-08-05 05:50:45", "Ăn sáng", 15000, "Expense", 1),
("2023-08-06 08:12:32", "Gửi tiền", 500000, "Transfer", 1),
("2023-08-07 21:31:47", "Tiền đòi nợ Nguyễn A", 250000, "Income", 1),
("2023-08-09 08:30:12", "Tiền lương", 30000, "Income", 2),
("2023-08-11 23:21:54", "Tiền mượn Thị B", 50000, "Income", 3);

INSERT INTO transfer_trans(id, target_wallet)
VALUES
(1, 1),
(2, 1),
(6, 2);

INSERT INTO income_trans(id, category_id, target_id, debt_id)
VALUES
(7, 2, NULL, 1),
(8, 1, NULL, NULL),
(9, 2, NULL, 2);

INSERT INTO expense_trans(id, category_id, target_id, debt_id)
VALUES
(3, 1, NULL, NULL),
(4, 1, NULL, NULL),
(5, 1, NULL, NULL);
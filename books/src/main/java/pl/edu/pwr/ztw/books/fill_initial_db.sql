
INSERT INTO authors(name) VALUES ('Henryk Sienkiewicz');
INSERT INTO authors(name) VALUES ('Stanisław Reymont');
INSERT INTO authors(name) VALUES ('Adam Mickiewicz');

INSERT INTO readers(name) VALUES ('Jan Kowalski');
INSERT INTO readers(name) VALUES ('Maria Nowak');

INSERT INTO books(title, pages, is_lent, author_id) VALUES ('Potop', 936, false, 1);
INSERT INTO books(title, pages, is_lent, author_id) VALUES ('Wesele', 150, false, 2);
INSERT INTO books(title, pages, is_lent, author_id) VALUES ('Dziady', 292, false, 3);

INSERT INTO lendings(book_id, reader_id, lending_date) VALUES (1, 1, '2023-03-15');
INSERT INTO lendings(book_id, reader_id, lending_date) VALUES (2, 2, '2023-03-16');

-- Book 1
INSERT INTO books (author, title, description, price, isbn, cover_image)
VALUES ('John Harvy', 'Cat', 'Happy Cat', 1.00, '123456788', '112234');

INSERT INTO book_category (book_id, category_id) VALUES
                                                     ((SELECT id FROM books WHERE isbn = '123456788'), 1),
                                                     ((SELECT id FROM books WHERE isbn = '123456788'), 2),
                                                     ((SELECT id FROM books WHERE isbn = '123456788'), 4);

-- Book 2
INSERT INTO books (author, title, description, price, isbn, cover_image)
VALUES ('Mark Harvy', 'Apple', 'Eat an apple', 1.00, '123456787', '112235');

INSERT INTO book_category (book_id, category_id) VALUES
                                                     ((SELECT id FROM books WHERE isbn = '123456787'), 1),
                                                     ((SELECT id FROM books WHERE isbn = '123456787'), 2),
                                                     ((SELECT id FROM books WHERE isbn = '123456787'), 5);

-- Book 3
INSERT INTO books (author, title, description, price, isbn, cover_image)
VALUES ('Hanna Wolly', 'Flower', 'Happy flower', 1.00, '123456786', '112236');

INSERT INTO book_category (book_id, category_id) VALUES
                                                     ((SELECT id FROM books WHERE isbn = '123456786'), 1),
                                                     ((SELECT id FROM books WHERE isbn = '123456786'), 2),
                                                     ((SELECT id FROM books WHERE isbn = '123456786'), 6);

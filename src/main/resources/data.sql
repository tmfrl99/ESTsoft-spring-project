INSERT INTO member(name) VALUES ('이름1');
INSERT INTO member(name) VALUES ('이름1');
INSERT INTO member(name) VALUES ('이름1');

INSERT INTO article (title, content, created_at, updated_at)
VALUES
('제목1', '내용1', NOW(), NOW())
, ('제목2', '내용2', NOW(), NOW())
, ('제목3', '내용3', NOW(), NOW());

INSERT INTO book (id, name, author) VALUES ('첫번째', 'JPA 프로그래밍', '김영한');
INSERT INTO book (id, name, author) VALUES ('두번째', '토비의 스프링', '토비');
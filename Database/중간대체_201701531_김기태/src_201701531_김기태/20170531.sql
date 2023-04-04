-- 계정만들기

DROP DATABASE IF EXISTS  id201701531;
DROP USER IF EXISTS  id201701531@localhost;
create user id201701531@localhost identified by 'pw201701531';
CREATE DATABASE mygames;
grant all privileges on mygames.* to id201701531@localhost with grant option;
commit;
use mygames;


show databases;
show tables;

-- 계정
CREATE TABLE accounts (
  id        VARCHAR(20) NOT NULL,
  pw     	VARCHAR(20) NOT NULL,
  username 	VARCHAR(20) NOT NULL,
  email     VARCHAR(50) NOT NULL,
  account_date Date NOT NULL,
  PRIMARY KEY(id)
);

-- 캐릭터
CREATE TABLE characters(
	id VARCHAR(20) NOT NULL,
    charactername VARCHAR(20) NOT NULL,
	hair integer NOT NULL,
    face integer NOT NULL,
    money decimal NOT NULL,
    borrow_money decimal NOT NULL,
    foreign key(id) REFERENCES accounts (id),
    PRIMARY KEY(charactername, id)
);

-- 아이템
CREATE TABLE items(
	item_num integer NOT NULL AUTO_INCREMENT,
    item_name varchar(50) NOT NULL,
    item_owner varchar(20) NOT NULL,
    item_original_price decimal NOT NULL,
    item_price decimal NOT NULL,
    item_reinforce integer NOT NULL,
    foreign key(item_owner) REFERENCES characters (charactername) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY(item_num)
);

drop table items;

Select * from accounts;
Select * from characters;
Select * from items;
SELECT count(*) as count from accounts WHERE id = "김기태";

UPDATE accounts SET username = "kkt" WHERE username="김기태";
-- 밑에 사용 불가
UPDATE accounts SET id = "kkt" WHERE username="kkt";
DELETE FROM accounts WHERE pw = "346";
DELETE FROM accounts WHERE pw = "1234" and id = '티리아';
ALTER TABLE characters ADD COLUMN money INTEGER NOT NULL;


--  accounts.id as '계정 ID', count(*) as '계정의 캐릭터 개수'
-- Select id , 0 as count from accounts;
select accounts.id, accounts.username, accounts.email, count(characters.id) as 'counts', accounts.account_date 
from accounts LEFT OUTER JOIN characters
ON characters.id = accounts.id
group by accounts.id;

INSERT INTO characters values("admin","ete","1","1");
Select * from characters where id = "admin";
drop table characters;
drop table items;

delete from characters WHERE charactername = '54';

Select money from characters where charactername = "티리아3";
Select * from items;
delete from items;
UPDATE characters SET money = 0, borrow_money = 0 WHERE charactername = "고양이";

SELECT * FROM items WHERE item_num = 2;

SELECT * FROM characters WHERE charactername LIKE "고양이";

SELECT * FROM items ORDER BY item_price DESC;

SELECT * FROM characters;
SELECT id FROM characters WHERE charactername = "고양이10";
SELECT id FROM accounts WHERE id = (SELECT id FROM characters WHERE charactername = "고양이10");

UPDATE characters SET charactername = "티리아2" WHERE charactername = "티리아1";
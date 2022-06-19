-- 普通表
CREATE TABLE `vote_record` (
	`id` INT (11) NOT NULL AUTO_INCREMENT,
	`user_id` VARCHAR (20) NOT NULL,
	`vote_id` INT (11) NOT NULL,
	`group_id` INT (11) NOT NULL,
	`create_time` datetime NOT NULL,
	PRIMARY KEY (`id`),
	KEY `index_user_id` (`user_id`) USING HASH
) ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8
-- 内存表
CREATE TABLE `vote_record_memory` (
	`id` INT (11) NOT NULL AUTO_INCREMENT,
	`user_id` VARCHAR (20) NOT NULL,
	`vote_id` INT (11) NOT NULL,
	`group_id` INT (11) NOT NULL,
	`create_time` datetime NOT NULL,
	PRIMARY KEY (`id`),
	KEY `index_id` (`user_id`) USING HASH
) ENGINE = MEMORY AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8
-- 创建函数
CREATE FUNCTION `rand_string`(n INT) RETURNS varchar(255) CHARSET latin1
BEGIN 
DECLARE chars_str varchar(100) DEFAULT 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'; 
DECLARE return_str varchar(255) DEFAULT '' ;
DECLARE i INT DEFAULT 0; 
WHILE i < n DO 
SET return_str = concat(return_str,substring(chars_str , FLOOR(1 + RAND()*62 ),1)); 
SET i = i +1; 
END WHILE; 
RETURN return_str; 
END
-- 调用
CALL add_vote_memory(1000000)
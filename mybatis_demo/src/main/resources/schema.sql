DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `follower`;

CREATE TABLE IF NOT EXISTS `user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(80) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`user_id`)
);

CREATE TABLE IF NOT EXISTS `follower` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `user` (`username`, `password`, `create_time`) values ('syh202', '123456', '2017-05-24 16:22:05');
INSERT INTO `follower` (name, user_id) VALUES ('test1', 1);
INSERT INTO `follower` (name, user_id) VALUES ('test2', 1);
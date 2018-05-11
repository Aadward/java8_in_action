DROP TABLE IF EXISTS `user`;

CREATE TABLE IF NOT EXISTS `user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(80) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`user_id`)
);

--INSERT INTO `user` (`username`, `password`, `create_time`) values ('syh202', '123456', '2017-05-24 16:22:05');
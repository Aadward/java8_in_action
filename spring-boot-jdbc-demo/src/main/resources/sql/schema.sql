DROP TABLE IF EXISTS `db_tags`;

CREATE TABLE IF NOT EXISTS `db_tags` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(100) NOT NULL,
  `versions` bigint(20) unsigned NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
);


DROP TABLE IF EXISTS `sys_account`;

CREATE TABLE `sys_account` (
      `id` bigint(20) NOT NULL,
      `account` varchar(255) DEFAULT NULL,
      `name` varchar(255) DEFAULT NULL,
      `password` varchar(255) DEFAULT NULL,
      `last_login_agent` varchar(255) DEFAULT NULL,
      `last_login_time` datetime DEFAULT NULL,
      `remember_me_token` char(32) DEFAULT NULL,
      `remember_me_expire` bigint(20) DEFAULT NULL,
      PRIMARY KEY (`id`)
);
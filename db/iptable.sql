/*
MySQL Data Transfer
Source Host: localhost
Source Database: project
Target Host: localhost
Target Database: project
Date: 2010/5/28 ¤U¤È 05:05:07
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for iptable
-- ----------------------------
CREATE TABLE `iptable` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ip` varchar(15) NOT NULL,
  `update` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------

/*
MySQL Data Transfer
Source Host: localhost
Source Database: project
Target Host: localhost
Target Database: project
Date: 2010/5/29 �W�� 01:25:12
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for blacklist
-- ----------------------------
CREATE TABLE `blacklist` (
  `no` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `id` varchar(10) NOT NULL,
  `path` text NOT NULL,
  `parameters` text NOT NULL,
  `type` varchar(20) NOT NULL,
  `status` int(2) NOT NULL,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------

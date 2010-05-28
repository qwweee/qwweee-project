/*
MySQL Data Transfer
Source Host: localhost
Source Database: 127.0.0.1
Target Host: localhost
Target Database: 127.0.0.1
Date: 2010/5/28 ¤U¤È 05:04:01
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for flow
-- ----------------------------
CREATE TABLE `flow` (
  `RouterIP` varchar(15) NOT NULL,
  `SysUptime` bigint(20) DEFAULT NULL,
  `Secs` bigint(20) DEFAULT NULL,
  `Nsecs` bigint(20) DEFAULT NULL,
  `Flow_Sequence` bigint(20) NOT NULL,
  `Engine_Type` int(11) NOT NULL,
  `Engine_ID` int(11) NOT NULL,
  `SrcAddr` varchar(15) NOT NULL,
  `DstAddr` varchar(15) NOT NULL,
  `NextHop` varchar(15) NOT NULL,
  `Input` int(11) NOT NULL,
  `Output` int(11) NOT NULL,
  `dPkts` bigint(20) DEFAULT NULL,
  `dOctets` bigint(20) DEFAULT NULL,
  `aFirst` bigint(20) DEFAULT NULL,
  `aLast` bigint(20) DEFAULT NULL,
  `SrcPort` int(11) NOT NULL,
  `DstPort` int(11) NOT NULL,
  `Tcp_Flags` int(11) NOT NULL,
  `Prot` int(11) NOT NULL,
  `TOS` int(11) NOT NULL,
  `Src_As` int(11) NOT NULL,
  `Dst_As` int(11) NOT NULL,
  `Src_Mask` int(11) NOT NULL,
  `Dst_Mask` int(11) NOT NULL,
  `Stamp` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------

/*
 Navicat Premium Dump SQL

 Source Server         : mysql80
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : bigevent

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 21/06/2025 17:59:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for game
-- ----------------------------
DROP TABLE IF EXISTS `game`;
CREATE TABLE `game`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `player_id` int NOT NULL,
  `player_cards` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `dealer_cards` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `player_score` int NULL DEFAULT NULL,
  `dealer_score` int NULL DEFAULT NULL,
  `game_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `bet_amount` int NULL DEFAULT NULL,
  `result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_player_id`(`player_id` ASC) USING BTREE,
  INDEX `idx_game_status`(`game_status` ASC) USING BTREE,
  CONSTRAINT `game_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of game
-- ----------------------------

-- ----------------------------
-- Table structure for player
-- ----------------------------
DROP TABLE IF EXISTS `player`;
CREATE TABLE `player`  (
  `player_id` int NOT NULL COMMENT '用户ID',
  `balance` int NOT NULL DEFAULT 0 COMMENT '当前余额',
  `initial_balance` int NOT NULL DEFAULT 0 COMMENT '初始余额',
  `consecutive_losses` int NOT NULL DEFAULT 0 COMMENT '连输次数',
  `consecutive_wins` int NOT NULL DEFAULT 0 COMMENT '连赢次数',
  `total_loss` int NOT NULL DEFAULT 0 COMMENT '总亏损',
  `win_number` int NOT NULL DEFAULT 0 COMMENT '总赢次',
  `all_number` int NOT NULL DEFAULT 0 COMMENT '总胜场',
  `winning` int NOT NULL DEFAULT 0 COMMENT '胜率',
  `win21` int NOT NULL DEFAULT 0 COMMENT '21点次数',
  `all_time` int NOT NULL DEFAULT 0 COMMENT '游戏总时长',
  `dan` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '新兵III' COMMENT '段位',
  `dan_number` int NOT NULL DEFAULT 0 COMMENT '段位积分',
  `dan_sort` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '99999+' COMMENT '段位世界排名',
  PRIMARY KEY (`player_id`) USING BTREE,
  INDEX `idx_dan`(`dan` ASC) USING BTREE,
  INDEX `idx_dan_sort`(`dan_sort` ASC) USING BTREE,
  CONSTRAINT `player_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '玩家信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of player
-- ----------------------------
INSERT INTO `player` VALUES (50219390, 1000, 1000, 0, 1, 0, 3, 16, 18, 2, 2114, '高手I', 953, '未上榜');
INSERT INTO `player` VALUES (58082269, 1000, 1000, 1, 0, 0, 4, 8, 50, 1, 10201, '新手III', 1352, '未上榜');
INSERT INTO `player` VALUES (74112975, 880, 880, 0, 1, 0, 6, 22, 27, 7, 3741, '中级I', 645, '未上榜');
INSERT INTO `player` VALUES (83978498, 1000, 1000, 0, 1, 0, 3, 5, 60, 4, 12030, '国际大师', 1652, '未上榜');
INSERT INTO `player` VALUES (95165606, 2000, 2000, 2, 0, 0, 3, 19, 15, 1, 5141, '新手I', 23, '未上榜');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `balance` int NULL DEFAULT 1000,
  `state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE,
  INDEX `idx_email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 650117122 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (50219390, '王恒123', 'e10adc3949ba59abbe56e057f20f883e', 'wh@qq.com', '12345678912', '/userpic', 2894, '离线', '2025-06-14 19:15:22', '2025-06-21 17:01:16');
INSERT INTO `user` VALUES (58082269, '刘世达', 'e10adc3949ba59abbe56e057f20f883e', 'lsd@qq.com', '13022003366', '/userpic', 2020, '离线', '2025-06-09 14:10:42', '2025-06-20 20:12:03');
INSERT INTO `user` VALUES (74112975, '菅月辉', 'e10adc3949ba59abbe56e057f20f883e', 'jyh@qq.com', '12311223344', '/userpic', 2785, '在线', '2025-06-09 14:10:52', '2025-06-21 17:49:07');
INSERT INTO `user` VALUES (83978498, '王荣泽', 'e10adc3949ba59abbe56e057f20f883e', 'wrz@qq.com', '12365422351', '/userpic', 1010, '离线', '2025-06-16 14:53:52', '2025-06-20 18:33:50');
INSERT INTO `user` VALUES (95165606, '宫新欣', 'e10adc3949ba59abbe56e057f20f883e', '', '', '/userpic', 1910, '离线', '2025-06-21 17:30:23', '2025-06-21 17:34:07');

SET FOREIGN_KEY_CHECKS = 1;

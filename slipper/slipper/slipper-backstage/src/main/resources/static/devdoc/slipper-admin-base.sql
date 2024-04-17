/*
 Navicat Premium Data Transfer

 Source Server         : Chen
 Source Server Type    : MySQL
 Source Server Version : 80024
 Source Host           : 139.196.182.46:3306
 Source Schema         : slipper-admin-base

 Target Server Type    : MySQL
 Target Server Version : 80024
 File Encoding         : 65001

 Date: 05/08/2022 11:28:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for administrator
-- ----------------------------
DROP TABLE IF EXISTS `administrator`;
CREATE TABLE `administrator`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密盐',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `sex` tinyint NULL DEFAULT 2 COMMENT '性别：0-女 1-男 2-未知',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
  `supervisor` tinyint NULL DEFAULT 0 COMMENT '是否是超管：0-否 1-是',
  `enterprise_id` bigint NULL DEFAULT NULL COMMENT '企业ID',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-否 1-是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of administrator
-- ----------------------------
INSERT INTO `administrator` VALUES (1, 'admin', 'a99fe8a04fbc0a5180f743494b5322c45b973cbc098670a273defc56010368c4', 'HqgiEUO3MDetycjNT5zG', '宸', 'http://oss.frame.gumingchen.icu/file/6d089c94-ac2e-4e24-b5c5-a9210ca9c0c9.png', '13777777777', '1240235512@qq.com', 1, 1, 1, 1, 1, '2022-06-27 15:35:44', 1, '2022-07-01 15:14:09', 0);

-- ----------------------------
-- Table structure for administrator__role
-- ----------------------------
DROP TABLE IF EXISTS `administrator__role`;
CREATE TABLE `administrator__role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `administrator_id` bigint NOT NULL COMMENT '管理员ID',
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员-角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of administrator__role
-- ----------------------------

-- ----------------------------
-- Table structure for captcha
-- ----------------------------
DROP TABLE IF EXISTS `captcha`;
CREATE TABLE `captcha`  (
  `uuid` varchar(72) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'UUID',
  `code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '验证码',
  `expired_at` datetime NOT NULL COMMENT '到期时间',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '验证码' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of captcha
-- ----------------------------

-- ----------------------------
-- Table structure for enterprise
-- ----------------------------
DROP TABLE IF EXISTS `enterprise`;
CREATE TABLE `enterprise`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `logo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'LOGO',
  `describe` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-否 1-是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '企业' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enterprise
-- ----------------------------
INSERT INTO `enterprise` VALUES (1, '总后台', 'http://oss.frame.gumingchen.icu/file/9c6ddfeb-b2e7-48eb-9b3f-d6bcc6437747.png', '总后台-管理所有企业', 1, 1, '2022-07-15 13:43:21', '2022-07-15 14:07:43', 1, 0);

-- ----------------------------
-- Table structure for enterprise__menu
-- ----------------------------
DROP TABLE IF EXISTS `enterprise__menu`;
CREATE TABLE `enterprise__menu`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name_cn` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '中文名称',
  `name_en` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '英文名称',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `sort` int NULL DEFAULT 1 COMMENT '排序',
  `menu_id` int NOT NULL COMMENT '菜单ID',
  `enterprise_id` bigint NULL DEFAULT NULL COMMENT '企业ID',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-否 1-是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 324 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '企业菜单权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enterprise__menu
-- ----------------------------
INSERT INTO `enterprise__menu` VALUES (1, NULL, NULL, NULL, 1, 1, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (2, NULL, NULL, NULL, 1, 2, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (3, NULL, NULL, NULL, 1, 3, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (4, NULL, NULL, NULL, 1, 4, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (5, NULL, NULL, NULL, 1, 5, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (6, NULL, NULL, NULL, 1, 6, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (7, NULL, NULL, NULL, 1, 7, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (8, NULL, NULL, NULL, 1, 8, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (9, NULL, NULL, NULL, 1, 9, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (10, NULL, NULL, NULL, 1, 10, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (11, NULL, NULL, NULL, 1, 11, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (12, NULL, NULL, NULL, 1, 12, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (13, NULL, NULL, NULL, 1, 13, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (14, NULL, NULL, NULL, 1, 14, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (15, NULL, NULL, NULL, 1, 15, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (16, NULL, NULL, NULL, 1, 16, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (17, NULL, NULL, NULL, 1, 17, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (18, NULL, NULL, NULL, 1, 18, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (19, NULL, NULL, NULL, 1, 19, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (20, NULL, NULL, NULL, 1, 20, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (21, NULL, NULL, NULL, 1, 21, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (22, NULL, NULL, NULL, 1, 22, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (23, NULL, NULL, NULL, 1, 23, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (24, NULL, NULL, NULL, 1, 24, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (25, NULL, NULL, NULL, 1, 25, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (26, NULL, NULL, NULL, 1, 26, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (27, NULL, NULL, NULL, 1, 27, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (28, NULL, NULL, NULL, 1, 28, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (29, NULL, NULL, NULL, 1, 29, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (30, NULL, NULL, NULL, 1, 30, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (31, NULL, NULL, NULL, 1, 31, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (32, NULL, NULL, NULL, 1, 32, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (33, NULL, NULL, NULL, 1, 33, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (34, NULL, NULL, NULL, 1, 34, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (35, NULL, NULL, NULL, 1, 35, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (36, NULL, NULL, NULL, 1, 36, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (37, NULL, NULL, NULL, 1, 37, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (38, NULL, NULL, NULL, 1, 38, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (39, NULL, NULL, NULL, 1, 39, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (40, NULL, NULL, NULL, 1, 40, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (41, NULL, NULL, NULL, 1, 41, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (42, NULL, NULL, NULL, 1, 42, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (43, NULL, NULL, NULL, 1, 43, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (44, NULL, NULL, NULL, 1, 44, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (45, NULL, NULL, NULL, 1, 45, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (46, NULL, NULL, NULL, 1, 46, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (47, NULL, NULL, NULL, 1, 47, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (48, NULL, NULL, NULL, 1, 48, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (49, NULL, NULL, NULL, 1, 49, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (50, NULL, NULL, NULL, 1, 50, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (51, NULL, NULL, NULL, 1, 51, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (52, NULL, NULL, NULL, 1, 52, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (53, NULL, NULL, NULL, 1, 53, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (54, NULL, NULL, NULL, 1, 54, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (55, NULL, NULL, NULL, 1, 55, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (56, NULL, NULL, NULL, 1, 56, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (57, NULL, NULL, NULL, 1, 57, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (58, NULL, NULL, NULL, 1, 58, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (59, NULL, NULL, NULL, 1, 59, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);
INSERT INTO `enterprise__menu` VALUES (60, NULL, NULL, NULL, 1, 60, 1, 1, '2022-08-04 16:35:28', NULL, NULL, 0);

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `original` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原始名称',
  `actual` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实际名称',
  `extension` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展名称',
  `size` bigint NULL DEFAULT NULL COMMENT '大小',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物理路径',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '虚拟路径',
  `prefix` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前缀',
  `type` tinyint NULL DEFAULT NULL COMMENT '存储类型',
  `creator` bigint NULL DEFAULT NULL COMMENT '上传用户ID',
  `created_at` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '上传时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-否 1-是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file
-- ----------------------------

-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录帐号',
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '登录信息',
  `request_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器',
  `operating_system` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户代理',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-否 1-是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3538 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '登录日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of login_log
-- ----------------------------

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name_cn` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '中文名称',
  `name_en` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '英文名称',
  `path` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由 path 属性值 若为空则按照url路径处理',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由 name 属性值 若为空则按照url路径处理',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单路由 或 URL',
  `permission` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` tinyint NULL DEFAULT 0 COMMENT '类型：0-目录 1-菜单 2-按钮 3-iframe 4-外链',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `show` tinyint NULL DEFAULT 1 COMMENT '是否在菜单显示：0-否 1-是',
  `tab` tinyint NULL DEFAULT 0 COMMENT '是否显示在tab页签：0-否 1-是',
  `multiple` tinyint NULL DEFAULT 0 COMMENT '是否支持tab页签多开：0-否 1-是',
  `keepalive` tinyint NULL DEFAULT 0 COMMENT '是否支持缓存：0-否 1-是',
  `sort` int NULL DEFAULT 1 COMMENT '排序',
  `parent_id` bigint NOT NULL COMMENT '父ID',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-否 1-是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 165 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '首页', 'Home', '/home', 'home', 'home/index', NULL, 1, 'home', 1, 1, 0, 0, 1, 0, 1, '2022-04-22 16:35:20', 1, '2022-07-26 17:13:37', 0);
INSERT INTO `menu` VALUES (2, '查看', 'View', NULL, NULL, NULL, 'home:visits', 2, NULL, 1, 0, 0, 0, 1, 1, 1, '2022-06-28 14:54:50', NULL, NULL, 0);
INSERT INTO `menu` VALUES (3, '系统管理', 'System', NULL, NULL, NULL, NULL, 0, 'system', 1, 1, 0, 0, 4, 0, 1, '2022-04-22 16:39:18', 1, '2022-07-26 17:13:37', 0);
INSERT INTO `menu` VALUES (4, '角色', 'Role', '/role', 'role', 'system/role/index', 'role:page', 1, 'role', 1, 1, 0, 0, 2, 3, 1, '2022-04-22 16:41:26', 1, '2022-05-20 11:08:59', 0);
INSERT INTO `menu` VALUES (5, '查看', 'View', NULL, NULL, NULL, 'role:info', 2, NULL, 1, 0, 0, 0, 1, 4, 1, '2022-04-22 16:45:23', NULL, NULL, 0);
INSERT INTO `menu` VALUES (6, '新增', 'Add', NULL, NULL, NULL, 'role:create;enterpriseMenu:select', 2, NULL, 1, 0, 0, 0, 2, 4, 1, '2022-04-22 16:46:39', NULL, NULL, 0);
INSERT INTO `menu` VALUES (7, '编辑', 'Edit', NULL, NULL, NULL, 'role:info;role:update;role:info;enterpriseMenu:select', 2, NULL, 1, 0, 0, 0, 3, 4, 1, '2022-04-22 16:46:39', NULL, NULL, 0);
INSERT INTO `menu` VALUES (8, '删除', 'Delete', NULL, NULL, NULL, 'role:delete', 2, NULL, 1, 0, 0, 0, 4, 4, 1, '2022-04-22 16:46:39', NULL, NULL, 0);
INSERT INTO `menu` VALUES (9, '设置是否显示', 'Set Display', NULL, NULL, NULL, 'role:show', 2, NULL, 1, 0, 0, 0, 5, 4, 1, '2022-05-10 20:06:38', NULL, NULL, 0);
INSERT INTO `menu` VALUES (10, '管理员', 'Administrator', '/administrator', 'administrator', 'system/administrator/index', 'administrator:page', 1, 'admin', 1, 1, 0, 0, 3, 3, 1, '2022-04-22 16:41:26', 1, '2022-05-20 11:08:59', 0);
INSERT INTO `menu` VALUES (11, '查看', 'View', NULL, NULL, NULL, 'administrator:info', 2, NULL, 1, 0, 0, 0, 1, 10, 1, '2022-04-22 16:45:23', NULL, NULL, 0);
INSERT INTO `menu` VALUES (12, '新增', 'Add', NULL, NULL, NULL, 'administrator:create;role:select', 2, NULL, 1, 0, 0, 0, 2, 10, 1, '2022-04-22 16:46:39', NULL, NULL, 0);
INSERT INTO `menu` VALUES (13, '编辑', 'Edit', NULL, NULL, NULL, 'administrator:info;administrator:update;administrator:info;role:select', 2, NULL, 1, 0, 0, 0, 3, 10, 1, '2022-04-22 16:46:39', NULL, NULL, 0);
INSERT INTO `menu` VALUES (14, '删除', 'Delete', NULL, NULL, NULL, 'administrator:delete', 2, NULL, 1, 0, 0, 0, 4, 10, 1, '2022-04-22 16:46:39', NULL, NULL, 0);
INSERT INTO `menu` VALUES (15, '设置是否禁用', 'Set Disable', NULL, NULL, NULL, 'administrator:status', 2, NULL, 1, 0, 0, 0, 5, 10, 1, '2022-05-10 20:06:38', NULL, NULL, 0);
INSERT INTO `menu` VALUES (16, '菜单管理', 'Menu', '/menu', 'menu', 'system/menu/index', 'enterpriseMenu:list', 1, 'menu', 1, 1, 0, 0, 4, 3, 1, '2022-04-22 16:41:26', 1, '2022-05-20 11:08:59', 0);
INSERT INTO `menu` VALUES (17, '查看', 'View', NULL, NULL, NULL, 'enterpriseMenu:list', 2, NULL, 1, 0, 0, 0, 1, 16, 1, '2022-04-22 16:45:23', NULL, NULL, 0);
INSERT INTO `menu` VALUES (18, '编辑', 'Edit', NULL, NULL, NULL, 'enterpriseMenu:update', 2, '', 1, 0, 0, 0, 2, 16, 1, '2022-04-22 16:46:39', 1, '2022-06-16 17:32:23', 0);
INSERT INTO `menu` VALUES (19, '个人中心', 'Personal Center', '/personal', 'personal', 'personal/index', 'self:loginLog:page;self:operationLog:page', 1, 'admin', 0, 1, 0, 0, 9, 0, 1, '2022-04-22 16:41:26', 1, '2022-07-26 17:13:37', 0);
INSERT INTO `menu` VALUES (20, '查看', 'View', NULL, NULL, NULL, 'self:loginLog:page;self:operationLog:page', 2, NULL, 0, 0, 0, 0, 1, 19, 1, '2022-06-28 15:28:08', NULL, NULL, 0);
INSERT INTO `menu` VALUES (21, '修改信息', 'Edit Information', NULL, NULL, NULL, 'administrator:basic', 2, '', 0, 0, 0, 0, 2, 19, 1, '2022-06-28 15:28:28', 1, '2022-06-28 15:30:04', 0);
INSERT INTO `menu` VALUES (22, '修改密码', 'Edit Password', NULL, NULL, NULL, 'administrator:password', 2, '', 0, 0, 0, 0, 3, 19, 1, '2022-06-28 15:28:46', 1, '2022-06-28 15:30:10', 0);
INSERT INTO `menu` VALUES (23, '日志管理', 'Log', NULL, NULL, NULL, NULL, 0, 'log', 1, 0, 0, 0, 5, 0, 1, '2022-05-18 15:24:34', 1, '2022-07-26 17:13:37', 0);
INSERT INTO `menu` VALUES (24, '操作日志', 'Operation log', '/log/operation', 'log-operation', 'log/operation/index', 'operationLog:page', 1, 'operation-log', 1, 1, 0, 0, 2, 23, 1, '2022-05-20 13:42:55', 1, '2022-06-17 16:01:40', 0);
INSERT INTO `menu` VALUES (25, '查看', 'View', NULL, NULL, NULL, 'operationLog:page', 2, NULL, 1, 0, 0, 0, 1, 24, 1, '2022-06-27 17:21:34', 1, '2022-06-27 17:21:35', 0);
INSERT INTO `menu` VALUES (26, '删除', 'Delete', NULL, NULL, NULL, 'operationLog:delete', 2, NULL, 1, 0, 0, 0, 2, 24, 1, '2022-04-22 16:46:39', 1, '2022-06-27 17:21:35', 0);
INSERT INTO `menu` VALUES (27, '登录日志', 'Login log', '/log/login', 'log-login', 'log/login/index', 'loginLog:page', 1, 'login-log', 1, 1, 0, 0, 1, 23, 1, '2022-05-20 13:42:55', 1, '2022-06-17 16:01:32', 0);
INSERT INTO `menu` VALUES (28, '查看', 'View', NULL, NULL, NULL, 'loginLog:page', 2, NULL, 1, 0, 0, 0, 1, 27, 1, '2022-06-27 17:21:17', 1, '2022-06-27 17:21:19', 0);
INSERT INTO `menu` VALUES (29, '删除', 'Delete', NULL, NULL, NULL, 'loginLog:delete', 2, NULL, 1, 0, 0, 0, 2, 27, 1, '2022-04-22 16:46:39', 1, '2022-06-27 17:21:19', 0);
INSERT INTO `menu` VALUES (30, '企业管理', 'Enterprise', NULL, NULL, NULL, NULL, 0, 'enterprise', 1, 0, 0, 0, 3, 0, 1, '2022-05-16 16:39:13', 1, '2022-07-26 17:13:37', 0);
INSERT INTO `menu` VALUES (31, '企业列表', 'Enterprise List', '/enterprise/list', 'enterprise-list', 'enterprise/list/index', 'enterprise:page', 1, 'list', 1, 1, 0, 0, 1, 30, 1, '2022-05-16 16:49:46', NULL, NULL, 0);
INSERT INTO `menu` VALUES (32, '查看', 'View', NULL, NULL, NULL, 'enterprise:info', 2, NULL, 1, 0, 0, 0, 1, 31, 1, '2022-05-17 09:37:34', NULL, NULL, 0);
INSERT INTO `menu` VALUES (33, '新增', 'Add', NULL, NULL, NULL, 'enterprise:create', 2, NULL, 1, 0, 0, 0, 2, 31, 1, '2022-05-17 09:38:21', NULL, NULL, 0);
INSERT INTO `menu` VALUES (34, '编辑', 'Edit', NULL, NULL, NULL, 'enterprise:info;enterprise:update', 2, NULL, 1, 0, 0, 0, 3, 31, 1, '2022-04-22 16:46:39', NULL, NULL, 0);
INSERT INTO `menu` VALUES (35, '删除', 'Delete', NULL, NULL, NULL, 'enterprise:delete', 2, NULL, 1, 0, 0, 0, 4, 31, 1, '2022-04-22 16:46:39', NULL, NULL, 0);
INSERT INTO `menu` VALUES (36, '设置是否禁用', 'Set Disable', NULL, NULL, NULL, 'enterprise:status', 2, NULL, 1, 0, 0, 0, 5, 31, 1, '2022-05-17 11:06:14', NULL, NULL, 0);
INSERT INTO `menu` VALUES (37, '企业角色', 'Enterprise Role', '/enterprise/role', 'enterprise-role', 'enterprise/role/index', 'global:enterprise:page;global:role:page', 1, 'role', 1, 1, 0, 0, 2, 30, 1, '2022-05-17 13:46:25', NULL, NULL, 0);
INSERT INTO `menu` VALUES (38, '查看', 'View', NULL, NULL, NULL, 'global:role:info', 2, NULL, 1, 0, 0, 0, 1, 37, 1, '2022-04-22 16:45:23', NULL, NULL, 0);
INSERT INTO `menu` VALUES (39, '新增', 'Add', NULL, NULL, NULL, 'global:role:create;global:enterpriseMenu:select', 2, NULL, 1, 0, 0, 0, 2, 37, 1, '2022-04-22 16:46:39', NULL, NULL, 0);
INSERT INTO `menu` VALUES (40, '编辑', 'Edit', NULL, NULL, NULL, 'global:role:update;global:role:info;global:enterpriseMenu:select', 2, NULL, 1, 0, 0, 0, 3, 37, 1, '2022-04-22 16:46:39', NULL, NULL, 0);
INSERT INTO `menu` VALUES (41, '删除', 'Delete', NULL, NULL, NULL, 'global:role:delete', 2, NULL, 1, 0, 0, 0, 4, 37, 1, '2022-04-22 16:46:39', NULL, NULL, 0);
INSERT INTO `menu` VALUES (42, '设置是否显示', 'Set Display', NULL, NULL, NULL, 'global:role:show', 2, NULL, 1, 0, 0, 0, 5, 37, 1, '2022-05-10 20:06:38', NULL, NULL, 0);
INSERT INTO `menu` VALUES (43, '企业管理员', 'Enterprise Administrator', '/enterprise/administrator', 'enterprise-administrator', 'enterprise/administrator/index', 'global:enterprise:page;global:administrator:page', 1, 'admin', 1, 1, 0, 0, 3, 30, 1, '2022-05-17 13:46:25', NULL, NULL, 0);
INSERT INTO `menu` VALUES (44, '查看', 'View', NULL, NULL, NULL, 'global:administrator:info', 2, NULL, 1, 0, 0, 0, 1, 43, 1, '2022-04-22 16:45:23', NULL, NULL, 0);
INSERT INTO `menu` VALUES (45, '新增', 'Add', NULL, NULL, NULL, 'global:administrator:create;global:role:select', 2, NULL, 1, 0, 0, 0, 2, 43, 1, '2022-04-22 16:46:39', NULL, NULL, 0);
INSERT INTO `menu` VALUES (46, '编辑', 'Edit', NULL, NULL, NULL, 'global:administrator:info;global:administrator:update;global:role:select', 2, NULL, 1, 0, 0, 0, 3, 43, 1, '2022-04-22 16:46:39', NULL, NULL, 0);
INSERT INTO `menu` VALUES (47, '删除', 'Delete', NULL, NULL, NULL, 'global:administrator:delete', 2, NULL, 1, 0, 0, 0, 4, 43, 1, '2022-04-22 16:46:39', NULL, NULL, 0);
INSERT INTO `menu` VALUES (48, '设置是否禁用', 'Set Disable', NULL, NULL, NULL, 'global:administrator:status', 2, NULL, 1, 0, 0, 0, 5, 43, 1, '2022-05-10 20:06:38', NULL, NULL, 0);
INSERT INTO `menu` VALUES (49, '企业菜单', 'Enterprise Menu', '/enterprise/menu', 'enterprise-menu', 'enterprise/menu/index', 'global:enterprise:page;global:enterpriseMenu:list', 1, 'menu', 1, 1, 0, 0, 4, 30, 1, '2022-05-17 13:46:25', 1, '2022-05-21 15:08:08', 0);
INSERT INTO `menu` VALUES (50, '查看', 'View', NULL, NULL, NULL, 'global:administrator:info', 2, NULL, 1, 0, 0, 0, 1, 49, 1, '2022-04-22 16:45:23', NULL, NULL, 0);
INSERT INTO `menu` VALUES (51, '编辑', 'Edit', NULL, NULL, NULL, 'global:enterpriseMenu:update', 2, NULL, 1, 0, 0, 0, 3, 49, 1, '2022-04-22 16:46:39', NULL, NULL, 0);
INSERT INTO `menu` VALUES (52, '删除', 'Delete', NULL, NULL, NULL, 'global:enterpriseMenu:delete', 2, NULL, 1, 0, 0, 0, 4, 49, 1, '2022-04-22 16:46:39', NULL, NULL, 0);
INSERT INTO `menu` VALUES (53, '修改权限菜单', 'Modify permission menu', NULL, NULL, NULL, 'global:enterpriseMenu:ids;menu:select;global:enterpriseMenu:modify', 2, NULL, 1, 0, 0, 0, 2, 49, 1, '2022-04-22 16:46:39', NULL, NULL, 0);
INSERT INTO `menu` VALUES (54, '开发配置', 'Development', NULL, NULL, NULL, NULL, 0, 'development', 1, 0, 0, 0, 8, 0, 1, '2022-05-18 15:24:34', 1, '2022-07-26 17:13:37', 0);
INSERT INTO `menu` VALUES (55, '菜单权限', 'Menu', '/develop/menu', 'develop-menu', 'develop/menu/index', 'menu:select', 1, 'menu', 1, 1, 0, 0, 1, 54, 1, '2022-05-18 15:27:20', 1, '2022-06-27 14:03:59', 0);
INSERT INTO `menu` VALUES (56, '查看', 'View', NULL, NULL, NULL, 'menu:info', 2, NULL, 1, 0, 0, 0, 1, 55, 1, '2022-04-22 16:45:23', 1, '2022-05-20 11:08:50', 0);
INSERT INTO `menu` VALUES (57, '新增', 'Add', NULL, NULL, NULL, 'menu:create', 2, NULL, 1, 0, 0, 0, 2, 55, 1, '2022-04-22 16:46:39', 1, '2022-05-20 11:08:50', 0);
INSERT INTO `menu` VALUES (58, '编辑', 'Edit', NULL, NULL, NULL, 'menu:update', 2, NULL, 1, 0, 0, 0, 3, 55, 1, '2022-04-22 16:46:39', 1, '2022-05-20 11:08:50', 0);
INSERT INTO `menu` VALUES (59, '删除', 'Delete', NULL, NULL, NULL, 'menu:delete', 2, NULL, 1, 0, 0, 0, 4, 55, 1, '2022-04-22 16:46:39', 1, '2022-05-20 11:08:50', 0);
INSERT INTO `menu` VALUES (60, '拖拽排序', 'Drag sort', '', '', '', 'menu:drag', 2, '', 1, 0, 0, 0, 5, 55, 1, '2022-05-20 10:23:35', 1, '2022-05-20 11:08:50', 0);

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求URL',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求方式',
  `request_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `response_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '响应参数',
  `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器',
  `operating_system` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户代理',
  `times` bigint NOT NULL COMMENT '执行时长',
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '完整方法名',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-否 1-是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1158 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `show` tinyint NULL DEFAULT 1 COMMENT '是否显示：0-否 1-是 \r\n用于不给用户操作的角色',
  `enterprise_id` bigint NULL DEFAULT NULL COMMENT '企业ID',
  `creator` bigint NULL DEFAULT NULL COMMENT '创建者',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint NULL DEFAULT NULL COMMENT '更新者',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-否 1-是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------

-- ----------------------------
-- Table structure for role__menu
-- ----------------------------
DROP TABLE IF EXISTS `role__menu`;
CREATE TABLE `role__menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `enterprise_menu_id` bigint NOT NULL COMMENT '企业-菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 469 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色-菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role__menu
-- ----------------------------

-- ----------------------------
-- Table structure for token
-- ----------------------------
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token`  (
  `administrator_id` bigint NOT NULL COMMENT '用户ID',
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '凭证',
  `expired_at` datetime NULL DEFAULT NULL COMMENT '到期时间',
  `created_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`administrator_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户凭证' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of token
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

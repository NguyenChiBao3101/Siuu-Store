-- MySQL dump 10.13  Distrib 9.2.0, for macos15.2 (arm64)
--
-- Host: 127.0.0.1    Database: siu
-- ------------------------------------------------------
-- Server version	9.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `siu`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `siu` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `siu`;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `is_verified` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKq0uja26qgu1atulenwup9rxyr` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES ('0d312ab2-b27d-4cfb-948a-9c2b6179c986','2024-11-03 22:48:10.450927','customer@gmail.com','$2a$10$d.z5EeIpLYIgh87FUoO0benuaE0hV32uyVoTUX6.nugrh2PWx7PXC','2024-11-03 22:48:10.450927',NULL,NULL),('49aff372-d06a-4d52-8d23-0de0098576e1','2024-11-03 22:48:10.336727','admin@gmail.com','$2a$10$SEm.bojGgo0KI8BhiIIv8Oo9Sz2Djc9LK48k8HWlSIDWuWKh6hRN6','2024-11-03 22:48:10.336727',NULL,NULL),('7ee987e5-f0a8-4138-b3ec-3b8358a22f16','2025-07-06 13:56:40.666016','baonguyen310115@gmail.com','','2025-07-06 13:56:40.666016',_binary '',_binary '');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_role`
--

DROP TABLE IF EXISTS `account_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_role` (
  `account_id` varchar(255) NOT NULL,
  `role_id` varchar(255) NOT NULL,
  PRIMARY KEY (`account_id`,`role_id`),
  KEY `FKrs2s3m3039h0xt8d5yhwbuyam` (`role_id`),
  CONSTRAINT `FK1f8y4iy71kb1arff79s71j0dh` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FKrs2s3m3039h0xt8d5yhwbuyam` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_role`
--

LOCK TABLES `account_role` WRITE;
/*!40000 ALTER TABLE `account_role` DISABLE KEYS */;
INSERT INTO `account_role` VALUES ('0d312ab2-b27d-4cfb-948a-9c2b6179c986','9b2b0a9c-8d6a-4ae8-80d1-d5438c13b65f'),('7ee987e5-f0a8-4138-b3ec-3b8358a22f16','9b2b0a9c-8d6a-4ae8-80d1-d5438c13b65f'),('49aff372-d06a-4d52-8d23-0de0098576e1','cd021927-7ebd-4dd1-a62f-b80319012c75');
/*!40000 ALTER TABLE `account_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `brand`
--

DROP TABLE IF EXISTS `brand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brand` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brand`
--

LOCK TABLES `brand` WRITE;
/*!40000 ALTER TABLE `brand` DISABLE KEYS */;
INSERT INTO `brand` VALUES ('041563f3-3fcb-4f70-9950-87990a12e0ed','2024-11-02 11:30:00.000000','adidas','2024-11-02 11:30:00.000000'),('1223f4e0-1280-4e07-96a5-ed359924b1be','2024-11-03 15:45:00.000000','puma','2024-11-03 15:45:00.000000'),('282fa0ce-e825-4943-bee4-02edfc57b587','2024-11-01 10:00:00.000000','nike','2024-11-01 10:00:00.000000');
/*!40000 ALTER TABLE `brand` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `id_account` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK7ci11mfeso55kc1d8ou1qhc6k` (`id_account`),
  CONSTRAINT `FKldb5k6mk67oudki8jgt7ri03h` FOREIGN KEY (`id_account`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES ('1aa6c444-817c-49a9-a5c0-489fee698912','2025-07-06 13:56:40.673925','2025-07-06 13:56:40.673925','7ee987e5-f0a8-4138-b3ec-3b8358a22f16'),('429b3123-3ad0-4dba-91fc-f720c907723c','2025-07-05 18:16:41.820616','2025-07-05 18:16:41.820616','0d312ab2-b27d-4cfb-948a-9c2b6179c986'),('668fc25d-0355-4148-9000-9d5553175b57','2025-07-05 18:16:41.824396','2025-07-05 18:16:41.824396','49aff372-d06a-4d52-8d23-0de0098576e1');
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_detail`
--

DROP TABLE IF EXISTS `cart_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_detail` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `quantity` int NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `id_cart` varchar(255) DEFAULT NULL,
  `id_product_variant` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh5pbblis7iw2kog1cponwqb9c` (`id_cart`),
  KEY `FKi332rf9lmjw85ylheu6dtlu6e` (`id_product_variant`),
  CONSTRAINT `FKh5pbblis7iw2kog1cponwqb9c` FOREIGN KEY (`id_cart`) REFERENCES `cart` (`id`),
  CONSTRAINT `FKi332rf9lmjw85ylheu6dtlu6e` FOREIGN KEY (`id_product_variant`) REFERENCES `product_variant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_detail`
--

LOCK TABLES `cart_detail` WRITE;
/*!40000 ALTER TABLE `cart_detail` DISABLE KEYS */;
INSERT INTO `cart_detail` VALUES ('df22cf87-cfe9-4858-ab82-fdc120ef4d8b','2025-08-07 11:46:22.372165',1,'2025-08-07 11:46:37.346815','429b3123-3ad0-4dba-91fc-f720c907723c','6261154c-5894-4e1c-9890-45100441c5b2'),('df640581-9231-4585-bbaa-10250bffd26a','2025-07-06 12:00:13.649010',1,'2025-07-06 12:00:26.302157','429b3123-3ad0-4dba-91fc-f720c907723c','f21395d3-0bb0-4b77-b796-71c091e3de73');
/*!40000 ALTER TABLE `cart_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `status` bit(1) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES ('8f72a7cb-236d-496e-8108-c0e90a8e558c','2024-11-03 22:49:14.313543','sneaker',_binary '','2024-11-08 22:35:40.302113'),('eae52c01-6239-498a-8e7e-1aa35836890e','2024-11-03 22:50:06.993145','giày đá bóng',_binary '\0','2025-09-24 16:42:26.144343');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `id` varchar(255) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `rate` int NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `account_id` varchar(255) NOT NULL,
  `order_detail_id` varchar(255) NOT NULL,
  `product_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKm630rkb43niptk4s8dway4qf` (`order_detail_id`),
  KEY `FKljoe4l0npa8nv5q24e4c1n1ik` (`account_id`),
  KEY `FKlsfunb44jdljfmbx4un8s4waa` (`product_id`),
  CONSTRAINT `FK88hp1ug23wm12cr9kb5ufffkf` FOREIGN KEY (`order_detail_id`) REFERENCES `order_detail` (`id`),
  CONSTRAINT `FKljoe4l0npa8nv5q24e4c1n1ik` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FKlsfunb44jdljfmbx4un8s4waa` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_image`
--

DROP TABLE IF EXISTS `feedback_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback_image` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `image_url` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `feedback_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5irkynd9hah7v7rdbrljsn099` (`feedback_id`),
  CONSTRAINT `FK5irkynd9hah7v7rdbrljsn099` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_image`
--

LOCK TABLES `feedback_image` WRITE;
/*!40000 ALTER TABLE `feedback_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `feedback_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `id` varchar(255) NOT NULL,
  `canceled_date` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `discount` int NOT NULL DEFAULT '0',
  `payment_date` datetime(6) DEFAULT NULL,
  `payment_expiration_date` datetime(6) DEFAULT NULL,
  `payment_method` enum('COD','VN_PAY') NOT NULL,
  `payment_status` enum('EXPIRED','FAILED','PAID','PENDING','UNPAID') NOT NULL,
  `payment_url` varchar(1000) DEFAULT NULL,
  `receive_date` datetime(6) DEFAULT NULL,
  `shipping_address` varchar(255) NOT NULL,
  `shipping_email` varchar(255) NOT NULL,
  `shipping_fee` int NOT NULL DEFAULT '0',
  `shipping_name` varchar(255) NOT NULL,
  `shipping_note` varchar(255) DEFAULT NULL,
  `shipping_phone` varchar(255) NOT NULL,
  `status` enum('CANCELLED','COMPLETED','CONFIRMED','PENDING','SHIPPING') NOT NULL,
  `total_price` int NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `vnpay_transaction_no` varchar(255) DEFAULT NULL,
  `customer_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKeiwkpprrj6uhm4w51ocackrmv` (`customer_id`),
  CONSTRAINT `FKeiwkpprrj6uhm4w51ocackrmv` FOREIGN KEY (`customer_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES ('DH785244868',NULL,'2025-07-06 14:00:44.861412',0,'2025-07-06 14:02:39.273454',NULL,'COD','PAID',NULL,NULL,'446/69/75 tổ 25 Khu phố 5A, Phường Long Bình, Thành phố Biên Hòa, Đồng Nai','baonguyen310115@gmail.com',0,'Nguyễn Chí Bảo',NULL,'0367422628','COMPLETED',1200000,'2025-07-06 14:02:39.277396',NULL,'7ee987e5-f0a8-4138-b3ec-3b8358a22f16');
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_detail`
--

DROP TABLE IF EXISTS `order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_detail` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `price` int NOT NULL,
  `quantity` int NOT NULL,
  `total_price` int NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `id_order` varchar(255) DEFAULT NULL,
  `id_product_variant` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKi12qhnofyq280bsfs26t17c36` (`id_order`),
  KEY `FK9gfhnax0akc11yt9t2homt54n` (`id_product_variant`),
  CONSTRAINT `FK9gfhnax0akc11yt9t2homt54n` FOREIGN KEY (`id_product_variant`) REFERENCES `product_variant` (`id`),
  CONSTRAINT `FKi12qhnofyq280bsfs26t17c36` FOREIGN KEY (`id_order`) REFERENCES `order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_detail`
--

LOCK TABLES `order_detail` WRITE;
/*!40000 ALTER TABLE `order_detail` DISABLE KEYS */;
INSERT INTO `order_detail` VALUES ('7bcc0a7b-5288-4307-ac9d-b3c010b251b8','2025-07-06 14:00:44.894346',1200000,1,1200000,'2025-07-06 14:00:44.894346','DH785244868','95605f5f-b2b7-4a3c-a28f-21ee2535c4dd');
/*!40000 ALTER TABLE `order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_history`
--

DROP TABLE IF EXISTS `order_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_history` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `status` enum('CANCELLED','COMPLETED','CONFIRMED','PENDING','SHIPPING') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `id_employee` varchar(255) DEFAULT NULL,
  `id_order` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo82tahf5rts521m5svcv16qqr` (`id_employee`),
  KEY `FKmppn32yom9yjqv7hx1awfp7d6` (`id_order`),
  CONSTRAINT `FKmppn32yom9yjqv7hx1awfp7d6` FOREIGN KEY (`id_order`) REFERENCES `order` (`id`),
  CONSTRAINT `FKo82tahf5rts521m5svcv16qqr` FOREIGN KEY (`id_employee`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_history`
--

LOCK TABLES `order_history` WRITE;
/*!40000 ALTER TABLE `order_history` DISABLE KEYS */;
INSERT INTO `order_history` VALUES ('77a84e11-1868-4f7d-9542-781f59a7440e','2025-07-06 14:02:18.382519','CONFIRMED','2025-07-06 14:02:18.382519','49aff372-d06a-4d52-8d23-0de0098576e1','DH785244868'),('dd67c6f1-37c6-4146-8207-b88c64d1b556','2025-07-06 14:02:39.276172','COMPLETED','2025-07-06 14:02:39.276172','49aff372-d06a-4d52-8d23-0de0098576e1','DH785244868'),('f0e49d20-1a30-416f-ab37-943b69cfdc3e','2025-07-06 14:02:25.593414','SHIPPING','2025-07-06 14:02:25.593414','49aff372-d06a-4d52-8d23-0de0098576e1','DH785244868');
/*!40000 ALTER TABLE `order_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` text NOT NULL,
  `name` varchar(255) NOT NULL,
  `slug` varchar(255) NOT NULL,
  `status` enum('ACTIVE','INACTIVE','IN_STOCK','OUT_OF_STOCK') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `brand_id` varchar(255) NOT NULL,
  `category_id` varchar(255) NOT NULL,
  `price` int NOT NULL,
  `rate` int DEFAULT '0',
  `rated_total` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FKs6cydsualtsrprvlf2bb3lcam` (`brand_id`),
  KEY `FK1mtsbur82frn64de7balymq9s` (`category_id`),
  CONSTRAINT `FK1mtsbur82frn64de7balymq9s` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `FKs6cydsualtsrprvlf2bb3lcam` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES ('78fdaddf-d4b1-4bcc-870c-eb9fbb058833','2024-11-08 22:02:34.885948','Khoác lên mình chiếc áo da cao cấp, mềm mịn tone màu trắng bao phủ toàn bộ đôi giày. Tuy sở hữu phối màu đơn giản, nhưng chúng chắc chắn vô cùng “đa di năng” có thể giúp chủ sở hữu mix cùng bất cứ set đồ nào. Đặc biệt không thể thiếu đi công nghệ Air tiên tiến có cấu trúc túi khí bên trong, nhằm giảm thiểu chấn thương trong quá trình hoạt động hay thể thao.','Nike Air Force 1','nike-air-force-1','ACTIVE','2024-11-08 22:02:34.885948','282fa0ce-e825-4943-bee4-02edfc57b587','8f72a7cb-236d-496e-8108-c0e90a8e558c',1200000,0,0),('7b2fb6de-c4da-49c4-8176-b80cdb16cb6d','2024-11-08 23:12:37.983231','Whether you\'re starting out or just playing for fun, the Club boot gets you on the pitch without compromising on quality. The Superfly 10 is made with speed in mind. Mix that velocity with touch and comfort? It’s goal time.','Nike Mercurial Superfly 10 Club','nike-mercurial-superfly-10-club','ACTIVE','2024-11-08 23:12:37.983231','282fa0ce-e825-4943-bee4-02edfc57b587','eae52c01-6239-498a-8e7e-1aa35836890e',1889000,0,0);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_image`
--

DROP TABLE IF EXISTS `product_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_image` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `image_url` varchar(255) NOT NULL,
  `is_primary` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `product_image_colour_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKes4k49u9ed6hor13wqqu1pjku` (`product_image_colour_id`),
  CONSTRAINT `FKes4k49u9ed6hor13wqqu1pjku` FOREIGN KEY (`product_image_colour_id`) REFERENCES `product_image_colour` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_image`
--

LOCK TABLES `product_image` WRITE;
/*!40000 ALTER TABLE `product_image` DISABLE KEYS */;
INSERT INTO `product_image` VALUES ('0c7190d7-ace4-4594-9c50-5f3c160d9ddf','2024-11-14 13:54:27.428465','http://res.cloudinary.com/ducww5vny/image/upload/v1731567272/uup4bdl821nwfarjltxb.webp',_binary '\0','2024-11-14 13:54:27.428465','4603f06a-02df-4e8c-a99e-24403e2c5640'),('177fe7d7-fbe8-4cf0-b5ee-8ac0cd46c03d','2024-11-14 17:17:52.913966','http://res.cloudinary.com/ducww5vny/image/upload/v1731579470/d3sljy203gcwjbd7naii.webp',_binary '\0','2024-11-14 17:17:52.913966','cad60e03-4104-4a2b-8ecf-e3f6d9dd1838'),('26b631cc-7334-42c9-8c2f-7c744930a01b','2024-11-14 17:17:52.885036','http://res.cloudinary.com/ducww5vny/image/upload/v1731579466/snxak3jbbxspuoeja9mq.webp',_binary '\0','2024-11-14 17:17:52.885036','cad60e03-4104-4a2b-8ecf-e3f6d9dd1838'),('27a1e951-7bf3-4074-a13d-211ddd534afd','2024-11-13 23:03:30.253798','http://res.cloudinary.com/ducww5vny/image/upload/v1731513799/fltv8ugemywwqc1h8gkt.webp',_binary '\0','2024-11-13 23:03:30.253798','f7a2202a-488f-4227-8311-2727b2d539da'),('2a002c59-9004-412c-aaf0-657e8a58c4d1','2024-11-13 23:03:30.306818','http://res.cloudinary.com/ducww5vny/image/upload/v1731513805/xrtnyddbh33qei99ulkv.webp',_binary '\0','2024-11-13 23:03:30.306818','f7a2202a-488f-4227-8311-2727b2d539da'),('2b169ad4-406a-4275-b898-7893e6c8dc90','2024-11-14 17:17:12.854229','http://res.cloudinary.com/ducww5vny/image/upload/v1731579428/tz1a2rrcutae5lpjm4xj.webp',_binary '\0','2024-11-14 17:17:12.854229','9333d420-13f6-42b7-b62f-16243fbd77c6'),('30c90d35-622e-4c1e-a229-694501bfdc46','2024-11-14 17:17:52.861528','http://res.cloudinary.com/ducww5vny/image/upload/v1731579463/zcu4oozdiwed4tisvmbu.webp',_binary '\0','2024-11-14 17:17:52.861528','cad60e03-4104-4a2b-8ecf-e3f6d9dd1838'),('449fcce6-f999-4c3b-876f-69bb93a598ce','2024-11-13 23:03:30.289527','http://res.cloudinary.com/ducww5vny/image/upload/v1731513803/itva7ugba8iywvlvklkj.webp',_binary '\0','2024-11-13 23:03:30.289527','f7a2202a-488f-4227-8311-2727b2d539da'),('4a4c30e3-d966-48e2-bc23-d35d601f03d3','2024-11-14 17:17:12.842045','http://res.cloudinary.com/ducww5vny/image/upload/v1731579426/t4tgkkewdaikq0gvja4t.webp',_binary '\0','2024-11-14 17:17:12.842045','9333d420-13f6-42b7-b62f-16243fbd77c6'),('6231d644-1bcf-4aa1-924e-d369135c0bcf','2024-11-13 23:03:30.328631','http://res.cloudinary.com/ducww5vny/image/upload/v1731513809/qobnkq4wyebarbgkipmd.webp',_binary '\0','2024-11-13 23:03:30.328631','f7a2202a-488f-4227-8311-2727b2d539da'),('8971fa3b-4e05-45b8-a2bc-308d1c8bafa4','2024-11-13 23:03:30.317716','http://res.cloudinary.com/ducww5vny/image/upload/v1731513807/ouifyu1ibhkicr4ht87k.webp',_binary '\0','2024-11-13 23:03:30.317716','f7a2202a-488f-4227-8311-2727b2d539da'),('938c4e33-36e9-4c6c-93b5-8743581985a0','2024-11-14 17:17:52.927414','http://res.cloudinary.com/ducww5vny/image/upload/v1731579472/zfxefjyx8ud2xqowpujc.jpg',_binary '\0','2024-11-14 17:17:52.927414','cad60e03-4104-4a2b-8ecf-e3f6d9dd1838'),('a31d6934-772a-4b7f-a6f9-1fe0725fcab6','2024-11-14 17:17:12.875778','http://res.cloudinary.com/ducww5vny/image/upload/v1731579432/abdfkndiyztlkzmte8r2.webp',_binary '\0','2024-11-14 17:17:12.875778','9333d420-13f6-42b7-b62f-16243fbd77c6'),('a8ec1e11-97f7-4f8f-82ad-2a3f1d4b4892','2024-11-14 17:17:52.899220','http://res.cloudinary.com/ducww5vny/image/upload/v1731579468/jqj2vkarx0g6e26yatsd.webp',_binary '\0','2024-11-14 17:17:52.899220','cad60e03-4104-4a2b-8ecf-e3f6d9dd1838'),('be71dab6-350b-4059-a47c-45b5c60acf35','2024-11-14 13:54:27.406520','http://res.cloudinary.com/ducww5vny/image/upload/v1731567268/nyoxpzioznvzpxjw0e5j.webp',_binary '\0','2024-11-14 13:54:27.406520','4603f06a-02df-4e8c-a99e-24403e2c5640'),('c9ec0399-9d34-4769-a299-7836dd915a18','2024-11-14 17:17:12.864148','http://res.cloudinary.com/ducww5vny/image/upload/v1731579430/owjff71w9jn0d04crthg.webp',_binary '\0','2024-11-14 17:17:12.864148','9333d420-13f6-42b7-b62f-16243fbd77c6'),('da47e7e4-600e-4452-b815-e6721f9213b6','2024-11-14 13:54:27.384631','http://res.cloudinary.com/ducww5vny/image/upload/v1731567264/iuih3zfgsvna5uq7fdb5.webp',_binary '\0','2024-11-14 13:54:27.384631','4603f06a-02df-4e8c-a99e-24403e2c5640'),('e0770466-724e-4755-9660-c835d8d6e420','2024-11-14 17:17:12.809987','http://res.cloudinary.com/ducww5vny/image/upload/v1731579425/rb8n7uenv21k48cbipyr.webp',_binary '\0','2024-11-14 17:17:12.809987','9333d420-13f6-42b7-b62f-16243fbd77c6'),('f428ce2c-a2b7-4f2e-9abf-abc1b5576aa7','2024-11-14 13:54:27.352200','http://res.cloudinary.com/ducww5vny/image/upload/v1731567262/erd4vxpqcmr3xfxlmgds.webp',_binary '\0','2024-11-14 13:54:27.352200','4603f06a-02df-4e8c-a99e-24403e2c5640'),('f7ffb6cb-95c7-49da-94f5-af1529e25058','2024-11-14 13:54:27.396075','http://res.cloudinary.com/ducww5vny/image/upload/v1731567266/upwhajqfypdaclfgdi8r.webp',_binary '\0','2024-11-14 13:54:27.396075','4603f06a-02df-4e8c-a99e-24403e2c5640'),('fe92ea7a-9943-4edb-b269-21a0920caeaa','2024-11-14 13:54:27.417908','http://res.cloudinary.com/ducww5vny/image/upload/v1731567270/prbhpq709xinrcf8ahoi.webp',_binary '\0','2024-11-14 13:54:27.417908','4603f06a-02df-4e8c-a99e-24403e2c5640');
/*!40000 ALTER TABLE `product_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_image_colour`
--

DROP TABLE IF EXISTS `product_image_colour`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_image_colour` (
  `id` varchar(255) NOT NULL,
  `product_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdk4in5sv8c7l97k45410wn0p6` (`product_id`),
  CONSTRAINT `FKdk4in5sv8c7l97k45410wn0p6` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_image_colour`
--

LOCK TABLES `product_image_colour` WRITE;
/*!40000 ALTER TABLE `product_image_colour` DISABLE KEYS */;
INSERT INTO `product_image_colour` VALUES ('9333d420-13f6-42b7-b62f-16243fbd77c6','78fdaddf-d4b1-4bcc-870c-eb9fbb058833'),('cad60e03-4104-4a2b-8ecf-e3f6d9dd1838','78fdaddf-d4b1-4bcc-870c-eb9fbb058833'),('4603f06a-02df-4e8c-a99e-24403e2c5640','7b2fb6de-c4da-49c4-8176-b80cdb16cb6d'),('f7a2202a-488f-4227-8311-2727b2d539da','7b2fb6de-c4da-49c4-8176-b80cdb16cb6d');
/*!40000 ALTER TABLE `product_image_colour` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_variant`
--

DROP TABLE IF EXISTS `product_variant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_variant` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `quantity` int NOT NULL,
  `sku` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `product_id` varchar(255) NOT NULL,
  `product_image_colour_id` varchar(255) NOT NULL,
  `size_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKivtjmjnhkb977nvkx92oyujw8` (`sku`),
  KEY `FKgrbbs9t374m9gg43l6tq1xwdj` (`product_id`),
  KEY `FKjw1oa3uanjrnq4g72o5ofbxpi` (`product_image_colour_id`),
  KEY `FKn1veiq5y5r3fb6qw0n030o7mh` (`size_id`),
  CONSTRAINT `FKgrbbs9t374m9gg43l6tq1xwdj` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKjw1oa3uanjrnq4g72o5ofbxpi` FOREIGN KEY (`product_image_colour_id`) REFERENCES `product_image_colour` (`id`),
  CONSTRAINT `FKn1veiq5y5r3fb6qw0n030o7mh` FOREIGN KEY (`size_id`) REFERENCES `size` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_variant`
--

LOCK TABLES `product_variant` WRITE;
/*!40000 ALTER TABLE `product_variant` DISABLE KEYS */;
INSERT INTO `product_variant` VALUES ('142a6e4c-821f-4ed9-9a18-8eed0149713c','2024-11-14 17:17:57.948785',0,'nike-air-force-1-39-9333d420','2024-11-14 17:17:57.948785','78fdaddf-d4b1-4bcc-870c-eb9fbb058833','9333d420-13f6-42b7-b62f-16243fbd77c6','d0b9eacf-9ff3-11ef-ab59-0242ac110002'),('1ad466a2-6ea3-49a6-9ba7-0c5b0b95ea49','2024-11-14 16:27:53.596949',0,'nike-mercurial-superfly-10-club-37-4603f06a','2024-11-14 16:27:53.596949','7b2fb6de-c4da-49c4-8176-b80cdb16cb6d','4603f06a-02df-4e8c-a99e-24403e2c5640','d0b964d5-9ff3-11ef-ab59-0242ac110002'),('2d5ce999-6b51-4b71-9dd8-b8a415a6b16f','2024-11-14 17:18:04.254182',0,'nike-air-force-1-39-cad60e03','2024-11-14 17:18:04.254182','78fdaddf-d4b1-4bcc-870c-eb9fbb058833','cad60e03-4104-4a2b-8ecf-e3f6d9dd1838','d0b9eacf-9ff3-11ef-ab59-0242ac110002'),('3afb46ac-2e17-4ee6-9fd4-2eda51daffd2','2024-11-14 16:27:53.647775',0,'nike-mercurial-superfly-10-club-38-4603f06a','2024-11-14 16:27:53.647775','7b2fb6de-c4da-49c4-8176-b80cdb16cb6d','4603f06a-02df-4e8c-a99e-24403e2c5640','d0b9dba1-9ff3-11ef-ab59-0242ac110002'),('5beee5f7-5afd-4def-8964-eb89647fde37','2024-11-14 17:18:04.215940',0,'nike-air-force-1-37-cad60e03','2024-11-14 17:18:04.215940','78fdaddf-d4b1-4bcc-870c-eb9fbb058833','cad60e03-4104-4a2b-8ecf-e3f6d9dd1838','d0b964d5-9ff3-11ef-ab59-0242ac110002'),('6261154c-5894-4e1c-9890-45100441c5b2','2024-11-14 16:27:53.677896',0,'nike-mercurial-superfly-10-club-40-4603f06a','2024-11-14 16:27:53.677896','7b2fb6de-c4da-49c4-8176-b80cdb16cb6d','4603f06a-02df-4e8c-a99e-24403e2c5640','d0b9eb45-9ff3-11ef-ab59-0242ac110002'),('7617913e-eab3-4d93-8d52-0c95fd6ce431','2024-11-14 17:18:04.235792',0,'nike-air-force-1-38-cad60e03','2024-11-14 17:18:04.235792','78fdaddf-d4b1-4bcc-870c-eb9fbb058833','cad60e03-4104-4a2b-8ecf-e3f6d9dd1838','d0b9dba1-9ff3-11ef-ab59-0242ac110002'),('95605f5f-b2b7-4a3c-a28f-21ee2535c4dd','2024-11-14 17:17:57.962791',0,'nike-air-force-1-40-9333d420','2024-11-14 17:17:57.962791','78fdaddf-d4b1-4bcc-870c-eb9fbb058833','9333d420-13f6-42b7-b62f-16243fbd77c6','d0b9eb45-9ff3-11ef-ab59-0242ac110002'),('b5947df3-1844-44d3-bce2-060088de89c2','2024-11-14 17:06:50.191643',0,'nike-mercurial-superfly-10-club-38-f7a2202a','2024-11-14 17:06:50.191643','7b2fb6de-c4da-49c4-8176-b80cdb16cb6d','f7a2202a-488f-4227-8311-2727b2d539da','d0b9dba1-9ff3-11ef-ab59-0242ac110002'),('b6e69fa6-96ee-459e-b827-fdd0f274634c','2024-11-14 17:06:50.210571',0,'nike-mercurial-superfly-10-club-39-f7a2202a','2024-11-14 17:06:50.210571','7b2fb6de-c4da-49c4-8176-b80cdb16cb6d','f7a2202a-488f-4227-8311-2727b2d539da','d0b9eacf-9ff3-11ef-ab59-0242ac110002'),('c416f306-e6fb-48de-b870-6b244e7c6b25','2024-11-14 17:17:57.907580',0,'nike-air-force-1-37-9333d420','2024-11-14 17:17:57.907580','78fdaddf-d4b1-4bcc-870c-eb9fbb058833','9333d420-13f6-42b7-b62f-16243fbd77c6','d0b964d5-9ff3-11ef-ab59-0242ac110002'),('d039f21d-35cf-46e1-93fd-fee6a7a2e078','2024-11-14 17:06:50.150620',0,'nike-mercurial-superfly-10-club-37-f7a2202a','2024-11-14 17:06:50.150620','7b2fb6de-c4da-49c4-8176-b80cdb16cb6d','f7a2202a-488f-4227-8311-2727b2d539da','d0b964d5-9ff3-11ef-ab59-0242ac110002'),('f0b789a6-2a8f-4275-80b2-3d9019da5f72','2024-11-14 16:27:53.664916',0,'nike-mercurial-superfly-10-club-39-4603f06a','2024-11-14 16:27:53.664916','7b2fb6de-c4da-49c4-8176-b80cdb16cb6d','4603f06a-02df-4e8c-a99e-24403e2c5640','d0b9eacf-9ff3-11ef-ab59-0242ac110002'),('f0dcfe1e-ae3a-4675-98d2-f89faf24147b','2024-11-14 17:17:57.933455',0,'nike-air-force-1-38-9333d420','2024-11-14 17:17:57.933455','78fdaddf-d4b1-4bcc-870c-eb9fbb058833','9333d420-13f6-42b7-b62f-16243fbd77c6','d0b9dba1-9ff3-11ef-ab59-0242ac110002'),('f12f1a86-6e6d-426e-89ab-34c30a220026','2024-11-14 17:18:04.270660',0,'nike-air-force-1-40-cad60e03','2024-11-14 17:18:04.270660','78fdaddf-d4b1-4bcc-870c-eb9fbb058833','cad60e03-4104-4a2b-8ecf-e3f6d9dd1838','d0b9eb45-9ff3-11ef-ab59-0242ac110002'),('f21395d3-0bb0-4b77-b796-71c091e3de73','2024-11-14 17:06:50.221472',0,'nike-mercurial-superfly-10-club-40-f7a2202a','2024-11-14 17:06:50.221472','7b2fb6de-c4da-49c4-8176-b80cdb16cb6d','f7a2202a-488f-4227-8311-2727b2d539da','d0b9eb45-9ff3-11ef-ab59-0242ac110002');
/*!40000 ALTER TABLE `product_variant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profile`
--

DROP TABLE IF EXISTS `profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profile` (
  `id` varchar(255) NOT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `is_active` bit(1) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `account_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKk8qk4j2lbffv7x78ydpugc6tg` (`account_id`),
  CONSTRAINT `FKlc4oipegt3vyph78q31itt3pf` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profile`
--

LOCK TABLES `profile` WRITE;
/*!40000 ALTER TABLE `profile` DISABLE KEYS */;
INSERT INTO `profile` VALUES ('1ea2bbaf-a0ba-4d54-ae4e-19e232736539','/assets/image/profile-user.png','2025-07-05 18:16:41.793015','2025-07-05','Web',_binary '','Admin','0123456789','2025-07-05 18:16:41.793015','49aff372-d06a-4d52-8d23-0de0098576e1'),('71b8fba0-5866-4384-9fb4-911fb3f19cea','https://lh3.googleusercontent.com/a/ACg8ocKwb4imEdi4mjZSHYuVhueEmGS2nq8aTlhKGiC1ujT6SEJ71Iff=s96-c','2025-07-06 13:56:40.675308','2003-01-31','Bảo',_binary '','Nguyễn','0367422628','2025-07-06 13:57:15.284080','7ee987e5-f0a8-4138-b3ec-3b8358a22f16'),('80aa8a2b-d9c6-4b36-97a8-61ce2b27b28b','/assets/image/profile-user.png','2025-07-05 18:16:41.815101','2025-07-05','Web',_binary '','Customer','0123456789','2025-07-05 18:16:41.815101','0d312ab2-b27d-4cfb-948a-9c2b6179c986');
/*!40000 ALTER TABLE `profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES ('4769ebd8-9992-4a04-b6a5-fa8a984296ce','EMPLOYEE'),('9b2b0a9c-8d6a-4ae8-80d1-d5438c13b65f','CUSTOMER'),('cd021927-7ebd-4dd1-a62f-b80319012c75','ADMIN');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `size`
--

DROP TABLE IF EXISTS `size`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `size` (
  `id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `size`
--

LOCK TABLES `size` WRITE;
/*!40000 ALTER TABLE `size` DISABLE KEYS */;
INSERT INTO `size` VALUES ('d0b964d5-9ff3-11ef-ab59-0242ac110002','2024-11-11 06:11:39.000000','37','2024-11-11 06:11:39.000000'),('d0b9dba1-9ff3-11ef-ab59-0242ac110002','2024-11-11 06:11:39.000000','38','2024-11-11 06:11:39.000000'),('d0b9eacf-9ff3-11ef-ab59-0242ac110002','2024-11-11 06:11:39.000000','39','2024-11-11 06:11:39.000000'),('d0b9eb45-9ff3-11ef-ab59-0242ac110002','2024-11-11 06:11:39.000000','40','2024-11-11 06:11:39.000000');
/*!40000 ALTER TABLE `size` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verification_token`
--

DROP TABLE IF EXISTS `verification_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `verification_token` (
  `id` varchar(255) NOT NULL,
  `expiry_date` datetime(6) NOT NULL,
  `token` varchar(255) NOT NULL,
  `account_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhcdftkkphim5iwk2f9ffmm8bt` (`account_id`),
  CONSTRAINT `FKs8je6hs7qhfs93obh8dqml9fe` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verification_token`
--

LOCK TABLES `verification_token` WRITE;
/*!40000 ALTER TABLE `verification_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `verification_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'siu'
--

--
-- Dumping routines for database 'siu'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-25 17:02:44

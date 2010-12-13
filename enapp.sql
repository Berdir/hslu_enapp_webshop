-- MySQL dump 10.13  Distrib 5.1.49-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: enapp
-- ------------------------------------------------------
-- Server version	5.1.49-MariaDB-mariadb82-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(15) NOT NULL COMMENT 'have to be unique',
  `password` varchar(64) DEFAULT NULL COMMENT 'md5 hash',
  `name` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `email` varchar(90) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (4,'Berdir','a94a8fe5ccb19ba61c4c0873d391e987982fbbd3','Sascha Grossenbacher','Hauptstrasse 21','saschagros@gmail.com'),(13,'test','a94a8fe5ccb19ba61c4c0873d391e987982fbbd3','test','test','test'),(14,'da','cdd4f874095045f4ae6670038cbbd05fac9d4802','da','da','da'),(15,'du','fa114377ef35b25ecaa4d57e5084641543e02588','du','du','du'),(16,'user','9d4e1e23bd5b727046a9e3b4b7db57bd8d6ee684','Name2','addresse','mail');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_group`
--

DROP TABLE IF EXISTS `customer_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_group` (
  `groupname` varchar(45) NOT NULL,
  `username` varchar(15) NOT NULL,
  PRIMARY KEY (`groupname`,`username`),
  KEY `username_2` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_group`
--

LOCK TABLES `customer_group` WRITE;
/*!40000 ALTER TABLE `customer_group` DISABLE KEYS */;
INSERT INTO `customer_group` VALUES ('USER','Berdir'),('USER','da'),('USER','du'),('USER','test'),('USER','user');
/*!40000 ALTER TABLE `customer_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT 'contain surname and last name',
  `description` varchar(255) DEFAULT NULL,
  `mediapath` varchar(180) DEFAULT NULL COMMENT 'relative path to the media file',
  `unitprice` decimal(10,0) DEFAULT NULL,
  `reference` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'bla','Scherzo In Minore OP. 20 (Chopin)','image\\ART0000224_0.jpg','1','ART0000224'),(2,'bla','Bach: Toccata&Fuge - Berühmte Orgelwerke','image\\ART0000225_0.jpg','9','ART0000225'),(3,'bla',' Don\'t Let me Bring Down','image\\ART0000226_0.jpg','1','ART0000226'),(4,'bla','Combat Circus - 01 - Tortuga','music\\Talco - Combat Circus\\01 - Tortuga.mp3','1','ART0000227'),(5,'bla','Combat Circus - 02 - La Sedia Vuota','music\\Talco - Combat Circus\\02 - La Sedia Vuota.mp3','1','ART0000228'),(6,'bla','Combat Circus - 03 - Il Passo Del Caciurdo','music\\Talco - Combat Circus\\03 - Il Passo Del Caciurdo.mp3','1','ART0000229'),(7,'bla','Combat Circus - 04 - Combat Circus','music\\Talco - Combat Circus\\04 - Combat Circus.mp3','1','ART0000230'),(8,'bla','Combat Circus - 05 - Venghino, Signori Venghino','music\\Talco - Combat Circus\\05 - Venghino, Signori Venghino.mp3','1','ART0000231'),(9,'bla','Combat Circus - 06 - La Carovana','music\\Talco - Combat Circus\\06 - La Carovana.mp3','1','ART0000232'),(10,'bla','Combat Circus - 07 - Testamento Di Un Buffone','music\\Talco - Combat Circus\\07 - Testamento Di Un Buffone.mp3','1','ART0000233'),(11,'bla','Combat Circus - 08 - Oro Nero.mp3','music\\Talco - Combat Circus\\08 - Oro Nero.mp3','1','ART0000234'),(12,'bla','Combat Circus - 09 - Bella Ciao','music\\Talco - Combat Circus\\09 - Bella Ciao.mp3','1','ART0000235'),(13,'bla','Combat Circus - 10 - La Fabbrica Del Dissenso','music\\Talco - Combat Circus\\10 - La Fabbrica Del Dissenso.mp3','1','ART0000236'),(14,'bla','Combat Circus - 11 - A La Patchanka','music\\Talco - Combat Circus\\11 - A La Patchanka.mp3','1','ART0000237'),(15,'bla','Combat Circus - 12 - Diari Perduti','music\\Talco - Combat Circus\\12 - Diari Perduti.mp3','1','ART0000238'),(16,'bla','Talco - Combat Circus','music\\Talco - Combat Circus.zip','10','ART0000239'),(17,'bla','Green Eyes - 01 - Green Eyes','music\\Jerry Lain - Green Eyes\\Green Eyes - Jerry Lain.mp3','1','ART0000240'),(18,'bla','Green Eyes - 02 - Hard Rock Song','music\\Jerry Lain - Green Eyes\\HARD ROCK SONG.mp3','1','ART0000241'),(19,'bla','Green Eyes - 03 - It\'s over','music\\Jerry Lain - Green Eyes\\It\'s over - Jerry Lain_cut.mp3','1','ART0000242'),(20,'bla','Green Eyes - 04 - Ja oder Nein','music\\Jerry Lain - Green Eyes\\JA ODER NEIN.mp3','1','ART0000243'),(21,'bla','Green Eyes - 05 - Wohin','music\\Jerry Lain - Green Eyes\\WOHIN_   .mp3','1','ART0000244'),(22,'bla','Jerry Lain - Green Eyes','music\\Jerry Lane - Green Eyes.zip','10','ART0000245'),(23,'bla','Rock My Reality - 01 - Bully','music\\P. O. Box - Rock My Reality\\01 - Bully.mp3','1','ART0000246'),(24,'bla','Rock My Reality - 02 - Whatever They Said','music\\P. O. Box - Rock My Reality\\02 - Whatever They Said.mp3','1','ART0000247'),(25,'bla','Rock My Reality - 03 - Death Promises Me A Better ','music\\P. O. Box - Rock My Reality\\03 - Death Promises Me A Better Place.mp3','1','ART0000248'),(26,'bla','Rock My Reality - 04 - As Time Flies By','music\\P. O. Box - Rock My Reality\\04 - As Time Flies By.mp3','1','ART0000249'),(27,'bla','Rock My Reality - 05 - Diving','music\\P. O. Box - Rock My Reality\\05 - Diving.mp3','1','ART0000250'),(28,'bla','Rock My Reality - 06 - Broken Hearts and Credit Ca','music\\P. O. Box - Rock My Reality\\06 - Broken Hearts and Credit Cards.mp3','1','ART0000251'),(29,'bla','Rock My Reality - 07 - Strike Back','music\\P. O. Box - Rock My Reality\\07 - Strike Back.mp3','1','ART0000252'),(30,'bla','P. O. Box - Rock My Reality','music\\P. O. Box - Rock My Reality.zip','10','ART0000253'),(31,'bla','Sophomore Jinx - 01 - I Do','music\\Rob Costlow - Solo Piano - Sophomore Jinx\\01 - I Do.mp3','1','ART0000254'),(32,'bla','Sophomore Jinx - 02 - L.A. Passing By','music\\Rob Costlow - Solo Piano - Sophomore Jinx\\02 - L.A. Passing By.mp3','1','ART0000255'),(33,'bla','Sophomore Jinx - 03 - Lost or Found','music\\Rob Costlow - Solo Piano - Sophomore Jinx\\03 - Lost or Found.mp3','1','ART0000256'),(34,'bla','Sophomore Jinx - 04 - Summer Garden','music\\Rob Costlow - Solo Piano - Sophomore Jinx\\04 - Summer Garden.mp3','1','ART0000257'),(35,'bla','Sophomore Jinx - 05 - Bliss','music\\Rob Costlow - Solo Piano - Sophomore Jinx\\05 - Bliss.mp3','1','ART0000258'),(36,'bla','Sophomore Jinx - 06 - Goodbyes','music\\Rob Costlow - Solo Piano - Sophomore Jinx\\06 - Goodbyes.mp3','1','ART0000259'),(37,'bla','Sophomore Jinx - 07 - Forever','music\\Rob Costlow - Solo Piano - Sophomore Jinx\\07 - Forever.mp3','1','ART0000260'),(38,'bla','Sophomore Jinx - 08 - Oceans','music\\Rob Costlow - Solo Piano - Sophomore Jinx\\08 - Oceans.mp3','1','ART0000261'),(39,'bla','Sophomore Jinx - 09 - Family','music\\Rob Costlow - Solo Piano - Sophomore Jinx\\09 - Family.mp3','1','ART0000262'),(40,'bla','Rob Costlow - Solo Piano - Sophomore Jinx','Rob Costlow - Solo Piano - Sophomore Jinx.zip','10','ART0000263');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase`
--

DROP TABLE IF EXISTS `purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `customerid` int(10) unsigned NOT NULL,
  `datetime` datetime DEFAULT NULL COMMENT 'Date / Time of purchase',
  `status` varchar(15) DEFAULT NULL COMMENT 'state of purchase',
  PRIMARY KEY (`id`),
  KEY `customerid` (`customerid`),
  CONSTRAINT `purchase_ibfk_1` FOREIGN KEY (`customerid`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase`
--

LOCK TABLES `purchase` WRITE;
/*!40000 ALTER TABLE `purchase` DISABLE KEYS */;
INSERT INTO `purchase` VALUES (1,4,'2010-10-26 10:44:09','Ordered'),(2,4,'2010-10-26 10:45:37','Ordered'),(3,4,'2010-10-28 11:16:12','Ordered'),(4,4,'2010-10-28 19:02:04','Ordered'),(5,4,'2010-10-28 19:03:43','Ordered'),(7,4,'2010-10-29 09:02:40','Ordered'),(8,14,'2010-10-29 09:06:15','Ordered'),(9,16,'2010-10-29 10:09:39','Ordered'),(10,4,'2010-11-02 00:15:48','Ordered'),(11,4,'2010-11-02 00:22:28','Ordered');
/*!40000 ALTER TABLE `purchase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchaseitem`
--

DROP TABLE IF EXISTS `purchaseitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchaseitem` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `purchaseid` int(10) unsigned NOT NULL,
  `productid` int(10) unsigned NOT NULL,
  `quantity` decimal(10,0) DEFAULT NULL COMMENT 'by a mp3 shop, generally one',
  `unitprice` decimal(10,0) DEFAULT NULL,
  `lineamount` decimal(10,0) DEFAULT NULL COMMENT 'total cost per line',
  `description` varchar(90) DEFAULT NULL COMMENT 'line description f.e. a comment',
  PRIMARY KEY (`id`),
  KEY `purchaseid` (`purchaseid`),
  KEY `productid` (`productid`),
  CONSTRAINT `purchaseitem_ibfk_1` FOREIGN KEY (`purchaseid`) REFERENCES `purchase` (`id`),
  CONSTRAINT `purchaseitem_ibfk_2` FOREIGN KEY (`productid`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchaseitem`
--

LOCK TABLES `purchaseitem` WRITE;
/*!40000 ALTER TABLE `purchaseitem` DISABLE KEYS */;
INSERT INTO `purchaseitem` VALUES (1,1,2,'1','9',NULL,NULL),(2,1,15,'1','1',NULL,NULL),(3,1,6,'1','1',NULL,NULL),(4,1,1,'1','1',NULL,NULL),(5,2,16,'1','10',NULL,NULL),(6,3,1,'1','1',NULL,NULL),(7,4,5,'1','1',NULL,NULL),(8,4,13,'1','1',NULL,NULL),(9,4,1,'1','1',NULL,NULL),(10,5,1,'1','1',NULL,NULL),(11,5,9,'1','1',NULL,NULL),(12,5,4,'1','1',NULL,NULL),(14,7,8,'1','1',NULL,NULL),(15,8,1,'1','1',NULL,NULL),(16,9,6,'1','1',NULL,NULL),(17,9,13,'1','1',NULL,NULL),(18,9,9,'1','1',NULL,NULL),(19,9,1,'1','1',NULL,NULL),(20,10,1,'1','1',NULL,NULL),(21,11,1,'1','1',NULL,NULL);
/*!40000 ALTER TABLE `purchaseitem` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-11-05  8:51:08

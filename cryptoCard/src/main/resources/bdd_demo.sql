# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Hôte: 127.0.0.1 (MySQL 5.7.17)
# Base de données: smartcards
# Temps de génération: 2017-01-13 15:25:38 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Affichage de la table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(30) NOT NULL,
  `password` varchar(32) NOT NULL,
  `sel` int(11) NOT NULL,
  `public_key` text,
  `number_to_sign` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`id`, `login`, `password`, `sel`, `public_key`, `number_to_sign`)
VALUES
	(3,'kevinfs','ff91677a3bce79539528a638cbbc3ad4',17,'MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAETdK8yRG3RlJl4DWMlMr54I7eWKqD/ushvbevyulVq96YDNas/7L6galhe1t9590JHyHsQeXt7nbTRmO1vEbD7w==',87),
	(5,'sami','a69a2d677ce704c44132c9d8a222bd9b',297,'MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEjOp8Zfh2e2SB+GvFNn6tk5LahS8FGIGDvjNySOL9u/+wqoXX1Q4f5mYPSc2ByfTi6WMTRgwU+/J9r7VXvTs+nw==',12),
	(6,'anselme','e0983286484528331bec485140d6f2d0',297,'MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEdHioXWySGa7JBg1H0mOGxeVE+PZNnPcczNWjSRwbW93TB0KLyDcn0onOCTfPq1p+QeGkpVBsRm+MPW9yKmjDEA==',130);

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema xwatch.transport
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema xwatch.transport
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `xwatch.transport` DEFAULT CHARACTER SET utf8 ;
USE `xwatch.transport` ;

-- -----------------------------------------------------
-- Table `xwatch.transport`.`tbl_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `xwatch.transport`.`tbl_role` (
  `role.name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`role.name`),
  UNIQUE INDEX `role.name_UNIQUE` (`role.name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `xwatch.transport`.`tbl_account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `xwatch.transport`.`tbl_account` (
  `user.name` VARCHAR(32) NOT NULL,
  `user.password` VARCHAR(45) NOT NULL,
  `user.email` VARCHAR(45) NOT NULL,
  `role.name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`user.name`),
  UNIQUE INDEX `user.name_UNIQUE` (`user.name` ASC),
  UNIQUE INDEX `user.email_UNIQUE` (`user.email` ASC),
  INDEX `fk_role_idx` (`role.name` ASC),
  CONSTRAINT `fk_role`
    FOREIGN KEY (`role.name`)
    REFERENCES `xwatch.transport`.`tbl_role` (`role.name`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `xwatch.transport`.`tbl_dns`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `xwatch.transport`.`tbl_dns` (
  `dns.name` VARCHAR(45) NOT NULL,
  `dns.isUsed` TINYINT(1) NULL DEFAULT '0',
  PRIMARY KEY (`dns.name`),
  UNIQUE INDEX `dns.name_UNIQUE` (`dns.name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `xwatch.transport`.`tbl_theme`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `xwatch.transport`.`tbl_theme` (
  `theme.id` INT(11) NOT NULL AUTO_INCREMENT,
  `theme.name` VARCHAR(45) NOT NULL,
  `theme.location` VARCHAR(500) NOT NULL,
  `theme.isUsed` TINYINT(1) NULL DEFAULT '0',
  PRIMARY KEY (`theme.id`),
  UNIQUE INDEX `theme.location_UNIQUE` (`theme.location` ASC),
  UNIQUE INDEX `theme.name_UNIQUE` (`theme.name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `xwatch.transport`.`tbl_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `xwatch.transport`.`tbl_status` (
  `status.name` VARCHAR(45) NOT NULL,
  `status.description` VARCHAR(200) NULL DEFAULT NULL,
  PRIMARY KEY (`status.name`),
  UNIQUE INDEX `status.name_UNIQUE` (`status.name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `xwatch.transport`.`tbl_transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `xwatch.transport`.`tbl_transaction` (
  `trans.id` INT(11) NOT NULL AUTO_INCREMENT,
  `trans.time` DATETIME NOT NULL,
  `trans.user.request` VARCHAR(45) NOT NULL,
  `trans.dns` VARCHAR(45) NOT NULL,
  `trans.theme` VARCHAR(45) NOT NULL,
  `trans.status` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`trans.id`),
  INDEX `fk.user.request_idx` (`trans.user.request` ASC),
  INDEX `fk.dns_idx` (`trans.dns` ASC),
  INDEX `fk.theme_idx` (`trans.theme` ASC),
  INDEX `fk.status_idx` (`trans.status` ASC),
  CONSTRAINT `fk.dns`
    FOREIGN KEY (`trans.dns`)
    REFERENCES `xwatch.transport`.`tbl_dns` (`dns.name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk.status`
    FOREIGN KEY (`trans.status`)
    REFERENCES `xwatch.transport`.`tbl_status` (`status.name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk.theme`
    FOREIGN KEY (`trans.theme`)
    REFERENCES `xwatch.transport`.`tbl_theme` (`theme.name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk.user.request`
    FOREIGN KEY (`trans.user.request`)
    REFERENCES `xwatch.transport`.`tbl_account` (`user.name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `xwatch.transport`.`tbl_deployment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `xwatch.transport`.`tbl_deployment` (
  `deployment.id` INT(11) NOT NULL AUTO_INCREMENT,
  `deployment.transaction` INT(11) NOT NULL,
  `deployment.dns` VARCHAR(45) NOT NULL,
  `deployment.theme` VARCHAR(45) NOT NULL,
  `deployment.host` VARCHAR(45) NOT NULL,
  `deployment.port` INT(11) NOT NULL,
  `deployment.location` VARCHAR(500) NOT NULL,
  `deployment.using` TINYINT(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`deployment.id`),
  INDEX `fk_transaction_idx` (`deployment.transaction` ASC),
  INDEX `fk_dns_idx` (`deployment.dns` ASC),
  INDEX `fk_theme_idx` (`deployment.theme` ASC),
  CONSTRAINT `fk_dns`
    FOREIGN KEY (`deployment.dns`)
    REFERENCES `xwatch.transport`.`tbl_dns` (`dns.name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_theme`
    FOREIGN KEY (`deployment.theme`)
    REFERENCES `xwatch.transport`.`tbl_theme` (`theme.name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_transaction`
    FOREIGN KEY (`deployment.transaction`)
    REFERENCES `xwatch.transport`.`tbl_transaction` (`trans.id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

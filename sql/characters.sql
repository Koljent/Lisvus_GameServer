-- ---------------------------
-- Table structure for characters
-- ---------------------------
CREATE TABLE IF NOT EXISTS characters (
  account_name VARCHAR(45) DEFAULT NULL,
  obj_Id INT UNSIGNED NOT NULL DEFAULT 0,
  char_name VARCHAR(35) NOT NULL,
  name_color MEDIUMINT UNSIGNED NOT NULL DEFAULT '16777215',
  level TINYINT UNSIGNED DEFAULT NULL,
  maxHp MEDIUMINT UNSIGNED DEFAULT NULL,
  curHp MEDIUMINT UNSIGNED DEFAULT NULL,
  maxCp MEDIUMINT UNSIGNED DEFAULT NULL,
  curCp MEDIUMINT UNSIGNED DEFAULT NULL,
  maxMp MEDIUMINT UNSIGNED DEFAULT NULL,
  curMp MEDIUMINT UNSIGNED DEFAULT NULL,
  face TINYINT UNSIGNED DEFAULT NULL,
  hairStyle TINYINT UNSIGNED DEFAULT NULL,
  hairColor TINYINT UNSIGNED DEFAULT NULL,
  sex TINYINT UNSIGNED DEFAULT NULL,
  heading MEDIUMINT DEFAULT NULL,
  x MEDIUMINT DEFAULT NULL,
  y MEDIUMINT DEFAULT NULL,
  z MEDIUMINT DEFAULT NULL,
  exp BIGINT UNSIGNED DEFAULT 0,
  sp INT UNSIGNED NOT NULL DEFAULT 0,
  karma INT UNSIGNED DEFAULT NULL,
  pvpkills SMALLINT UNSIGNED DEFAULT NULL,
  pkkills SMALLINT UNSIGNED DEFAULT NULL,
  clanid INT UNSIGNED DEFAULT NULL,
  race TINYINT UNSIGNED DEFAULT NULL,
  classid TINYINT UNSIGNED DEFAULT NULL,
  base_class TINYINT UNSIGNED NOT NULL DEFAULT 0,
  deletetime BIGINT DEFAULT NULL,
  cancraft TINYINT UNSIGNED DEFAULT NULL,
  title VARCHAR(16) DEFAULT NULL,
  rec_have TINYINT UNSIGNED NOT NULL DEFAULT 0,
  rec_left TINYINT UNSIGNED NOT NULL DEFAULT 0,
  accesslevel MEDIUMINT DEFAULT 0,
  online TINYINT UNSIGNED DEFAULT NULL,
  onlinetime INT DEFAULT NULL,
  char_slot TINYINT UNSIGNED DEFAULT NULL,
  lastAccess BIGINT UNSIGNED DEFAULT NULL,
  clan_privs MEDIUMINT UNSIGNED DEFAULT 0,
  wantspeace TINYINT UNSIGNED DEFAULT 0,
  clan_join_expiry_time BIGINT UNSIGNED NOT NULL DEFAULT 0,
  clan_create_expiry_time BIGINT UNSIGNED NOT NULL DEFAULT 0,
  in_jail TINYINT UNSIGNED DEFAULT 0,
  jail_timer INT UNSIGNED DEFAULT 0,
  nobless TINYINT UNSIGNED NOT NULL DEFAULT 0,
  last_recom_date BIGINT UNSIGNED NOT NULL DEFAULT 0,
  varka_ketra_ally TINYINT NOT NULL DEFAULT 0,
  aio_buffer TINYINT NOT NULL DEFAULT 0,
  newbie_at BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY  (obj_Id),
  KEY `clanid` (`clanid`)
);
# mineauth

![Java CI with Gradle](https://github.com/AethLi/mineauth/workflows/Java%20CI%20with%20Gradle/badge.svg)
[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![change logs](https://img.shields.io/badge/change%20logs-0.3.0-yellow)](change_logs.md)

## Feature
1. Server-level authentication for both online-mode true/false;
2. Ban player with sql;(coming soon at 1.0)
3. Reset player's password;(coming soon at 1.0)

## Installation
1. Stop your server.
2. Download jar file, put into $MINECRAFT_SERVER_PATH/mods/.
3. Restart server.
4. (Optional)Modify $MINECRAFT_SERVER_PATH/world/serverconfig/mineauth-server.toml and restart.

## Usage
- Register: /register \<password\> \<password confirm\>
- Login: /login \<password\>
- Change password: /changePassword \<old password\> \<new password\> \<password confirm\>

## Database
- Support: 
    1. h2 database(default) 
    2. MySQL(have not test yet)
- References to create table
```sql
create schema MINEAUTH;
create table PLAYERS
(
    `ID`         VARCHAR(36) not null
        primary key,
    `USERNAME`   VARCHAR(40),
    `UUID`       VARCHAR(40) not null,
    `EMAIL`      VARCHAR(40),
    `BANNED`     VARCHAR(5),
    `PASSWORD`   VARCHAR(40) not null,
    `LAST_LOGIN` TIMESTAMP
);
``` 

## Plan
- Chest lock support
- Internationalization support
- 1.17 support

## Internationalization
- Coming soon 

## Backup
- mineauth/ folder is default internal database file, you should keep it.

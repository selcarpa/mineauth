# 0.4.0

### New feature

1. Changed database column
    1. add ip,ipv6,forget column
2. Add /smurfCheck command
3. /forgetPassword command will not write to file

### Attention:

1. Internal database file is not compatible, you should update it([How to update](0.3.1_to_0.4.0.md))
2. $MINECRAFT_SERVER_PATH/world/serverconfig/mineauth-server.toml is updated, In case of modified it, you should backup
   and re-edit it
   
# 0.3.1

### Fixed

1. Update sql wrong(Caused some serious errors)
2. "enableAccountModule=false" does not take effect correctly

### Attention:

1. This version is a bug fix version and does not contain functional updates.

# 0.3.0

### Fixed

1. Some datasource hot-modify error
2. First-startup error
3. Player(unauthorised) cannot been kicked out correctly sometimes
4. Version range error that caused mineauth cannot run on 1.16.1 correctly

### New feature

1. ForgetPassword command
2. Add more i18n customization options(You can put your own i18n file into $MINECRAFT_SERVER_PATH/mineauth/i18n/ ,
   Rename it to xxx.json[File example](src/main/resources/assets/mineauth/json/i18n), And edit
   $MINECRAFT_SERVER_PATH/world/serverconfig/mineauth-server.toml.)

### Attention:

1. Internal database file is fully compatible, you can keep mineauth/ folder
2. $MINECRAFT_SERVER_PATH/world/serverconfig/mineauth-server.toml is updated, In case of modified it, you should backup
   and re-edit it

# 0.2.0

1. Refactor code(ready for chest lock module)
2. Fixed a bug that cause username field cannot been saved
3. Disable banner print of default config
4. Add i18n support(has not test)
5. Build a mysql-support jar file
6. Add identifierSet command(but forgetPassword not completely)

### Knew issues

1. First start will fail, it won't happen in second startup.

### Attention:

1. Internal database file is fully compatible, you can keep mineauth/ folder
2. $MINECRAFT_SERVER_PATH/world/serverconfig/mineauth-server.toml is updated, In case of modified it, you should backup
   and re-edit it

# 0.1.0

1. It works
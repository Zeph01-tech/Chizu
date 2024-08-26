package com.discord.chizu.models;

import com.mysqlorm.module.entities.Model;
import com.mysqlorm.module.entities.annotations.AutoIncrement;
import com.mysqlorm.module.entities.annotations.PrimaryKey;
import com.mysqlorm.module.entities.annotations.Table;
import com.mysqlorm.module.entities.annotations.Varchar;

@Table("atlas")
public class AtlasModel extends Model {

  @PrimaryKey
  @AutoIncrement
  int entry;

  @Varchar(50)
  String place;
}

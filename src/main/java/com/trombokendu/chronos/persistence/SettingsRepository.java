package com.trombokendu.chronos.persistence;
import java.sql.*;
public final class SettingsRepository extends JdbcRepository<Setting,String>{
 public SettingsRepository(ConnectionPool p){super(p);}protected String selectOneSql(){return "SELECT key,value FROM settings WHERE key=?";}protected String selectAllSql(){return "SELECT key,value FROM settings ORDER BY key";}protected String insertSql(){return "INSERT INTO settings(key,value) VALUES(?,?) ON CONFLICT(key) DO UPDATE SET value=excluded.value";}protected Setting map(ResultSet r)throws SQLException{return new Setting(r.getString("key"),r.getString("value"));}protected void bind(PreparedStatement s,Setting v)throws SQLException{s.setString(1,v.key());s.setString(2,v.value());}protected void bindId(PreparedStatement s,String id)throws SQLException{s.setString(1,id);}
}

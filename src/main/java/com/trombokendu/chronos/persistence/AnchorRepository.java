package com.trombokendu.chronos.persistence;
import java.sql.*;import java.time.Instant;
public final class AnchorRepository extends JdbcRepository<Anchor,Long>{
 public AnchorRepository(ConnectionPool p){super(p);}protected String selectOneSql(){return "SELECT * FROM anchors WHERE seq=?";}protected String selectAllSql(){return "SELECT * FROM anchors ORDER BY seq";}protected String insertSql(){return "INSERT INTO anchors(seq,hash,exported_at) VALUES(?,?,?)";}protected Anchor map(ResultSet r)throws SQLException{return new Anchor(r.getLong("seq"),r.getString("hash"),Instant.ofEpochMilli(r.getLong("exported_at")));}protected void bind(PreparedStatement s,Anchor a)throws SQLException{s.setLong(1,a.seq());s.setString(2,a.hash());s.setLong(3,a.exportedAt().toEpochMilli());}protected void bindId(PreparedStatement s,Long id)throws SQLException{s.setLong(1,id);}
}

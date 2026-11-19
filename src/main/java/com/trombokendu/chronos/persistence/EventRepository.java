package com.trombokendu.chronos.persistence;

import com.trombokendu.chronos.domain.*;
import com.trombokendu.chronos.ledger.StoredEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.*;
import java.util.*;

/** JDBC event DAO. Call save inside the writer's UnitOfWork for atomic append semantics. */
public final class EventRepository extends JdbcRepository<StoredEvent,Long> {
    public EventRepository(ConnectionPool pool){super(pool);}
    protected String selectOneSql(){return "SELECT * FROM events WHERE seq=?";}
    protected String selectAllSql(){return "SELECT * FROM events ORDER BY seq";}
    protected String insertSql(){return "INSERT INTO events(event_id,type,at,payload,prev_hash,hash) VALUES(?,?,?,?,?,?)";}
    protected void bindId(PreparedStatement s,Long id)throws SQLException{s.setLong(1,id);}
    protected void bind(PreparedStatement s,StoredEvent e)throws SQLException{s.setString(1,e.event().eventId().toString());s.setString(2,e.event().getClass().getSimpleName());s.setLong(3,e.event().at().toEpochMilli());s.setString(4,encode(e.event()));s.setString(5,e.prevHash());s.setString(6,e.hash());}
    protected StoredEvent map(ResultSet r)throws SQLException{return new StoredEvent(r.getLong("seq"),decode(r.getString("type"),r.getString("payload")),r.getString("prev_hash"),r.getString("hash"));}
    public void insert(Connection c,StoredEvent e){try(PreparedStatement s=c.prepareStatement(insertSql())){bind(s,e);s.executeUpdate();}catch(SQLException x){throw new PersistenceException("Event insert failed",x);}}
    private static String encode(LedgerEvent e){try{ByteArrayOutputStream b=new ByteArrayOutputStream();DataOutputStream d=new DataOutputStream(b);switch(e){case TransactionPosted x->{d.writeUTF(x.valueDate().toString());d.writeUTF(x.kind().name());d.writeUTF(x.category().name());d.writeUTF(x.amount().toString());d.writeUTF(x.note());d.writeUTF(x.groupId().orElse(""));d.writeUTF(x.payer().orElse(""));}case TransactionVoided x->{d.writeUTF(x.originalEventId().toString());d.writeUTF(x.reason());}case BudgetSet x->d.writeUTF(x.monthlyBudget().toString());case RecurringRuleDefined x->{d.writeUTF(x.category().name());d.writeUTF(x.amount().toString());d.writeInt(x.dayOfMonth());d.writeUTF(x.description());}}return e.eventId()+"|"+e.at().toEpochMilli()+"|"+Base64.getEncoder().encodeToString(b.toByteArray());}catch(IOException impossible){throw new UncheckedIOException(impossible);}}
    private static LedgerEvent decode(String type,String payload)throws SQLException{try{String[] parts=payload.split("\\|",3);DataInputStream d=new DataInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(parts[2])));return switch(type){case "TransactionPosted"->new TransactionPosted(readId(payload),readAt(payload),LocalDate.parse(d.readUTF()),Kind.valueOf(d.readUTF()),Category.valueOf(d.readUTF()),Money.of(d.readUTF()),d.readUTF(),Optional.ofNullable(empty(d.readUTF())),Optional.ofNullable(empty(d.readUTF())));case "TransactionVoided"->new TransactionVoided(readId(payload),readAt(payload),UUID.fromString(d.readUTF()),d.readUTF());case "BudgetSet"->new BudgetSet(readId(payload),readAt(payload),Money.of(d.readUTF()));case "RecurringRuleDefined"->new RecurringRuleDefined(readId(payload),readAt(payload),Category.valueOf(d.readUTF()),Money.of(d.readUTF()),d.readInt(),d.readUTF());default->throw new SQLException("Unknown event type "+type);};}catch(Exception e){throw new SQLException("Invalid event payload",e);}}
    // The event envelope is stored in the payload prefix to keep the database schema compact.
    private static UUID readId(String p){return UUID.fromString(p.split("\\|",3)[0]);}
    private static Instant readAt(String p){return Instant.ofEpochMilli(Long.parseLong(p.split("\\|",3)[1]));}
    private static String empty(String s){return s.isEmpty()?null:s;}
}

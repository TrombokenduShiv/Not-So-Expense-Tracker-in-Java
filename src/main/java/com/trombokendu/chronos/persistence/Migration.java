package com.trombokendu.chronos.persistence; import java.sql.*; public interface Migration { int version(); void apply(Connection c) throws SQLException; }

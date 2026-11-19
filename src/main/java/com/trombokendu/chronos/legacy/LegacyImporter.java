package com.trombokendu.chronos.legacy;

import com.trombokendu.chronos.domain.*;
import com.trombokendu.chronos.ledger.EventStore;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.*;
import java.util.*;

/** One-time importer. The shim class preserves the old stream's binary class name. */
public final class LegacyImporter {
    private LegacyImporter() { }
    public static int importIfPresent(Path file, EventStore store, Clock clock) {
        if (!Files.exists(file) || !store.all().isEmpty()) return 0;
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            Object listObject = in.readObject();
            Object budgetObject = in.readObject();
            if (!(listObject instanceof List<?> list) || !(budgetObject instanceof BigDecimal budget))
                throw new ValidationException("Unexpected legacy serialization shape");
            List<LegacyTransaction> rows = new ArrayList<>();
            for (Object value : list) rows.add(read(value));
            for (LegacyTransaction t : rows) {
                Kind kind = "Income".equalsIgnoreCase(t.type()) ? Kind.INCOME : Kind.EXPENSE;
                store.append(new TransactionPosted(UUID.nameUUIDFromBytes((t.id()+"|"+t.date()+"|"+t.amount()).getBytes(java.nio.charset.StandardCharsets.UTF_8)),
                    clock.instant(), t.date(), kind, Category.fromDisplay(t.category()), new Money(t.amount()),
                    t.description(), Optional.empty(), Optional.empty()));
            }
            store.append(new BudgetSet(UUID.randomUUID(), clock.instant(), new Money(budget)));
            Files.move(file, file.resolveSibling(file.getFileName()+".migrated"), StandardCopyOption.REPLACE_EXISTING);
            return rows.size();
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenceException("Legacy import failed; preserve the source file and use the CSV fallback", e);
        }
    }
    private static LegacyTransaction read(Object value) {
        try {
            Class<?> c=value.getClass();
            return new LegacyTransaction((int)field(c,value,"id"), (LocalDate)field(c,value,"date"),
                (String)field(c,value,"type"), (String)field(c,value,"category"),
                (BigDecimal)field(c,value,"amount"), (String)field(c,value,"description"));
        } catch (ReflectiveOperationException e) { throw new PersistenceException("Legacy class shape mismatch",e); }
    }
    private static Object field(Class<?> c,Object o,String name)throws ReflectiveOperationException {
        Field f=c.getDeclaredField(name);f.setAccessible(true);return f.get(o);
    }
}

package com.trombokendu.chronos.ledger;
import com.trombokendu.chronos.domain.*;import java.nio.charset.StandardCharsets;import java.security.*;import java.util.*;
public final class HashChain {public static final String GENESIS="0".repeat(64);private HashChain(){}
 public static String next(String prev,LedgerEvent e){try{MessageDigest d=MessageDigest.getInstance("SHA-256");return HexFormat.of().formatHex(d.digest((prev+canonical(e)).getBytes(StandardCharsets.UTF_8)));}catch(NoSuchAlgorithmException x){throw new IllegalStateException(x);}}
 public static String canonical(LedgerEvent e){return switch(e){case TransactionPosted x->join(x.eventId(),x.at(),x.valueDate(),x.kind(),x.category(),x.amount(),x.note(),x.groupId().orElse(""),x.payer().orElse(""));case TransactionVoided x->join(x.eventId(),x.at(),x.originalEventId(),x.reason());case BudgetSet x->join(x.eventId(),x.at(),x.monthlyBudget());case RecurringRuleDefined x->join(x.eventId(),x.at(),x.category(),x.amount(),x.dayOfMonth(),x.description());};}
 private static String join(Object...v){return Arrays.stream(v).map(x->String.valueOf(x).replace("\\","\\\\").replace("|","\\|").replace("\n","\\n")).reduce((a,b)->a+"|"+b).orElse("");}
 public static VerificationReport verify(Iterable<StoredEvent> all){String prev=GENESIS;for(StoredEvent s:all){String expected=next(prev,s.event());if(!prev.equals(s.prevHash())||!expected.equals(s.hash()))return new VerificationReport(false,OptionalLong.of(s.seq()),"Hash mismatch");prev=s.hash();}return new VerificationReport(true,OptionalLong.empty(),"Chain intact");}
 public record VerificationReport(boolean valid,OptionalLong brokenSeq,String message){}
}

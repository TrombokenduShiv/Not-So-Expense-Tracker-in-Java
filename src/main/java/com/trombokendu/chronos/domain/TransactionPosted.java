package com.trombokendu.chronos.domain;
import java.time.*;import java.util.*;
public record TransactionPosted(UUID eventId,Instant at,LocalDate valueDate,Kind kind,Category category,Money amount,String note,Optional<String> groupId,Optional<String> payer) implements LedgerEvent {
 public TransactionPosted {Objects.requireNonNull(eventId);Objects.requireNonNull(at);Objects.requireNonNull(valueDate);Objects.requireNonNull(kind);Objects.requireNonNull(category);Objects.requireNonNull(amount);note=Objects.requireNonNullElse(note,"");groupId=groupId==null?Optional.empty():groupId;payer=payer==null?Optional.empty():payer;if(!amount.isPositive())throw new ValidationException("Transaction amount must be positive");}
}

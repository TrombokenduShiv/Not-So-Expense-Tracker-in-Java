package com.trombokendu.chronos.ledger; import com.trombokendu.chronos.domain.*; public record StoredEvent(long seq,LedgerEvent event,String prevHash,String hash) {}

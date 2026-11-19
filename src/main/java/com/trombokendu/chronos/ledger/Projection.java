package com.trombokendu.chronos.ledger; import com.trombokendu.chronos.domain.*; public interface Projection<S>{S empty();S apply(S state,LedgerEvent event);}

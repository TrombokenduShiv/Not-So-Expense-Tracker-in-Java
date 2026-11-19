package com.trombokendu.chronos.persistence;
import java.time.Instant;
public record Anchor(long seq,String hash,Instant exportedAt) { }

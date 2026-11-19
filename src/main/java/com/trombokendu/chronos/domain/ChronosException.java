package com.trombokendu.chronos.domain;
public abstract class ChronosException extends RuntimeException { private static final long serialVersionUID = 1L; protected ChronosException(String m){super(m);} protected ChronosException(String m,Throwable c){super(m,c);} }

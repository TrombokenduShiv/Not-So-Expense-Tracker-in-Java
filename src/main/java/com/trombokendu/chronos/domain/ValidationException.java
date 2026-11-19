package com.trombokendu.chronos.domain;
public final class ValidationException extends ChronosException { private static final long serialVersionUID = 1L; public ValidationException(String m){super(m);} public ValidationException(String m,Throwable c){super(m,c);} }

package com.trombokendu.chronos.domain;
import java.math.*;
public record Money(BigDecimal amount) implements Comparable<Money> {
 public static final Money ZERO=new Money(BigDecimal.ZERO);
 public Money { if(amount==null) throw new ValidationException("Amount is required"); amount=amount.setScale(2,RoundingMode.HALF_EVEN); }
 public static Money of(String s){try{return new Money(new BigDecimal(s));}catch(NumberFormatException e){throw new ValidationException("Invalid money: "+s,e);}}
 public Money plus(Money x){return new Money(amount.add(x.amount));} public Money minus(Money x){return new Money(amount.subtract(x.amount));} public Money negate(){return new Money(amount.negate());}
 public boolean isPositive(){return amount.signum()>0;} public int compareTo(Money x){return amount.compareTo(x.amount);} public String toString(){return amount.toPlainString();}
}

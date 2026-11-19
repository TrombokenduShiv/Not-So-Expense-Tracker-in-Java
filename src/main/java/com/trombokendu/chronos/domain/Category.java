package com.trombokendu.chronos.domain;
public enum Category {
 FOOD(Nature.DISCRETIONARY,"Food"),TRAVEL(Nature.DISCRETIONARY,"Travel"),EDUCATION(Nature.DISCRETIONARY,"Education"),SHOPPING(Nature.DISCRETIONARY,"Shopping"),RENT(Nature.FIXED,"Rent"),BILLS(Nature.FIXED,"Bills"),HEALTH(Nature.DISCRETIONARY,"Health"),ENTERTAINMENT(Nature.DISCRETIONARY,"Entertainment"),SALARY(Nature.INCOME,"Salary"),ALLOWANCE(Nature.INCOME,"Allowance"),OTHER(Nature.DISCRETIONARY,"Other");
 public enum Nature{FIXED,DISCRETIONARY,INCOME} private final Nature nature; private final String displayName; Category(Nature n,String d){nature=n;displayName=d;} public Nature nature(){return nature;} public String displayName(){return displayName;} public static Category fromDisplay(String s){for(Category c:values())if(c.displayName.equalsIgnoreCase(s)||c.name().equalsIgnoreCase(s))return c;throw new ValidationException("Unknown category: "+s);}
}

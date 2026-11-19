package com.trombokendu.chronos.analytics;import java.util.*;public interface AnomalyRule{Optional<String> check(double amount,String category,String description);}

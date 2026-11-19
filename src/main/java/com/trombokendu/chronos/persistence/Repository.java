package com.trombokendu.chronos.persistence;import java.util.*;public interface Repository<T,ID>{Optional<T> find(ID id);List<T> findAll();void save(T value);}

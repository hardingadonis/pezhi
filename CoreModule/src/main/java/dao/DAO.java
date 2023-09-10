package dao;

import java.util.*;

public interface DAO<T> {
    public List<T> getAll();

    public Optional<T> get(Integer ID);

    public void insert(T obj);

    public void update(T obj);

    public void delete(Integer ID);
}

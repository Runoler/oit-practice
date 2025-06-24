package org.example.dao;
import org.example.DBConnectionProvider;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDAO<T, ID> {
    protected Connection dbConnection;

    // Конструктор теперь принимает Connection
    public AbstractDAO(Connection connection) {
        this.dbConnection = connection;
    }

    public abstract List<T> findAll();
    public abstract Optional<T> findById(ID id);
    public abstract Optional<T> create(T entity);
    public abstract Optional<T> update(T entity);
    public abstract boolean delete(ID id);
}

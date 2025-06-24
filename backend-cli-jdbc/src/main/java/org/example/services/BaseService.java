package org.example.services;

import java.sql.Connection;

abstract class BaseService {
    protected Connection connection;

    public BaseService(Connection connection) {
        this.connection = connection;
    }

    // Метод для получения нового соединения или использования существующего (в более сложной архитектуре)
    // В данном случае просто возвращает переданное соединение
    protected Connection getConnection() {
        return this.connection;
    }
}

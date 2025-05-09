package com.hospital.dao;

import com.hospital.exception.HospitalException;
import com.hospital.util.LoggingUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T> {
    protected final Connection connection;
    protected final String tableName;
    protected final String idColumn;

    protected BaseDAO(Connection connection, String tableName, String idColumn) {
        if (connection == null) {
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Database connection cannot be null");
        }
        this.connection = connection;
        this.tableName = tableName;
        this.idColumn = idColumn;
    }

    protected abstract T extractEntity(ResultSet rs) throws SQLException;
    protected abstract String getInsertQuery();
    protected abstract String getUpdateQuery();
    protected abstract void setInsertParameters(PreparedStatement stmt, T entity) throws SQLException;
    protected abstract void setUpdateParameters(PreparedStatement stmt, T entity) throws SQLException;

    public boolean insert(T entity) {
        if (entity == null) {
            throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                "Entity cannot be null for insert operation");
        }

        String sql = getInsertQuery();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setInsertParameters(stmt, entity);
            int result = stmt.executeUpdate();
            LoggingUtils.debug("Inserted " + result + " row(s) into " + tableName);
            return result > 0;
        } catch (SQLException e) {
            LoggingUtils.error("Failed to insert into " + tableName, e);
            throw new HospitalException(HospitalException.ErrorCode.DATABASE_ERROR, 
                "Failed to insert record into " + tableName, e);
        }
    }

    public boolean update(T entity) {
        if (entity == null) {
            throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                "Entity cannot be null for update operation");
        }

        String sql = getUpdateQuery();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setUpdateParameters(stmt, entity);
            int result = stmt.executeUpdate();
            LoggingUtils.debug("Updated " + result + " row(s) in " + tableName);
            return result > 0;
        } catch (SQLException e) {
            LoggingUtils.error("Failed to update " + tableName, e);
            throw new HospitalException(HospitalException.ErrorCode.DATABASE_ERROR, 
                "Failed to update record in " + tableName, e);
        }
    }

    public boolean delete(int id) {
        if (id <= 0) {
            throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                "Invalid ID for delete operation");
        }

        String sql = "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int result = stmt.executeUpdate();
            LoggingUtils.debug("Deleted " + result + " row(s) from " + tableName);
            return result > 0;
        } catch (SQLException e) {
            LoggingUtils.error("Failed to delete from " + tableName, e);
            throw new HospitalException(HospitalException.ErrorCode.DATABASE_ERROR, 
                "Failed to delete record from " + tableName, e);
        }
    }

    public T findById(int id) {
        if (id <= 0) {
            throw new HospitalException(HospitalException.ErrorCode.VALIDATION_ERROR, 
                "Invalid ID for find operation");
        }

        String sql = "SELECT * FROM " + tableName + " WHERE " + idColumn + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractEntity(rs);
            }
            LoggingUtils.debug("No record found in " + tableName + " with ID: " + id);
            return null;
        } catch (SQLException e) {
            LoggingUtils.error("Failed to find record in " + tableName, e);
            throw new HospitalException(HospitalException.ErrorCode.DATABASE_ERROR, 
                "Failed to find record in " + tableName, e);
        }
    }

    public List<T> findAll() {
        String sql = "SELECT * FROM " + tableName;
        List<T> entities = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                entities.add(extractEntity(rs));
            }
            LoggingUtils.debug("Found " + entities.size() + " records in " + tableName);
            return entities;
        } catch (SQLException e) {
            LoggingUtils.error("Failed to find all records in " + tableName, e);
            throw new HospitalException(HospitalException.ErrorCode.DATABASE_ERROR, 
                "Failed to find all records in " + tableName, e);
        }
    }

    protected void validateConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                    "Database connection is not available");
            }
        } catch (SQLException e) {
            throw new HospitalException(HospitalException.ErrorCode.SYSTEM_ERROR, 
                "Failed to validate database connection", e);
        }
    }
} 
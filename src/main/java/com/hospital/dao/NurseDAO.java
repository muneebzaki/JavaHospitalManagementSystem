package com.hospital.dao;

import com.hospital.entities.Nurse;
import com.hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NurseDAO implements INurseDAO {
    private final Connection connection;

    public NurseDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public boolean insertNurse(Nurse nurse) {
        String sql = "INSERT INTO nurses (name, specialization, is_available) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nurse.getName());
            stmt.setString(2, nurse.getDepartment());
            stmt.setBoolean(3, nurse.isAvailable());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        nurse.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateNurse(Nurse nurse) {
        String sql = "UPDATE nurses SET name=?, specialization=?, is_available=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nurse.getName());
            stmt.setString(2, nurse.getDepartment());
            stmt.setBoolean(3, nurse.isAvailable());
            stmt.setInt(4, nurse.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteNurse(int id) {
        String sql = "DELETE FROM nurses WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Nurse findById(int id) {
        String sql = "SELECT * FROM nurses WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractNurse(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Nurse> findAll() {
        String sql = "SELECT * FROM nurses";
        List<Nurse> nurses = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                nurses.add(extractNurse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nurses;
    }

    @Override
    public List<Nurse> findByDepartment(String department) {
        String sql = "SELECT * FROM nurses WHERE specialization=?";
        List<Nurse> nurses = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, department);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                nurses.add(extractNurse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nurses;
    }

    @Override
    public List<Nurse> findAvailable() {
        String sql = "SELECT * FROM nurses WHERE is_available=true";
        List<Nurse> nurses = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                nurses.add(extractNurse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nurses;
    }

    private Nurse extractNurse(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String department = rs.getString("specialization");
        boolean isAvailable = rs.getBoolean("is_available");
        
        Nurse nurse = new Nurse(id, name, department);
        nurse.setAvailable(isAvailable);
        return nurse;
    }
} 
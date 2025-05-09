package com.hospital.dao;

import com.hospital.entities.Nurse;
import java.util.List;

public interface INurseDAO {
    boolean insertNurse(Nurse nurse);
    boolean updateNurse(Nurse nurse);
    boolean deleteNurse(int id);
    Nurse findById(int id);
    List<Nurse> findAll();
    List<Nurse> findByDepartment(String department);
    List<Nurse> findAvailable();
} 
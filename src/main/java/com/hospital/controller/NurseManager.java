package com.hospital.controller;

import com.hospital.dao.INurseDAO;
import com.hospital.dao.NurseDAO;
import com.hospital.entities.Nurse;

import java.time.LocalDateTime;
import java.util.List;

public class NurseManager {
    private final INurseDAO nurseDAO;

    public NurseManager() {
        this.nurseDAO = new NurseDAO();
    }

    public boolean addNurse(Nurse nurse) {
        return nurseDAO.insertNurse(nurse);
    }

    public boolean updateNurse(Nurse nurse) {
        return nurseDAO.updateNurse(nurse);
    }

    public boolean removeNurse(int id) {
        return nurseDAO.deleteNurse(id);
    }

    public Nurse findNurseById(int id) {
        return nurseDAO.findById(id);
    }

    public List<Nurse> getAllNurses() {
        return nurseDAO.findAll();
    }

    public List<Nurse> getNursesByDepartment(String department) {
        return nurseDAO.findByDepartment(department);
    }

    public List<Nurse> getAvailableNurses() {
        return nurseDAO.findAvailable();
    }

    public boolean nurseExists(int id) {
        return findNurseById(id) != null;
    }

    public boolean scheduleNurseShift(int nurseId, LocalDateTime shiftTime) {
        Nurse nurse = findNurseById(nurseId);
        if (nurse != null && nurse.isAvailable() && !nurse.hasShiftAt(shiftTime)) {
            nurse.addToSchedule(shiftTime);
            return updateNurse(nurse);
        }
        return false;
    }

    public boolean cancelNurseShift(int nurseId, LocalDateTime shiftTime) {
        Nurse nurse = findNurseById(nurseId);
        if (nurse != null && nurse.hasShiftAt(shiftTime)) {
            nurse.removeFromSchedule(shiftTime);
            return updateNurse(nurse);
        }
        return false;
    }

    public void listAllNurses() {
        List<Nurse> all = getAllNurses();
        if (all.isEmpty()) {
            System.out.println("No nurses found.");
        } else {
            all.forEach(System.out::println);
        }
    }
} 
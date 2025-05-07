package controller;

import entities.Doctor;
import entities.Nurse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DoctorManager {
    private List<Doctor> doctors;
    private List<Nurse> nurses;

    public DoctorManager() {
        this.doctors = new ArrayList<>();
        this.nurses = new ArrayList<>();
    }

    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    public void addNurse(Nurse nurse) {
        nurses.add(nurse);
    }

    public List<Doctor> getAvailableDoctors(LocalDateTime time) {
        return doctors.stream()
                .filter(doctor -> doctor.isAvailable() && !doctor.hasAppointmentAt(time))
                .collect(Collectors.toList());
    }

    public List<Nurse> getAvailableNurses(LocalDateTime time) {
        return nurses.stream()
                .filter(nurse -> nurse.isAvailable() && !nurse.hasShiftAt(time))
                .collect(Collectors.toList());
    }

    public boolean scheduleDoctorAppointment(int doctorId, LocalDateTime appointmentTime) {
        Doctor doctor = doctors.stream()
                .filter(d -> d.getId() == doctorId)
                .findFirst()
                .orElse(null);

        if (doctor != null && doctor.isAvailable() && !doctor.hasAppointmentAt(appointmentTime)) {
            doctor.addToSchedule(appointmentTime);
            return true;
        }
        return false;
    }

    public boolean scheduleNurseShift(int nurseId, LocalDateTime shiftTime) {
        Nurse nurse = nurses.stream()
                .filter(n -> n.getId() == nurseId)
                .findFirst()
                .orElse(null);

        if (nurse != null && nurse.isAvailable() && !nurse.hasShiftAt(shiftTime)) {
            nurse.addToSchedule(shiftTime);
            return true;
        }
        return false;
    }

    public boolean cancelDoctorAppointment(int doctorId, LocalDateTime appointmentTime) {
        Doctor doctor = doctors.stream()
                .filter(d -> d.getId() == doctorId)
                .findFirst()
                .orElse(null);

        if (doctor != null && doctor.hasAppointmentAt(appointmentTime)) {
            doctor.removeFromSchedule(appointmentTime);
            return true;
        }
        return false;
    }

    public boolean cancelNurseShift(int nurseId, LocalDateTime shiftTime) {
        Nurse nurse = nurses.stream()
                .filter(n -> n.getId() == nurseId)
                .findFirst()
                .orElse(null);

        if (nurse != null && nurse.hasShiftAt(shiftTime)) {
            nurse.removeFromSchedule(shiftTime);
            return true;
        }
        return false;
    }

    public List<Doctor> getDoctors() {
        return new ArrayList<>(doctors);
    }

    public List<Nurse> getNurses() {
        return new ArrayList<>(nurses);
    }
} 
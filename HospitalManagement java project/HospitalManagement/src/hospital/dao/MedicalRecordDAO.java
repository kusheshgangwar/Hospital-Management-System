package hospital.dao;

import hospital.db.DatabaseConnection;
import hospital.model.MedicalRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDAO {

    public boolean addRecord(MedicalRecord r) {
        String sql = "INSERT INTO medical_records (patient_id, doctor_id, diagnosis, prescription, record_date) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, r.getPatientId());
            ps.setInt(2, r.getDoctorId());
            ps.setString(3, r.getDiagnosis());
            ps.setString(4, r.getPrescription());
            ps.setString(5, r.getRecordDate());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<MedicalRecord> getAllRecords() {
        List<MedicalRecord> list = new ArrayList<>();
        String sql = "SELECT mr.*, p.name as patient_name, d.name as doctor_name " +
                     "FROM medical_records mr " +
                     "JOIN patients p ON mr.patient_id = p.id " +
                     "JOIN doctors d ON mr.doctor_id = d.id " +
                     "ORDER BY mr.id DESC";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                MedicalRecord r = new MedicalRecord();
                r.setId(rs.getInt("id"));
                r.setPatientId(rs.getInt("patient_id"));
                r.setDoctorId(rs.getInt("doctor_id"));
                r.setPatientName(rs.getString("patient_name"));
                r.setDoctorName(rs.getString("doctor_name"));
                r.setDiagnosis(rs.getString("diagnosis"));
                r.setPrescription(rs.getString("prescription"));
                r.setRecordDate(rs.getString("record_date"));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteRecord(int id) {
        String sql = "DELETE FROM medical_records WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

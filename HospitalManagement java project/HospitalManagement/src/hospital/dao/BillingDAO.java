package hospital.dao;

import hospital.db.DatabaseConnection;
import hospital.model.Bill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillingDAO {

    public boolean generateBill(Bill b) {
        String sql = "INSERT INTO bills (patient_id, consultation_fee, medicine_fee, room_charges, other_charges, total_amount, payment_status, bill_date) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, b.getPatientId());
            ps.setDouble(2, b.getConsultationFee());
            ps.setDouble(3, b.getMedicineFee());
            ps.setDouble(4, b.getRoomCharges());
            ps.setDouble(5, b.getOtherCharges());
            ps.setDouble(6, b.getTotalAmount());
            ps.setString(7, b.getPaymentStatus());
            ps.setString(8, b.getBillDate());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Bill> getAllBills() {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT b.*, p.name as patient_name FROM bills b JOIN patients p ON b.patient_id = p.id ORDER BY b.id DESC";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Bill b = new Bill();
                b.setId(rs.getInt("id"));
                b.setPatientId(rs.getInt("patient_id"));
                b.setPatientName(rs.getString("patient_name"));
                b.setConsultationFee(rs.getDouble("consultation_fee"));
                b.setMedicineFee(rs.getDouble("medicine_fee"));
                b.setRoomCharges(rs.getDouble("room_charges"));
                b.setOtherCharges(rs.getDouble("other_charges"));
                b.setTotalAmount(rs.getDouble("total_amount"));
                b.setPaymentStatus(rs.getString("payment_status"));
                b.setBillDate(rs.getString("bill_date"));
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updatePaymentStatus(int id, String status) {
        String sql = "UPDATE bills SET payment_status=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

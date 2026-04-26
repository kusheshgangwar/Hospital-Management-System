package hospital.gui;

import hospital.dao.AppointmentDAO;
import hospital.dao.DoctorDAO;
import hospital.dao.PatientDAO;
import hospital.model.Appointment;
import hospital.model.Doctor;
import hospital.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class AppointmentPanel extends JPanel {

    private AppointmentDAO apptDAO = new AppointmentDAO();
    private PatientDAO patientDAO  = new PatientDAO();
    private DoctorDAO doctorDAO    = new DoctorDAO();

    private JTable table;
    private DefaultTableModel tableModel;

    private JComboBox<Patient> cmbPatient;
    private JComboBox<Doctor>  cmbDoctor;
    private JTextField txtDate, txtTime, txtNotes;
    private JComboBox<String> cmbStatus;
    private int selectedApptId = -1;

    public AppointmentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 255, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        loadTable();
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(46, 139, 87), 2),
            "Book Appointment", 0, 0,
            new Font("Arial", Font.BOLD, 13), new Color(46, 139, 87)));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        cmbPatient = new JComboBox<>();
        cmbDoctor  = new JComboBox<>();
        txtDate    = new JTextField(LocalDate.now().toString(), 12);
        txtTime    = new JTextField("10:00 AM", 10);
        txtNotes   = new JTextField(20);
        cmbStatus  = new JComboBox<>(new String[]{"Scheduled", "Completed", "Cancelled"});

        loadPatientCombo();
        loadDoctorCombo();

        g.gridx=0; g.gridy=0; panel.add(bold("Patient:"), g);
        g.gridx=1;             panel.add(cmbPatient, g);
        g.gridx=2;             panel.add(bold("Doctor:"), g);
        g.gridx=3;             panel.add(cmbDoctor, g);

        g.gridx=0; g.gridy=1; panel.add(bold("Date (YYYY-MM-DD):"), g);
        g.gridx=1;             panel.add(txtDate, g);
        g.gridx=2;             panel.add(bold("Time:"), g);
        g.gridx=3;             panel.add(txtTime, g);

        g.gridx=0; g.gridy=2; panel.add(bold("Status:"), g);
        g.gridx=1;             panel.add(cmbStatus, g);
        g.gridx=2;             panel.add(bold("Notes:"), g);
        g.gridx=3;             panel.add(txtNotes, g);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnPanel.setBackground(Color.WHITE);
        JButton btnBook   = styledBtn("📅 Book",   new Color(46, 139, 87));
        JButton btnUpdate = styledBtn("✏️ Update", new Color(70, 130, 180));
        JButton btnDelete = styledBtn("🗑 Delete", new Color(220, 53, 69));
        JButton btnClear  = styledBtn("🔄 Clear",  new Color(108, 117, 125));

        btnBook.addActionListener(e -> bookAppointment());
        btnUpdate.addActionListener(e -> updateStatus());
        btnDelete.addActionListener(e -> deleteAppointment());
        btnClear.addActionListener(e -> clearForm());

        btnPanel.add(btnBook); btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete); btnPanel.add(btnClear);

        g.gridx=0; g.gridy=3; g.gridwidth=4;
        panel.add(btnPanel, g);
        return panel;
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 255, 250));

        String[] cols = {"ID", "Patient", "Doctor", "Date", "Time", "Status", "Notes"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        styleTable(table);
        table.getSelectionModel().addListSelectionListener(e -> fillFromTable());

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void bookAppointment() {
        Patient p = (Patient) cmbPatient.getSelectedItem();
        Doctor  d = (Doctor)  cmbDoctor.getSelectedItem();
        if (p == null || d == null) return;

        Appointment a = new Appointment();
        a.setPatientId(p.getId());
        a.setDoctorId(d.getId());
        a.setAppointmentDate(txtDate.getText().trim());
        a.setAppointmentTime(txtTime.getText().trim());
        a.setStatus((String) cmbStatus.getSelectedItem());
        a.setNotes(txtNotes.getText().trim());

        if (apptDAO.addAppointment(a)) {
            JOptionPane.showMessageDialog(this, "Appointment booked! ✅");
            clearForm(); loadTable();
        }
    }

    private void updateStatus() {
        if (selectedApptId == -1) {
            JOptionPane.showMessageDialog(this, "Appointment select karo!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (apptDAO.updateStatus(selectedApptId, (String) cmbStatus.getSelectedItem())) {
            JOptionPane.showMessageDialog(this, "Status updated! ✅");
            loadTable();
        }
    }

    private void deleteAppointment() {
        if (selectedApptId == -1) return;
        int c = JOptionPane.showConfirmDialog(this, "Delete karna chahte ho?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION && apptDAO.deleteAppointment(selectedApptId)) {
            JOptionPane.showMessageDialog(this, "Deleted! 🗑");
            clearForm(); loadTable();
        }
    }

    private void fillFromTable() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            selectedApptId = (int) tableModel.getValueAt(row, 0);
            cmbStatus.setSelectedItem(tableModel.getValueAt(row, 5));
            txtNotes.setText((String) tableModel.getValueAt(row, 6));
        }
    }

    private void clearForm() {
        txtDate.setText(LocalDate.now().toString());
        txtTime.setText("10:00 AM"); txtNotes.setText("");
        cmbStatus.setSelectedIndex(0); selectedApptId = -1;
        table.clearSelection();
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Appointment a : apptDAO.getAllAppointments()) {
            tableModel.addRow(new Object[]{a.getId(), a.getPatientName(), a.getDoctorName(),
                a.getAppointmentDate(), a.getAppointmentTime(), a.getStatus(), a.getNotes()});
        }
    }

    private void loadPatientCombo() {
        for (Patient p : patientDAO.getAllPatients()) cmbPatient.addItem(p);
    }
    private void loadDoctorCombo() {
        for (Doctor d : doctorDAO.getAllDoctors()) cmbDoctor.addItem(d);
    }

    public void refreshCombos() {
        cmbPatient.removeAllItems(); cmbDoctor.removeAllItems();
        loadPatientCombo(); loadDoctorCombo(); loadTable();
    }

    private void styleTable(JTable t) {
        t.setRowHeight(26);
        t.setFont(new Font("Arial", Font.PLAIN, 13));
        t.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        t.getTableHeader().setBackground(new Color(46, 139, 87));
        t.getTableHeader().setForeground(Color.WHITE);
        t.setSelectionBackground(new Color(144, 238, 144));
    }

    private JLabel bold(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        return l;
    }

    private JButton styledBtn(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        return btn;
    }
}

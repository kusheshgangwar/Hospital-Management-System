package hospital.gui;

import hospital.dao.DoctorDAO;
import hospital.dao.MedicalRecordDAO;
import hospital.dao.PatientDAO;
import hospital.model.Doctor;
import hospital.model.MedicalRecord;
import hospital.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class MedicalRecordPanel extends JPanel {

    private MedicalRecordDAO recDAO   = new MedicalRecordDAO();
    private PatientDAO patientDAO     = new PatientDAO();
    private DoctorDAO doctorDAO       = new DoctorDAO();

    private JTable table;
    private DefaultTableModel tableModel;

    private JComboBox<Patient> cmbPatient;
    private JComboBox<Doctor>  cmbDoctor;
    private JTextArea txtDiagnosis, txtPrescription;
    private JTextField txtDate;
    private int selectedId = -1;

    public MedicalRecordPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(255, 250, 245));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        loadTable();
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(210, 105, 30), 2),
            "Add Medical Record", 0, 0,
            new Font("Arial", Font.BOLD, 13), new Color(210, 105, 30)));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.fill = GridBagConstraints.BOTH;

        cmbPatient      = new JComboBox<>();
        cmbDoctor       = new JComboBox<>();
        txtDate         = new JTextField(LocalDate.now().toString(), 12);
        txtDiagnosis    = new JTextArea(3, 25);
        txtPrescription = new JTextArea(3, 25);
        txtDiagnosis.setLineWrap(true);
        txtPrescription.setLineWrap(true);

        for (Patient p : patientDAO.getAllPatients()) cmbPatient.addItem(p);
        for (Doctor d : doctorDAO.getAllDoctors())    cmbDoctor.addItem(d);

        g.gridx=0; g.gridy=0; panel.add(bold("Patient:"), g);
        g.gridx=1;             panel.add(cmbPatient, g);
        g.gridx=2;             panel.add(bold("Doctor:"), g);
        g.gridx=3;             panel.add(cmbDoctor, g);
        g.gridx=4;             panel.add(bold("Date:"), g);
        g.gridx=5;             panel.add(txtDate, g);

        g.gridx=0; g.gridy=1; panel.add(bold("Diagnosis:"), g);
        g.gridx=1; g.gridwidth=2; panel.add(new JScrollPane(txtDiagnosis), g);
        g.gridwidth=1;
        g.gridx=3;             panel.add(bold("Prescription:"), g);
        g.gridx=4; g.gridwidth=2; panel.add(new JScrollPane(txtPrescription), g);
        g.gridwidth=1;

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnPanel.setBackground(Color.WHITE);
        JButton btnAdd    = styledBtn("➕ Add Record",  new Color(210, 105, 30));
        JButton btnDelete = styledBtn("🗑 Delete",      new Color(220, 53, 69));
        JButton btnClear  = styledBtn("🔄 Clear",       new Color(108, 117, 125));

        btnAdd.addActionListener(e -> addRecord());
        btnDelete.addActionListener(e -> deleteRecord());
        btnClear.addActionListener(e -> clearForm());

        btnPanel.add(btnAdd); btnPanel.add(btnDelete); btnPanel.add(btnClear);

        g.gridx=0; g.gridy=2; g.gridwidth=6;
        panel.add(btnPanel, g);

        return panel;
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 250, 245));

        String[] cols = {"ID", "Patient", "Doctor", "Diagnosis", "Prescription", "Date"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(210, 105, 30));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(255, 222, 173));
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) selectedId = (int) tableModel.getValueAt(row, 0);
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void addRecord() {
        Patient p = (Patient) cmbPatient.getSelectedItem();
        Doctor  d = (Doctor)  cmbDoctor.getSelectedItem();
        if (p == null || txtDiagnosis.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient aur Diagnosis required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        MedicalRecord r = new MedicalRecord();
        r.setPatientId(p.getId());
        r.setDoctorId(d != null ? d.getId() : 0);
        r.setDiagnosis(txtDiagnosis.getText().trim());
        r.setPrescription(txtPrescription.getText().trim());
        r.setRecordDate(txtDate.getText().trim());

        if (recDAO.addRecord(r)) {
            JOptionPane.showMessageDialog(this, "Record saved! ✅");
            clearForm(); loadTable();
        }
    }

    private void deleteRecord() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Record select karo!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int c = JOptionPane.showConfirmDialog(this, "Delete?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION && recDAO.deleteRecord(selectedId)) {
            JOptionPane.showMessageDialog(this, "Deleted! 🗑");
            clearForm(); loadTable();
        }
    }

    private void clearForm() {
        txtDiagnosis.setText(""); txtPrescription.setText("");
        txtDate.setText(LocalDate.now().toString());
        selectedId = -1; table.clearSelection();
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (MedicalRecord r : recDAO.getAllRecords()) {
            tableModel.addRow(new Object[]{r.getId(), r.getPatientName(), r.getDoctorName(),
                r.getDiagnosis(), r.getPrescription(), r.getRecordDate()});
        }
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

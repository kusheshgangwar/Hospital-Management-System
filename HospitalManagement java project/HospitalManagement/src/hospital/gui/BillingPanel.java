package hospital.gui;

import hospital.dao.BillingDAO;
import hospital.dao.PatientDAO;
import hospital.model.Bill;
import hospital.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class BillingPanel extends JPanel {

    private BillingDAO billingDAO = new BillingDAO();
    private PatientDAO patientDAO = new PatientDAO();

    private JTable table;
    private DefaultTableModel tableModel;

    private JComboBox<Patient> cmbPatient;
    private JTextField txtConsultation, txtMedicine, txtRoom, txtOther, txtTotal, txtDate;
    private JComboBox<String> cmbStatus;
    private int selectedBillId = -1;

    public BillingPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(253, 245, 255));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        loadTable();
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(138, 43, 226), 2),
            "Generate Bill", 0, 0,
            new Font("Arial", Font.BOLD, 13), new Color(138, 43, 226)));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        cmbPatient      = new JComboBox<>();
        txtConsultation = new JTextField("500", 8);
        txtMedicine     = new JTextField("0", 8);
        txtRoom         = new JTextField("0", 8);
        txtOther        = new JTextField("0", 8);
        txtTotal        = new JTextField("0", 8);
        txtTotal.setEditable(false);
        txtTotal.setBackground(new Color(230, 255, 230));
        txtDate         = new JTextField(LocalDate.now().toString(), 12);
        cmbStatus       = new JComboBox<>(new String[]{"Pending", "Paid", "Partial"});

        for (Patient p : patientDAO.getAllPatients()) cmbPatient.addItem(p);

        // Auto-calculate total
        java.awt.event.KeyAdapter calc = new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { calculateTotal(); }
        };
        txtConsultation.addKeyListener(calc);
        txtMedicine.addKeyListener(calc);
        txtRoom.addKeyListener(calc);
        txtOther.addKeyListener(calc);

        g.gridx=0; g.gridy=0; panel.add(bold("Patient:"), g);
        g.gridx=1;             panel.add(cmbPatient, g);
        g.gridx=2;             panel.add(bold("Date:"), g);
        g.gridx=3;             panel.add(txtDate, g);
        g.gridx=4;             panel.add(bold("Status:"), g);
        g.gridx=5;             panel.add(cmbStatus, g);

        g.gridx=0; g.gridy=1; panel.add(bold("Consultation (₹):"), g);
        g.gridx=1;             panel.add(txtConsultation, g);
        g.gridx=2;             panel.add(bold("Medicine (₹):"), g);
        g.gridx=3;             panel.add(txtMedicine, g);
        g.gridx=4;             panel.add(bold("Room Charges (₹):"), g);
        g.gridx=5;             panel.add(txtRoom, g);

        g.gridx=0; g.gridy=2; panel.add(bold("Other (₹):"), g);
        g.gridx=1;             panel.add(txtOther, g);

        JLabel lblTotal = bold("💰 TOTAL (₹):");
        lblTotal.setForeground(new Color(138, 43, 226));
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        g.gridx=2;             panel.add(lblTotal, g);
        txtTotal.setFont(new Font("Arial", Font.BOLD, 14));
        g.gridx=3;             panel.add(txtTotal, g);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnPanel.setBackground(Color.WHITE);
        JButton btnGenerate = styledBtn("🧾 Generate Bill", new Color(138, 43, 226));
        JButton btnPaid     = styledBtn("✅ Mark Paid",     new Color(46, 139, 87));
        JButton btnClear    = styledBtn("🔄 Clear",         new Color(108, 117, 125));

        btnGenerate.addActionListener(e -> generateBill());
        btnPaid.addActionListener(e -> markPaid());
        btnClear.addActionListener(e -> clearForm());

        btnPanel.add(btnGenerate); btnPanel.add(btnPaid); btnPanel.add(btnClear);

        g.gridx=0; g.gridy=3; g.gridwidth=6;
        panel.add(btnPanel, g);
        return panel;
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(253, 245, 255));

        String[] cols = {"ID", "Patient", "Consultation", "Medicine", "Room", "Other", "Total", "Status", "Date"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(138, 43, 226));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(221, 160, 221));
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selectedBillId = (int) tableModel.getValueAt(row, 0);
                cmbStatus.setSelectedItem(tableModel.getValueAt(row, 7));
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void calculateTotal() {
        try {
            double c = Double.parseDouble(txtConsultation.getText().trim());
            double m = Double.parseDouble(txtMedicine.getText().trim());
            double r = Double.parseDouble(txtRoom.getText().trim());
            double o = Double.parseDouble(txtOther.getText().trim());
            txtTotal.setText(String.format("%.2f", c + m + r + o));
        } catch (NumberFormatException ignored) {}
    }

    private void generateBill() {
        Patient p = (Patient) cmbPatient.getSelectedItem();
        if (p == null) return;
        try {
            Bill b = new Bill();
            b.setPatientId(p.getId());
            b.setConsultationFee(Double.parseDouble(txtConsultation.getText().trim()));
            b.setMedicineFee(Double.parseDouble(txtMedicine.getText().trim()));
            b.setRoomCharges(Double.parseDouble(txtRoom.getText().trim()));
            b.setOtherCharges(Double.parseDouble(txtOther.getText().trim()));
            b.setTotalAmount(Double.parseDouble(txtTotal.getText().trim()));
            b.setPaymentStatus((String) cmbStatus.getSelectedItem());
            b.setBillDate(txtDate.getText().trim());

            if (billingDAO.generateBill(b)) {
                JOptionPane.showMessageDialog(this, "Bill generated! ✅\nTotal: ₹" + txtTotal.getText());
                clearForm(); loadTable();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Amounts sahi daalo!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void markPaid() {
        if (selectedBillId == -1) {
            JOptionPane.showMessageDialog(this, "Bill select karo!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (billingDAO.updatePaymentStatus(selectedBillId, "Paid")) {
            JOptionPane.showMessageDialog(this, "Payment marked as Paid! ✅");
            loadTable();
        }
    }

    private void clearForm() {
        txtConsultation.setText("500"); txtMedicine.setText("0");
        txtRoom.setText("0"); txtOther.setText("0"); txtTotal.setText("0");
        txtDate.setText(LocalDate.now().toString());
        cmbStatus.setSelectedIndex(0); selectedBillId = -1;
        table.clearSelection();
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Bill b : billingDAO.getAllBills()) {
            tableModel.addRow(new Object[]{b.getId(), b.getPatientName(),
                "₹" + b.getConsultationFee(), "₹" + b.getMedicineFee(),
                "₹" + b.getRoomCharges(), "₹" + b.getOtherCharges(),
                "₹" + b.getTotalAmount(), b.getPaymentStatus(), b.getBillDate()});
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

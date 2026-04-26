package hospital.gui;

import hospital.dao.PatientDAO;
import hospital.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientPanel extends JPanel {

    private PatientDAO dao = new PatientDAO();
    private JTable table;
    private DefaultTableModel tableModel;

    // Form fields
    private JTextField txtName, txtAge, txtPhone, txtAddress;
    private JComboBox<String> cmbGender, cmbBloodGroup;
    private JTextField txtSearch;
    private int selectedPatientId = -1;

    public PatientPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);

        loadTable(dao.getAllPatients());
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
            "Patient Registration", 0, 0,
            new Font("Arial", Font.BOLD, 13), new Color(100, 149, 237)));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtName       = new JTextField(15);
        txtAge        = new JTextField(5);
        txtPhone      = new JTextField(12);
        txtAddress    = new JTextField(20);
        cmbGender     = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        cmbBloodGroup = new JComboBox<>(new String[]{"A+","A-","B+","B-","AB+","AB-","O+","O-"});

        // Row 1
        g.gridx=0; g.gridy=0; panel.add(label("Name:"), g);
        g.gridx=1;             panel.add(txtName, g);
        g.gridx=2;             panel.add(label("Age:"), g);
        g.gridx=3;             panel.add(txtAge, g);
        g.gridx=4;             panel.add(label("Gender:"), g);
        g.gridx=5;             panel.add(cmbGender, g);

        // Row 2
        g.gridx=0; g.gridy=1; panel.add(label("Phone:"), g);
        g.gridx=1;             panel.add(txtPhone, g);
        g.gridx=2;             panel.add(label("Blood Group:"), g);
        g.gridx=3;             panel.add(cmbBloodGroup, g);
        g.gridx=4;             panel.add(label("Address:"), g);
        g.gridx=5;             panel.add(txtAddress, g);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnPanel.setBackground(Color.WHITE);

        JButton btnAdd    = styledButton("➕ Add",    new Color(46, 139, 87));
        JButton btnUpdate = styledButton("✏️ Update", new Color(70, 130, 180));
        JButton btnDelete = styledButton("🗑 Delete", new Color(220, 53, 69));
        JButton btnClear  = styledButton("🔄 Clear",  new Color(108, 117, 125));

        btnAdd.addActionListener(e -> addPatient());
        btnUpdate.addActionListener(e -> updatePatient());
        btnDelete.addActionListener(e -> deletePatient());
        btnClear.addActionListener(e -> clearForm());

        btnPanel.add(btnAdd); btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete); btnPanel.add(btnClear);

        g.gridx=0; g.gridy=2; g.gridwidth=6;
        panel.add(btnPanel, g);

        return panel;
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(245, 248, 255));

        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(245, 248, 255));
        txtSearch = new JTextField(20);
        JButton btnSearch = styledButton("🔍 Search", new Color(100, 149, 237));
        JButton btnAll    = styledButton("📋 Show All", new Color(100, 149, 237));
        btnSearch.addActionListener(e -> loadTable(dao.searchPatients(txtSearch.getText())));
        btnAll.addActionListener(e -> loadTable(dao.getAllPatients()));
        searchPanel.add(new JLabel("Search: ")); searchPanel.add(txtSearch);
        searchPanel.add(btnSearch); searchPanel.add(btnAll);

        // Table
        String[] cols = {"ID", "Name", "Age", "Gender", "Phone", "Address", "Blood Group"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(100, 149, 237));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));

        table.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void addPatient() {
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Patient p = getFormData();
        if (dao.addPatient(p)) {
            JOptionPane.showMessageDialog(this, "Patient added successfully! ✅");
            clearForm();
            loadTable(dao.getAllPatients());
        }
    }

    private void updatePatient() {
        if (selectedPatientId == -1) {
            JOptionPane.showMessageDialog(this, "Pehle table se patient select karo!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Patient p = getFormData();
        p.setId(selectedPatientId);
        if (dao.updatePatient(p)) {
            JOptionPane.showMessageDialog(this, "Patient updated! ✅");
            clearForm();
            loadTable(dao.getAllPatients());
        }
    }

    private void deletePatient() {
        if (selectedPatientId == -1) {
            JOptionPane.showMessageDialog(this, "Pehle patient select karo!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete karna chahte ho?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.deletePatient(selectedPatientId)) {
                JOptionPane.showMessageDialog(this, "Patient deleted! 🗑");
                clearForm();
                loadTable(dao.getAllPatients());
            }
        }
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            selectedPatientId = (int) tableModel.getValueAt(row, 0);
            txtName.setText((String) tableModel.getValueAt(row, 1));
            txtAge.setText(String.valueOf(tableModel.getValueAt(row, 2)));
            cmbGender.setSelectedItem(tableModel.getValueAt(row, 3));
            txtPhone.setText((String) tableModel.getValueAt(row, 4));
            txtAddress.setText((String) tableModel.getValueAt(row, 5));
            cmbBloodGroup.setSelectedItem(tableModel.getValueAt(row, 6));
        }
    }

    private Patient getFormData() {
        Patient p = new Patient();
        p.setName(txtName.getText().trim());
        p.setAge(Integer.parseInt(txtAge.getText().trim().isEmpty() ? "0" : txtAge.getText().trim()));
        p.setGender((String) cmbGender.getSelectedItem());
        p.setPhone(txtPhone.getText().trim());
        p.setAddress(txtAddress.getText().trim());
        p.setBloodGroup((String) cmbBloodGroup.getSelectedItem());
        return p;
    }

    private void clearForm() {
        txtName.setText(""); txtAge.setText(""); txtPhone.setText(""); txtAddress.setText("");
        cmbGender.setSelectedIndex(0); cmbBloodGroup.setSelectedIndex(0);
        selectedPatientId = -1;
        table.clearSelection();
    }

    public void loadTable(List<Patient> list) {
        tableModel.setRowCount(0);
        for (Patient p : list) {
            tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getAge(),
                p.getGender(), p.getPhone(), p.getAddress(), p.getBloodGroup()});
        }
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        return l;
    }

    private JButton styledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        return btn;
    }
}

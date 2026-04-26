package hospital.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private PatientPanel patientPanel;
    private AppointmentPanel appointmentPanel;
    private MedicalRecordPanel medicalRecordPanel;
    private BillingPanel billingPanel;

    public MainFrame() {
        setTitle("🏥 Hospital Management System");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabbedPane(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 84, 123));
        header.setPreferredSize(new Dimension(0, 70));

        JLabel title = new JLabel("  🏥  HOSPITAL MANAGEMENT SYSTEM", JLabel.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Patient Care | Appointments | Records | Billing  ", JLabel.RIGHT);
        subtitle.setFont(new Font("Arial", Font.ITALIC, 13));
        subtitle.setForeground(new Color(173, 216, 230));

        header.add(title, BorderLayout.CENTER);
        header.add(subtitle, BorderLayout.EAST);
        return header;
    }

    private JTabbedPane buildTabbedPane() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 14));
        tabs.setBackground(new Color(240, 248, 255));

        patientPanel       = new PatientPanel();
        appointmentPanel   = new AppointmentPanel();
        medicalRecordPanel = new MedicalRecordPanel();
        billingPanel       = new BillingPanel();

        tabs.addTab("👤 Patients",        patientPanel);
        tabs.addTab("📅 Appointments",     appointmentPanel);
        tabs.addTab("🩺 Medical Records",  medicalRecordPanel);
        tabs.addTab("💰 Billing",          billingPanel);

        // Refresh combos when tab changes
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 1) appointmentPanel.refreshCombos();
        });

        return tabs;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(new Color(25, 84, 123));
        JLabel lbl = new JLabel("© 2024 Hospital Management System  |  Developed in Java Swing + MySQL");
        lbl.setForeground(new Color(173, 216, 230));
        lbl.setFont(new Font("Arial", Font.PLAIN, 11));
        footer.add(lbl);
        return footer;
    }
}

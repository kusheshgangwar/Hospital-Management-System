package hospital.model;

// ─── Appointment ────────────────────────────────────────────────
public class Appointment {
    private int id;
    private int patientId;
    private int doctorId;
    private String patientName;
    private String doctorName;
    private String appointmentDate;
    private String appointmentTime;
    private String status;
    private String notes;

    public int getId()                     { return id; }
    public void setId(int id)              { this.id = id; }
    public int getPatientId()              { return patientId; }
    public void setPatientId(int p)        { this.patientId = p; }
    public int getDoctorId()               { return doctorId; }
    public void setDoctorId(int d)         { this.doctorId = d; }
    public String getPatientName()         { return patientName; }
    public void setPatientName(String n)   { this.patientName = n; }
    public String getDoctorName()          { return doctorName; }
    public void setDoctorName(String n)    { this.doctorName = n; }
    public String getAppointmentDate()     { return appointmentDate; }
    public void setAppointmentDate(String d){ this.appointmentDate = d; }
    public String getAppointmentTime()     { return appointmentTime; }
    public void setAppointmentTime(String t){ this.appointmentTime = t; }
    public String getStatus()              { return status; }
    public void setStatus(String s)        { this.status = s; }
    public String getNotes()               { return notes; }
    public void setNotes(String n)         { this.notes = n; }
}

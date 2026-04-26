package hospital.model;

public class MedicalRecord {
    private int id;
    private int patientId;
    private int doctorId;
    private String patientName;
    private String doctorName;
    private String diagnosis;
    private String prescription;
    private String recordDate;

    public int getId()                    { return id; }
    public void setId(int id)             { this.id = id; }
    public int getPatientId()             { return patientId; }
    public void setPatientId(int p)       { this.patientId = p; }
    public int getDoctorId()              { return doctorId; }
    public void setDoctorId(int d)        { this.doctorId = d; }
    public String getPatientName()        { return patientName; }
    public void setPatientName(String n)  { this.patientName = n; }
    public String getDoctorName()         { return doctorName; }
    public void setDoctorName(String n)   { this.doctorName = n; }
    public String getDiagnosis()          { return diagnosis; }
    public void setDiagnosis(String d)    { this.diagnosis = d; }
    public String getPrescription()       { return prescription; }
    public void setPrescription(String p) { this.prescription = p; }
    public String getRecordDate()         { return recordDate; }
    public void setRecordDate(String d)   { this.recordDate = d; }
}

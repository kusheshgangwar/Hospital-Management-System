package hospital.model;

public class Bill {
    private int id;
    private int patientId;
    private String patientName;
    private double consultationFee;
    private double medicineFee;
    private double roomCharges;
    private double otherCharges;
    private double totalAmount;
    private String paymentStatus;
    private String billDate;

    public int getId()                       { return id; }
    public void setId(int id)                { this.id = id; }
    public int getPatientId()                { return patientId; }
    public void setPatientId(int p)          { this.patientId = p; }
    public String getPatientName()           { return patientName; }
    public void setPatientName(String n)     { this.patientName = n; }
    public double getConsultationFee()       { return consultationFee; }
    public void setConsultationFee(double c) { this.consultationFee = c; }
    public double getMedicineFee()           { return medicineFee; }
    public void setMedicineFee(double m)     { this.medicineFee = m; }
    public double getRoomCharges()           { return roomCharges; }
    public void setRoomCharges(double r)     { this.roomCharges = r; }
    public double getOtherCharges()          { return otherCharges; }
    public void setOtherCharges(double o)    { this.otherCharges = o; }
    public double getTotalAmount()           { return totalAmount; }
    public void setTotalAmount(double t)     { this.totalAmount = t; }
    public String getPaymentStatus()         { return paymentStatus; }
    public void setPaymentStatus(String s)   { this.paymentStatus = s; }
    public String getBillDate()              { return billDate; }
    public void setBillDate(String d)        { this.billDate = d; }
}

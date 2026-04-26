package hospital.model;

public class Doctor {
    private int id;
    private String name;
    private String specialization;
    private String phone;
    private String email;
    private String availableDays;

    public Doctor() {}

    public Doctor(String name, String specialization, String phone, String email, String availableDays) {
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.availableDays = availableDays;
    }

    public int getId()                       { return id; }
    public void setId(int id)                { this.id = id; }
    public String getName()                  { return name; }
    public void setName(String name)         { this.name = name; }
    public String getSpecialization()        { return specialization; }
    public void setSpecialization(String s)  { this.specialization = s; }
    public String getPhone()                 { return phone; }
    public void setPhone(String p)           { this.phone = p; }
    public String getEmail()                 { return email; }
    public void setEmail(String e)           { this.email = e; }
    public String getAvailableDays()         { return availableDays; }
    public void setAvailableDays(String d)   { this.availableDays = d; }

    @Override
    public String toString() { return id + " - " + name + " (" + specialization + ")"; }
}

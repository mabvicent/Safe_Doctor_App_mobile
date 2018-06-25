package example.safe_doctor_app.json_scripts;

import com.google.gson.annotations.SerializedName;

public class Doctors {
    @SerializedName("Doctor_Id")
    public int Doctor_Id;
    @SerializedName("Doctor_Tag")
    public String Doctor_Tag;
    @SerializedName("Image")
    public String Image;
    @SerializedName("Fname")
    public String Fname;
    @SerializedName("Lname")
    public String Lname;
    @SerializedName("Gender")
    public String Gender;
    @SerializedName("Email")
    public String Email;
    @SerializedName("Phone")
    public String Phone;
    @SerializedName("Biography")
    public String Biography;
    @SerializedName("Deadline")
    public String Deadline;

    @SerializedName("Specialisation_Id")
    public String Specialisation_Id;
    @SerializedName("Specialisation")
    public String Specialisation;

    @SerializedName("Hospital_Id")
    public String Hospital_Id;
    @SerializedName("Hospital_Name")
    public String Hospital_Name;

    @SerializedName("District_Name")
    public String District_Name;
    @SerializedName("District_Id")
    public String District_Id;
}

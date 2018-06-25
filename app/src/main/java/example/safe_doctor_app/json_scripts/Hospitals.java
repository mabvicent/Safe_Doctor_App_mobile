package example.safe_doctor_app.json_scripts;

import com.google.gson.annotations.SerializedName;


public class Hospitals {
    @SerializedName("District_Id")
    public String District_Id;
    @SerializedName("District_Name")
    public String District_Name;

    @SerializedName("Hospital_Id")
    public String Hospital_Id;
    @SerializedName("Hospital_Name")
    public String Hospital_Name;
}

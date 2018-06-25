package example.safe_doctor_app.json_scripts;

import com.google.gson.annotations.SerializedName;


public class Drugs {
    @SerializedName("Drug_Id")
    public int Drug_Id;
    @SerializedName("Barcode_Number")
    public String Barcode_Number;
    @SerializedName("Drug_Name")
    public String Drug_Name;
    @SerializedName("Type")
    public String Type;
    @SerializedName("Expiry_Date")
    public String Expiry_Date;
    @SerializedName("Manufacture_Date")
    public String Manufacture_Date;
    @SerializedName("Manufucturer")
    public String Manufucturer;
    @SerializedName("Manufucturer_Country")
    public String Manufucturer_Country;
}

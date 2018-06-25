package example.safe_doctor_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.kosalgeek.android.photoutil.PhotoLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import example.safe_doctor_app.json_scripts.Hospitals;

public class Cases_Page extends AppCompatActivity {
    GlobalActivity globalActivity;
    String IMEI,case_type,selectedPhoto,encodedImage;
    Spinner case_type_spinner;
    TextView error_message;
    LinearLayout doctor_layout,drug_layout;
    FloatingActionButton save_button;
    ImageView camera_button,image_taken;
    EditText doctor_name_txt,doctor_tag_txt,drug_name_txt,drug_code_txt,hospital_txt,location_txt,case_description_txt;
    String doctor_name,doctor_tag,drug_name,drug_code,hospital_name,location,case_description;
    final int CAMERA_REQUEST=1223;
    CameraPhoto cameraPhoto;
    String parent_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases_page);

        //Components
        case_type_spinner=(Spinner)findViewById(R.id.case_type_spinner);
        error_message=(TextView) findViewById(R.id.error_message);
        doctor_layout=(LinearLayout) findViewById(R.id.doctor_layout);
        drug_layout=(LinearLayout)findViewById(R.id.drug_layout);
        save_button=(FloatingActionButton) findViewById(R.id.save_btn);
        doctor_name_txt=(EditText)findViewById(R.id.doctor_name_txt);
        doctor_tag_txt=(EditText)findViewById(R.id.doctor_tag_txt);
        drug_name_txt=(EditText) findViewById(R.id.drug_name_txt);
        drug_code_txt=(EditText) findViewById(R.id.bar_code_txt);
        hospital_txt=(EditText) findViewById(R.id.hospital_txt);
        location_txt=(EditText) findViewById(R.id.location_txt);
        case_description_txt=(EditText) findViewById(R.id.case_description_txt);
        camera_button=(ImageView) findViewById(R.id.camera_btn);
        image_taken=(ImageView) findViewById(R.id.image_taken);

        globalActivity = (GlobalActivity) getApplicationContext();
        //IMEI = globalActivity.getIMEI();
        parent_url=globalActivity.getUrl();
        //Adding camera option
        cameraPhoto=new CameraPhoto(getApplicationContext());
        //On camera button clicking
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
//                    cameraPhoto.addToGallery();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Error in capturing image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        case_type=case_type_spinner.getSelectedItem().toString();
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String case_url=parent_url+"android_connection/index.php?page=case_upload";
                StringRequest caseReportingRequest = new StringRequest(Request.Method.POST, case_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.isEmpty()||response.equals("")) {
                            //Toast.makeText(Cases_Page.this, "Response not received", Toast.LENGTH_SHORT).show();
                            error_message.setText("Case not received on the server");
                            error_message.setTextColor(Color.RED);
                        } else{
                            //Toast.makeText(Cases_Page.this, "Successfully uploaded the case", Toast.LENGTH_SHORT).show();
                            error_message.setText("Successfully uploaded the case,\nYou can submit a new one");
                            error_message.setTextColor(Color.BLUE);
                    }
                        empty_fields();
                        save_button.setVisibility(View.GONE);
                        doctor_layout.setVisibility(View.GONE);
                        drug_layout.setVisibility(View.GONE);
                        error_message.setVisibility(View.VISIBLE);


                        //need to empty the spinner (set to index zero)
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof com.android.volley.TimeoutError) {
                            Toast.makeText(Cases_Page.this, "Request time out", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof com.android.volley.NoConnectionError) {
                            Toast.makeText(Cases_Page.this, "Connection failure", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof com.android.volley.AuthFailureError) {
                            Toast.makeText(Cases_Page.this, "Authentication error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof com.android.volley.NetworkError) {
                            Toast.makeText(Cases_Page.this, "Network error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof com.android.volley.ServerError) {
                            Toast.makeText(Cases_Page.this, "Server error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof com.android.volley.ParseError) {
                            Toast.makeText(Cases_Page.this, "JSON parse error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }){
                    @Override
                    protected HashMap<String, String> getParams() throws AuthFailureError {
                        case_type = case_type_spinner.getSelectedItem().toString();
                        HashMap<String, String> caseHashMap = new HashMap<>();
                        caseHashMap.put("IMEI", IMEI);
                        caseHashMap.put("Case_Type", case_type);
                        if (case_type.equals("Doctors")) {
                            doctor_name = doctor_name_txt.getText().toString();
                            doctor_tag = doctor_tag_txt.getText().toString();
                            caseHashMap.put("doctor_name", doctor_name);
                            caseHashMap.put("doctor_tag", doctor_tag);
                            if (!selectedPhoto.equals("")) {
                                try {
                                    Bitmap bitmap = ImageLoader.init().from(selectedPhoto).requestSize(1024, 1024).getBitmap();
                                    encodedImage = ImageBase64.encode(bitmap);
                                    caseHashMap.put("photo", encodedImage);
                                } catch (FileNotFoundException ex) {
                                    Toast.makeText(getApplicationContext(), "Error while encoding the image: ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else if (case_type.equals("Drugs")) {
                            drug_name = drug_name_txt.getText().toString();
                            drug_code = drug_code_txt.getText().toString();
                            caseHashMap.put("drug_name", drug_name);
                            caseHashMap.put("drug_code", drug_code);
                        }
                        hospital_name = hospital_txt.getText().toString();
                        location = location_txt.getText().toString();
                        case_description = case_description_txt.getText().toString();

                        caseHashMap.put("hospital_name", hospital_name);
                        caseHashMap.put("location", location);
                        caseHashMap.put("description", case_description);
                    return caseHashMap;
                    }
                };
                MySingleton.getInstance(Cases_Page.this).addToRequestQueue(caseReportingRequest);
            }
        });
        case_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                case_type=case_type_spinner.getSelectedItem().toString();
                error_message.setText("Please select the case type first");
                error_message.setTextColor(Color.RED);
                if(case_type.equals("Doctors")){
                    save_button.setVisibility(View.VISIBLE);
                    doctor_layout.setVisibility(View.VISIBLE);
                    drug_layout.setVisibility(View.GONE);
                    error_message.setVisibility(View.GONE);
                }
                else if(case_type.equals("Drugs")){
                    save_button.setVisibility(View.VISIBLE);
                    doctor_layout.setVisibility(View.GONE);
                    drug_layout.setVisibility(View.VISIBLE);
                    error_message.setVisibility(View.GONE);
                }
                else{
                    save_button.setVisibility(View.GONE);
                    doctor_layout.setVisibility(View.GONE);
                    drug_layout.setVisibility(View.GONE);
                    error_message.setVisibility(View.VISIBLE);
                }
                empty_fields();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                empty_fields();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            if (requestCode == CAMERA_REQUEST) {
                if(requestCode == CAMERA_REQUEST ) {
//                    String filePath = cameraPhoto.getPhotoPath();
                    selectedPhoto = cameraPhoto.getPhotoPath();
                }
                try {
                    Bitmap bitmap = PhotoLoader.init().from(selectedPhoto).requestSize(512, 512).getBitmap();
                    image_taken.setImageBitmap(bitmap);
                    //uploadBlog("",selectedPhoto,"","");
                } catch (Exception ex) {
                    Toast.makeText(Cases_Page.this, ""+ex, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void empty_fields(){
        doctor_name_txt.setText("");
        doctor_tag_txt.setText("");
        drug_name_txt.setText("");
        drug_code_txt.setText("");
        hospital_txt.setText("");
        location_txt.setText("");
        case_description_txt.setText("");
        image_taken.setImageResource(R.drawable.ic_launcher);
        selectedPhoto="";
    }
}

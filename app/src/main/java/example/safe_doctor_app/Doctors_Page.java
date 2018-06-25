package example.safe_doctor_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import example.safe_doctor_app.json_scripts.Districts;
import example.safe_doctor_app.json_scripts.Doctors;
import example.safe_doctor_app.json_scripts.Hospitals;

public class Doctors_Page extends AppCompatActivity implements Response.Listener<String>  {
String TAG;
    String em;
    GlobalActivity globalActivity;
    String parent_url;
    ListView doctor_list_view;
    Spinner DistrictSpinner,HospitalSpinner;
    Button search_button;
    String district_id,hospital_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        doctor_list_view=(ListView)findViewById(R.id.doctor_list);
        HospitalSpinner=(Spinner)findViewById(R.id.hospital_spinner);
        DistrictSpinner=(Spinner)findViewById(R.id.district_spinner);
        search_button=(Button)findViewById(R.id.search_doctor_button);
        globalActivity=(GlobalActivity)getApplicationContext();
        parent_url=globalActivity.getUrl();
        search_button.setVisibility(View.INVISIBLE);

        //connecting externally
        String district_url=parent_url+"android_connection/index.php?page=districts_view";
        String hospital_url=parent_url+"android_connection/index.php?page=hospitals_view";
        try {
            searchDoctors("", "");//Call a function to return all doctors registered
            StringRequest districtStringRequest = new StringRequest(Request.Method.POST, district_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    final ArrayList<Districts> jsonArray = new JsonConverter<Districts>().toArrayList(response, Districts.class);
                    BindDictionary<Districts> dictionary = new BindDictionary<>();
                    dictionary.addStringField(R.id.spinner_view, new StringExtractor<Districts>() {
                        @Override
                        public String getStringValue(Districts districts_data, int position) {
                            return districts_data.District_Name;
                        }
                    });
                    FunDapter<Districts> districtAdapter = new FunDapter<>(getApplicationContext(), jsonArray, R.layout.spinner_view_layout, dictionary);
                    DistrictSpinner.setAdapter(districtAdapter);
                    DistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            final Districts districtData = jsonArray.get(i);
                            district_id=districtData.District_Id;
                            searchDoctors(district_id, hospital_id);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof com.android.volley.TimeoutError) {
                        Toast.makeText(Doctors_Page.this, "Request time out", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof com.android.volley.NoConnectionError) {
                        Toast.makeText(Doctors_Page.this, "Connection failure", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof com.android.volley.AuthFailureError) {
                        Toast.makeText(Doctors_Page.this, "Authentication error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof com.android.volley.NetworkError) {
                        Toast.makeText(Doctors_Page.this, "Network error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof com.android.volley.ServerError) {
                        Toast.makeText(Doctors_Page.this, "Server error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof com.android.volley.ParseError) {
                        Toast.makeText(Doctors_Page.this, "JSON parse error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            StringRequest hospitalStringRequest = new StringRequest(Request.Method.POST, hospital_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    final ArrayList<Hospitals> jsonArray = new JsonConverter<Hospitals>().toArrayList(response, Hospitals.class);
                    BindDictionary<Hospitals> dictionary = new BindDictionary<>();
                    dictionary.addStringField(R.id.spinner_view, new StringExtractor<Hospitals>() {
                        @Override
                        public String getStringValue(Hospitals hospital_data, int position) {
                            return hospital_data.Hospital_Name;
                        }
                    });
                    FunDapter<Hospitals> hospitalsAdapter = new FunDapter<>(getApplicationContext(), jsonArray, R.layout.spinner_view_layout, dictionary);
                    HospitalSpinner.setAdapter(hospitalsAdapter);
                    HospitalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            final Hospitals hospitals = jsonArray.get(i);
                            hospital_id=hospitals.Hospital_Id;
                            searchDoctors(district_id,hospital_id);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof com.android.volley.TimeoutError) {
                        Toast.makeText(Doctors_Page.this, "Request time out", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof com.android.volley.NoConnectionError) {
                        Toast.makeText(Doctors_Page.this, "Connection failure", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof com.android.volley.AuthFailureError) {
                        Toast.makeText(Doctors_Page.this, "Authentication error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof com.android.volley.NetworkError) {
                        Toast.makeText(Doctors_Page.this, "Network error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof com.android.volley.ServerError) {
                        Toast.makeText(Doctors_Page.this, "Server error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof com.android.volley.ParseError) {
                        Toast.makeText(Doctors_Page.this, "JSON parse error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            MySingleton.getInstance(this).addToRequestQueue(districtStringRequest);
            MySingleton.getInstance(this).addToRequestQueue(hospitalStringRequest);
        }catch(Exception ex){
            Toast.makeText(Doctors_Page.this, "Error in connecting to external server", Toast.LENGTH_SHORT).show();
        }
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //searchDoctors(district_id,hospital_id);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onResponse(String response) {
        try {
            if (response.contains("no results")||response.equals("")) {
                //doctor_list_view.clearChoices();
                doctor_list_view.setVisibility(View.INVISIBLE);
            Toast.makeText(Doctors_Page.this, "No search results found", Toast.LENGTH_SHORT).show();
        } else {
                doctor_list_view.setVisibility(View.VISIBLE);
            final ArrayList<Doctors> jsonArray = new JsonConverter<Doctors>().toArrayList(response, Doctors.class);
            BindDictionary<Doctors> dictionary = new BindDictionary<>();
            dictionary.addStringField(R.id.names, new StringExtractor<Doctors>() {
                @Override
                public String getStringValue(Doctors doctor_data, int position) {
                    return "Names: " + doctor_data.Fname.toUpperCase();
                }
            });
                dictionary.addStringField(R.id.vacancy, new StringExtractor<Doctors>() {
                    @Override
                    public String getStringValue(Doctors doctor_data, int position) {
                        return "Vacancies: " + doctor_data.Lname;
                    }
                });
            dictionary.addStringField(R.id.email, new StringExtractor<Doctors>() {
                @Override
                public String getStringValue(Doctors doctor_data, int position) {
                    return "Application Email:\n" + doctor_data.Email;
                }

            });
            dictionary.addStringField(R.id.phone, new StringExtractor<Doctors>() {
                @Override
                public String getStringValue(Doctors doctor_data, int position) {
                    return "Call: " + doctor_data.Phone;
                }
            });
                dictionary.addStringField(R.id.schedule, new StringExtractor<Doctors>() {
                    @Override
                    public String getStringValue(Doctors doctor_data, int position) {
                        return "Deadline: " + doctor_data.Deadline;
                    }
                });
                dictionary.addStringField(R.id.specialisation, new StringExtractor<Doctors>() {
                    @Override
                    public String getStringValue(Doctors doctor_data, int position) {
                        return "Department: " + doctor_data.Specialisation;
                    }
                });
            dictionary.addStringField(R.id.hospital, new StringExtractor<Doctors>() {
                @Override
                public String getStringValue(Doctors doctor_data, int position) {
                    return "Company: " + doctor_data.Hospital_Name;
                }
            });
            dictionary.addStringField(R.id.district, new StringExtractor<Doctors>() {
                @Override
                public String getStringValue(Doctors doctor_data, int position) {
                    return "District: " + doctor_data.District_Name;
                }
            });
            dictionary.addStringField(R.id.biography, new StringExtractor<Doctors>() {
                @Override
                public String getStringValue(Doctors doctor_data, int position) {
                    return "JOB DETAILS:\n " + doctor_data.Biography;
                }
            });

            dictionary.addDynamicImageField(R.id.photo, new StringExtractor<Doctors>() {
                @Override
                public String getStringValue(Doctors doctor_data, int position) {
                    return doctor_data.Image;
                }
            }, new DynamicImageLoader() {
                @Override
                public void loadImage(String url, ImageView imageView) {
                    Picasso.with(getApplicationContext()).load(parent_url + "images/doctor/" + url).into(imageView);
//                imageView.setPadding(0,0,0,0);
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setPadding(0, 0, 0, 10);
                    imageView.setAdjustViewBounds(true);
                }
            });
            FunDapter<Doctors> funDapter = new FunDapter<>(getApplicationContext(), jsonArray, R.layout.doctors_list_layout, dictionary);
            doctor_list_view.setAdapter(funDapter);
        }
            }catch(Exception ex){
            doctor_list_view.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "No results found for this search", Toast.LENGTH_SHORT).show();
            }
            doctor_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String email = String.valueOf(parent.getItemAtPosition(position));
                    sendEmail(email);
                }
            });
    }

    private void sendEmail(String mail) {

            Log.d(TAG, "sendEmail: ");

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","hrmapplication@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Application");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }



    public void searchDoctors(final String district_id, final String hospital_id){
        String doctor_url=parent_url+"android_connection/index.php?page=doctors_view";
        try{
            HashMap districtHashMap=new HashMap();
            if(!district_id.equals("")){
                districtHashMap.put("districtKey",""+district_id);
            }
            if(!hospital_id.equals("")){
                districtHashMap.put("hospitalKey",""+hospital_id);
            }
            StringRequest doctorStringRequest=new StringRequest(Request.Method.POST,doctor_url,this, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof com.android.volley.TimeoutError) {
                        Toast.makeText(Doctors_Page.this, "Request time out", Toast.LENGTH_SHORT).show();
                    }else if (error instanceof com.android.volley.NoConnectionError) {
                        Toast.makeText(Doctors_Page.this, "Connection failure", Toast.LENGTH_SHORT).show();
                    }else if (error instanceof com.android.volley.AuthFailureError) {
                        Toast.makeText(Doctors_Page.this, "Authentication error", Toast.LENGTH_SHORT).show();
                    }else if (error instanceof com.android.volley.NetworkError) {
                        Toast.makeText(Doctors_Page.this, "Network error", Toast.LENGTH_SHORT).show();
                    }else if (error instanceof com.android.volley.ServerError) {
                        Toast.makeText(Doctors_Page.this, "Server error", Toast.LENGTH_SHORT).show();
                    }else if (error instanceof com.android.volley.ParseError) {
                        Toast.makeText(Doctors_Page.this, "JSON parse error", Toast.LENGTH_SHORT).show();
                    }
                }
            }){
                @Override
                protected HashMap<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> doctorHashMap = new HashMap<>();
                    doctorHashMap.put("district_id", district_id);
                    doctorHashMap.put("hospital_id", hospital_id);
                    return doctorHashMap;
                }
            };
            MySingleton.getInstance(this).addToRequestQueue(doctorStringRequest);
        }catch(Exception ex){
            //Toast.makeText(getApplicationContext(),"An error happened while connecting to external server",Toast.LENGTH_SHORT).show();
        }
    }
}

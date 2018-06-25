package example.safe_doctor_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;

import example.safe_doctor_app.json_scripts.Drugs;

public class Drugs_Page extends AppCompatActivity implements Response.Listener<String>  {
    EditText drugSearchView;
    ListView drugs_list_view;
    TextView heading;
    String barcode="";
    ImageView imageView;
    String parent_url;
    GlobalActivity globalActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drugs_page);
        drugSearchView=(EditText)findViewById(R.id.drug_search_view);
        drugs_list_view=(ListView)findViewById(R.id.drugs_list_view);
        heading=(TextView)findViewById(R.id.heading);
        imageView=(ImageView)findViewById(R.id.imageView2);
        imageView.setVisibility(View.INVISIBLE);
        drugs_list_view.setVisibility(View.VISIBLE);

        globalActivity=(GlobalActivity)getApplicationContext();
        parent_url=globalActivity.getUrl();
        searchDrugs("");

        drugSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    barcode = drugSearchView.getText().toString().toUpperCase();
                        //Call the function and send the barcode
                        searchDrugs(barcode);
                }
                catch (Exception ex){
                    Toast.makeText(Drugs_Page.this, ""+ex, Toast.LENGTH_SHORT).show();
                    heading.setTextAppearance(Drugs_Page.this,android.R.style.TextAppearance_Small);
                    heading.setText("No Search results");
                    imageView.setVisibility(View.VISIBLE);
                    drugs_list_view.setVisibility(View.INVISIBLE);
                }
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void searchDrugs(final String barcode_got){
        String drug_url=parent_url+"android_connection/index.php?page=drugs_view";
        try{
            StringRequest drugsStringRequest=new StringRequest(Request.Method.POST,drug_url,this, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof com.android.volley.TimeoutError) {
                        Toast.makeText(Drugs_Page.this, "Request time out", Toast.LENGTH_SHORT).show();
                    }else if (error instanceof com.android.volley.NoConnectionError) {
                        Toast.makeText(Drugs_Page.this, "Connection failure", Toast.LENGTH_SHORT).show();
                    }else if (error instanceof com.android.volley.AuthFailureError) {
                        Toast.makeText(Drugs_Page.this, "Authentication error", Toast.LENGTH_SHORT).show();
                    }else if (error instanceof com.android.volley.NetworkError) {
                        Toast.makeText(Drugs_Page.this, "Network error", Toast.LENGTH_SHORT).show();
                    }else if (error instanceof com.android.volley.ServerError) {
                        Toast.makeText(Drugs_Page.this, "Server error", Toast.LENGTH_SHORT).show();
                    }else if (error instanceof com.android.volley.ParseError) {
                        Toast.makeText(Drugs_Page.this, "JSON parse error", Toast.LENGTH_SHORT).show();
                    }
                }
            }){
                @Override
                protected HashMap<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> drugsHashMap = new HashMap<>();
                    drugsHashMap.put("barcode", barcode_got);
                    return drugsHashMap;
                }
            };
            MySingleton.getInstance(this).addToRequestQueue(drugsStringRequest);
        }catch(Exception ex){
            Toast.makeText(getApplicationContext(),"An error happened while connecting to external server\n"+ex,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(String response) {
        try {
            if(response.equals("")||response.contains("No results")) {
                heading.setTextAppearance(Drugs_Page.this,android.R.style.TextAppearance_Small);
                heading.setText("No Search results");
                imageView.setVisibility(View.VISIBLE);
                drugs_list_view.setVisibility(View.INVISIBLE);
            }
            else{
                heading.setTextAppearance(Drugs_Page.this,android.R.style.TextAppearance_Small);
                heading.setText("Search results");
                imageView.setVisibility(View.INVISIBLE);
                drugs_list_view.setVisibility(View.VISIBLE);
                final ArrayList<Drugs> jsonArray = new JsonConverter<Drugs>().toArrayList(response, Drugs.class);
                BindDictionary<Drugs> dictionary = new BindDictionary<>();
                dictionary.addStringField(R.id.drug_name, new StringExtractor<Drugs>() {
                    @Override
                    public String getStringValue(Drugs drug_data, int position) {
                        return "Name: " + drug_data.Drug_Name.toUpperCase() + " (" + drug_data.Type + ")";
                    }
                });
                dictionary.addStringField(R.id.bar_code, new StringExtractor<Drugs>() {
                    @Override
                    public String getStringValue(Drugs drug_data, int position) {
                        return "Barcode: " + drug_data.Barcode_Number;
                    }
                });
                dictionary.addStringField(R.id.expiry_date, new StringExtractor<Drugs>() {
                    @Override
                    public String getStringValue(Drugs drug_data, int position) {
                        return "Expiry: " + drug_data.Expiry_Date;
                    }
                });
                dictionary.addStringField(R.id.country, new StringExtractor<Drugs>() {
                    @Override
                    public String getStringValue(Drugs drug_data, int position) {
                        return "Country: " + drug_data.Manufucturer_Country;
                    }
                });
                dictionary.addStringField(R.id.manufacturer, new StringExtractor<Drugs>() {
                    @Override
                    public String getStringValue(Drugs drug_data, int position) {
                        return "Manufacturer: " + drug_data.Manufucturer;
                    }
                });
                dictionary.addStringField(R.id.manufacture_date, new StringExtractor<Drugs>() {
                    @Override
                    public String getStringValue(Drugs drug_data, int position) {
                        return "Manufacture Date: " + drug_data.Manufacture_Date;
                    }
                });
                FunDapter<Drugs> funDapter = new FunDapter<>(getApplicationContext(), jsonArray, R.layout.drugs_list_layout, dictionary);
                drugs_list_view.setAdapter(funDapter);
            }
        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), "An error occured while connecting to the server", Toast.LENGTH_SHORT).show();
        }
    }

    public void clear(View view) {
        drugSearchView.setText("");
    }
}

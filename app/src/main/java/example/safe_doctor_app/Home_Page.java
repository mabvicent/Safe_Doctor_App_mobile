package example.safe_doctor_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Home_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    public void openDoctorsActivity(View view) {
        startActivity(new Intent(this,Doctors_Page.class));
    }

    public void openDrugsActivity(View view) {
        startActivity(new Intent(this,Drugs_Page.class));
    }

    public void openCasesActivity(View view) {
        startActivity(new Intent(this,Cases_Page.class));
    }
}

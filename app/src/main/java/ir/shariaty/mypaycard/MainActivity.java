package ir.shariaty.mypaycard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView imgphone;
    ImageView imgGhabz;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            imgphone=findViewById(R.id.img1);
            imgGhabz=findViewById(R.id.img2);


            imgphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentmain=new Intent(MainActivity.this,CellPhoneActivity.class);
                    startActivity(intentmain);

                }
            });

            imgGhabz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,GhabzActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
    }

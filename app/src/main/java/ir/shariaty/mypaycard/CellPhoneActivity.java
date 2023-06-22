package ir.shariaty.mypaycard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ir.shariaty.mypaycard.databinding.ActivityCellPhoneBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CellPhoneActivity extends AppCompatActivity {

    public static final MediaType JSON
            = MediaType.get("application/json;charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    ActivityCellPhoneBinding binding;


    Integer operator;
    Integer amount;
    String mobile;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCellPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = binding.mobile.getText().toString();
                //String Charge=binding.mobile.getText().toString();
                if(binding.rdcell1.isChecked())
                    operator = 1;
                else if (binding.rdcell2.isChecked())
                    operator = 2;
                else if (binding.rdcell3.isChecked())
                    operator = 3;

                if (binding.rd5000.isChecked())
                    amount = 50000;
                else if(binding.rd10000.isChecked())
                    amount = 100000;
                else if(binding.rd15000.isChecked())
                    amount = 150000;
                else if(binding.rd20000.isChecked())
                    amount = 200000;

                callAPI(mobile,operator,amount);
            }
        });
    }

    private void callAPI(String mobile, Integer operator, Integer amount) {
        JSONObject object = new JSONObject();
        try {
            object.put("MobileNo", "09190982697");
            object.put("OperatorType",1);
            object.put("AmountPure",20000);
            object.put("mid","0");
        } catch (Exception e) {

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        RequestBody requestBody = RequestBody.create(object.toString(), JSON);
        Request request = new Request.Builder().url("https://topup.pec.ir/")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());

                    url = jsonObject.getString("url");
                    load(url);
                    

                } catch (Exception e) {
                    String error = e.getMessage();

                }
            }
        });
    }

    private void load(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}

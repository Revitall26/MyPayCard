package ir.shariaty.mypaycard;

//import static java.lang.System.load;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import ir.shariaty.mypaycard.databinding.ActivityGhabzBinding;
import ir.shariaty.mypaycard.databinding.ActivityMainBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class GhabzActivity extends AppCompatActivity {

    public static final MediaType JSON
            = MediaType.get("application/json;charset=utf-8");

    OkHttpClient client = new OkHttpClient();


    String EndPaytId;
    String MidPaytId;
    Integer EndAmount;
    Integer MidAmount;


    ActivityGhabzBinding binding;
    ActivityMainBinding binding1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGhabzBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnstelam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String LandlineNo = binding.sabet.getText().toString();
                callAPI(LandlineNo);
            }
        });
    }
        private void callAPI(String landlineNo) {
            progress(true);
            JSONObject object = new JSONObject();

            try {
                object.put("FixedLineNumber", landlineNo);
            } catch (Exception e) {

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            RequestBody requestBody = RequestBody.create(object.toString(), JSON);
            Request request = new Request.Builder().url("https://charge.sep.ir/Inquiry/FixedLineBillInquiry")
                   // .header("Authorization",)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    call.cancel();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(GhabzActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_LONG).show();
                        }
                    });
                    //Toast.makeText(GhabzActivity.this,"please try again",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        EndAmount=jsonObject.getJSONObject("data").getJSONObject("FinalTerm").getInt("Amount");
                        MidAmount=jsonObject.getJSONObject("data").getJSONObject("MidTerm").getInt("Amount");
                        EndPaytId=jsonObject.getJSONObject("data").getJSONObject("FinalTerm").getString("PaymentID");
                        MidPaytId=jsonObject.getJSONObject("data").getJSONObject("MidTerm").getString("PaymentID");

                        load(EndAmount,MidAmount,EndPaytId,MidPaytId);
                        progress(false);

                    } catch (Exception e) {
                        String error = e.getMessage();

                    }
                }
            });
        }

    private void progress(boolean Progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Progress){
                   binding.lblEndAmount.setVisibility(View.VISIBLE);
                    binding.lblMidAmount.setVisibility(View.VISIBLE);
                    binding.lblEndPaytId.setVisibility(View.VISIBLE);
                    binding.lblMidPaytId.setVisibility(View.VISIBLE);
                    binding.lblaMidTerm.setVisibility(View.VISIBLE);
                    binding.lblEndTerm.setVisibility(View.VISIBLE);
                   // binding.lblPaymenTerm.setVisibility(View.VISIBLE);
                    binding.lblPaymentMid.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void load(Integer endTermAmount, Integer midTermAmount, String endTermPaymentId, String midTermPaymentId) {
            binding.lblEndAmount.setText(endTermAmount+" ریال");
            binding.lblMidAmount.setText(midTermAmount+" ریال");
            binding.lblEndPaytId.setText(endTermPaymentId);
            binding.lblMidPaytId.setText(midTermPaymentId);

        }

    }

package choihouk.houkbank;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class transfer extends AppCompatActivity {

    String HttpUrl2 = "http://13.124.186.173/transfer.php";
    String HttpUrl = "http://13.124.186.173/getUserInfo.php";

    String name, account, account_type, userName, receive_user, transfer_amount;
    TextView cust_info, test_view;
    EditText transfer;
    int balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        cust_info = (TextView) findViewById(R.id.cust_info);
        transfer = (EditText) findViewById(R.id.transfer_amount);
        test_view = (TextView) findViewById(R.id.test_view);


        userName = "최호욱";
        receive_user = "박승철";
        receive_user_info();

        //테스트 클릭 에딧텍스트 값 나오는지 안나오는지
        Button test_btn = (Button) findViewById(R.id.test_btn);
        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transfer_amount = transfer.getText().toString();
                test_view.setText(transfer_amount);
            }
        });

        //취소 클릭시 메인으로 돌아가기
        Button cancle = (Button) findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(transfer.this, MainActivity.class);
                i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        //확인 클릭시 송금
        Button confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transfer_amount = transfer.getText().toString();
                transfer();
                Intent i = new Intent(transfer.this, MainActivity.class);
                i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });




    }

    //송금 메서드
    public void transfer() {
        RequestQueue request = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), "송금이 성공적으로 완료되었습니다!", Toast.LENGTH_LONG).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("trans_name", userName);
                parameters.put("receive_name", receive_user);
                parameters.put("balance", transfer_amount);
                return parameters;
            }
        };
        request.add(stringRequest);
    }

    //데이터 가져오기 메서드
    public void receive_user_info() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject data = arr.getJSONObject(i);
                        name = data.getString("Name");
                        account = data.getString("Account");
                        balance = data.getInt("Balance");
                        account_type = data.getString("account_type");
                    }

                    String switch_balance = String.valueOf(balance);

                    //가져온 데이터 셋팅
                    cust_info.setText(name+getResources().getString(R.string.ofCust)+"("+account+"): "+switch_balance+getResources().getString(R.string.one));




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("name", userName);
                return parameters;
            }
        };
        queue.add(stringRequest);
    }

}

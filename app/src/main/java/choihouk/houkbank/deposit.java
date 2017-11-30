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

public class deposit extends AppCompatActivity {

    String HttpUrl2 = "http://13.124.186.173/deposit.php";
    String HttpUrl = "http://13.124.186.173/getUserInfo.php";

    String name, account, account_type, userName, deposit_amount;
    TextView cust_info;
    EditText deposit;

    int balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        cust_info = (TextView) findViewById(R.id.cust_info);
        deposit = (EditText) findViewById(R.id.deposit_amount);

        userName = SharedPref.getInstance(getApplicationContext()).getUsername();
        receive_user_info();

        //취소 클릭시 메인으로 돌아가기
        Button cancle = (Button) findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(deposit.this, MainActivity.class);
                i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        //확인 클릭시 입금
        Button confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deposit_amount = deposit.getText().toString();
                deposit();
                Intent i = new Intent(deposit.this, MainActivity.class);
                i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });



    }

    //입금 메서드
    public void deposit() {
        RequestQueue request = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), "입금이 성공적으로 완료되었습니다!", Toast.LENGTH_LONG).show();


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
                parameters.put("balance", deposit_amount);
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

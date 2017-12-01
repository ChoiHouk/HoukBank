package choihouk.houkbank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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


public class check_account extends AppCompatActivity {

    ListView listview ;
    ListViewAdapter adapter;

    String HttpUrl2 = "http://13.124.186.173/getTransactionInfo.php";
    String HttpUrl = "http://13.124.186.173/getUserInfo.php";

    String t_name, name, date, type, userName, account, account_type;
    TextView account_number,cust_balance,check_title = null;
    int balance, amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        adapter = new ListViewAdapter();
        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);

        receive_transaction_info();

        check_title = (TextView) findViewById(R.id.check_title);
        account_number = (TextView) findViewById(R.id.account_number);
        cust_balance = (TextView) findViewById(R.id.cust_balance);

        userName = SharedPref.getInstance(getApplicationContext()).getUsername();
        receive_user_info();

        //취소 클릭시 메인으로 돌아가기
        Button cancle = (Button) findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(check_account.this, MainActivity.class);
                i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

    }

    //데이터 가져오기 메서드
    public void receive_user_info() {
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
                    check_title.setText(name+getResources().getString(R.string.ofCust));
                    account_number.setText(account);
                    cust_balance.setText(switch_balance+getResources().getString(R.string.one));


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
        RequestQueue queue2 = Volley.newRequestQueue(this);
        queue2.add(stringRequest);
    }

    //거래내역 가져오기 메서드
    public void receive_transaction_info() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "거래내역 조회 성공!", Toast.LENGTH_LONG).show();
                try {
                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject data = arr.getJSONObject(i);
                        t_name = data.getString("name");
                        date = data.getString("date");
                        type = data.getString("type");
                        amount = data.getInt("amount");

                        adapter.addItem(t_name, type,amount, date);
                    }

                    listview.setAdapter(adapter);


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

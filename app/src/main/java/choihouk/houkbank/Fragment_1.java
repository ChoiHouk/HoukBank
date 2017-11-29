package choihouk.houkbank;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class Fragment_1 extends Fragment {

    String HttpUrl = "http://13.124.186.173/getUserInfo.php";
    ViewGroup rootView;

    TextView cust_name, ac_type, account_number,cust_balance, welcome = null;
    ImageView profile;

    String name, account, account_type, userName;
    int balance;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userName = "최호욱";
        receive_user_info();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_1, container, false);


        welcome = (TextView) rootView.findViewById(R.id.welcome);
        cust_name = (TextView) rootView.findViewById(R.id.cust_name);
        ac_type = (TextView) rootView.findViewById(R.id.account_type);
        account_number = (TextView) rootView.findViewById(R.id.account_number);
        cust_balance = (TextView) rootView.findViewById(R.id.balance);
        profile = (ImageView) rootView.findViewById(R.id.profile);


        //외부 포털 사이트 이동 기능
        ImageButton adButton = (ImageButton) rootView.findViewById(R.id.ad_btn);
        adButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://kmong.com/")));
            }
        });


        return rootView;
    }

    public void receive_user_info() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                        cust_name.setText(name+getResources().getString(R.string.ofCust));
                        ac_type.setText(account_type);
                        account_number.setText(account);
                        cust_balance.setText(switch_balance+getResources().getString(R.string.one));
                        welcome.setText(name+getResources().getString(R.string.welcome_cust));

                        profile.setImageResource(R.drawable.houk);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
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

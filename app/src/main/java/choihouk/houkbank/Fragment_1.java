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
    String userName;
    ViewGroup rootView;
    RequestQueue requestQueue;

    TextView cust_name, ac_type, account_number;

    String name, account, account_type;
    int balance;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //로그인한사람 정보가져오기
        receive_user_info();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_1, container, false);

        cust_name = (TextView) rootView.findViewById(R.id.cust_name);
        ac_type = (TextView) rootView.findViewById(R.id.account_type);
        account_number = (TextView) rootView.findViewById(R.id.account_number);


        userName = "최호욱";
        //php서버를 통해 db로 데이터를 요청 안드로이드는 보안이슈로 디비와 직접적인 통신을 할 수 없기때문에 php를 거친다.



        //가져온 데이터 셋팅
        cust_name.setText(name);
        ac_type.setText(account_type);
        account_number.setText(account);




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
                    JSONArray arr  = new JSONArray(response);
                    for(int i =0; i<arr.length(); i++){
                        JSONObject data =  arr.getJSONObject(i);
                        name = data.getString("name");
                        account = data.getString("account");
                        balance = data.getInt("balance");
                        account_type = data.getString("account_type");
                    }

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

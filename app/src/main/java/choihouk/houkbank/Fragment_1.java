package choihouk.houkbank;

import android.app.ProgressDialog;
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

    ViewGroup rootView;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    String name, account, account_type;
    int balnace;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_1, container, false);

        //php서버를 통해 db로 데이터를 요청 안드로이드는 보안이슈로 디비와 직접적인 통신을 할 수 없기때문에 php를 거친다.
        progressDialog.setMessage("로딩중.. 좀만 기둘려주떼염");
        progressDialog.show();

        requestQueue = Volley.newRequestQueue(getActivity().getApplication());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if(!response.equals("0 results")){
                    Toast.makeText(getActivity().getApplication(),"데이터 가져오기 성공",Toast.LENGTH_LONG).show();

                    try {
                        JSONArray arr = new JSONArray(response);
                        for(int i =0; i<arr.length(); i++){
                            JSONObject obj =  arr.getJSONObject(i);
                            name=obj.getString("name");
                            account=obj.getString("account");
                            account_type = obj.getString("account_type");
                            balnace = obj.getInt("balance");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplication(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", "최호욱");

                return params;
            }
        };
        requestQueue.add(stringRequest);

        //가져온 데이터 셋팅
        TextView cust_name = (TextView)rootView.findViewById(R.id.cust_name);
        cust_name.setText(name);
        TextView ac_type = (TextView)rootView.findViewById(R.id.account_type);
        ac_type.setText(account_type);
        TextView account_number = (TextView)rootView.findViewById(R.id.account_number);
        account_number.setText(account);
        TextView cust_balance = (TextView)rootView.findViewById(R.id.balance);
        cust_balance.setText(balnace);



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

}

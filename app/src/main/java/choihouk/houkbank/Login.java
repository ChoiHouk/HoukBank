package choihouk.houkbank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class Login extends AppCompatActivity {

    String HttpUrl = "http://13.124.186.173/getUserInfo.php";

    EditText show_name;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        show_name = (EditText) findViewById(R.id.user_name);


        //로그인버튼 클릭시 이벤트
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receive_user_info();
            }
        });

    }
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
                    }

                    if(show_name.getText().toString().equals(name)){
                        Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_LONG).show();
                        final String user_name = show_name.getText().toString();
                        SharedPref.getInstance(getApplicationContext()).setUsername(user_name);
                        Intent i = new Intent(Login.this, MainActivity.class);
                        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "사용자 이름이 유효하지 않습니다!", Toast.LENGTH_LONG).show();
                    }





                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "사용자 이름이 유효하지 않습니다!", Toast.LENGTH_LONG).show();
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
                parameters.put("name", show_name.getText().toString());
                return parameters;
            }
        };
        queue.add(stringRequest);
    }


}

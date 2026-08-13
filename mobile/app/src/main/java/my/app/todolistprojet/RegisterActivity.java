package my.app.todolistprojet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText editUsername,
            editEmail,
            editPassword;

    Button btnRegister;

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        editUsername =
                findViewById(R.id.editUsername);

        editEmail =
                findViewById(R.id.editEmail);

        editPassword =
                findViewById(R.id.editPassword);

        btnRegister =
                findViewById(R.id.btnRegister);

        apiService = RetrofitClient
                .getClient()
                .create(ApiService.class);

        btnRegister.setOnClickListener(v -> {

            apiService.registerUser(

                    editUsername.getText().toString(),

                    editEmail.getText().toString(),

                    editPassword.getText().toString()

            ).enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(
                        Call<ResponseBody> call,
                        Response<ResponseBody> response) {

                    Toast.makeText(
                            RegisterActivity.this,
                            "Compte créé",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                }

                @Override
                public void onFailure(
                        Call<ResponseBody> call,
                        Throwable t) {

                }
            });
        });
    }
}
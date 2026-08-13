package my.app.todolistprojet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editPassword;

    Button btnLogin;

    TextView textRegister;

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);

        editPassword = findViewById(R.id.editPassword);

        btnLogin = findViewById(R.id.btnLogin);

        textRegister = findViewById(R.id.textRegister);

        apiService = RetrofitClient
                .getClient()
                .create(ApiService.class);

        // LOGIN

        btnLogin.setOnClickListener(v -> {

            try {

                String email =
                        editEmail.getText()
                                .toString()
                                .trim();

                String password =
                        editPassword.getText()
                                .toString()
                                .trim();

                // Vérification champs vides

                if(email.isEmpty() || password.isEmpty()){

                    Toast.makeText(
                            LoginActivity.this,
                            "Tous les champs sont obligatoires",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                // Vérification email

                if(!email.contains("@")){

                    Toast.makeText(
                            LoginActivity.this,
                            "Email invalide",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                // Vérification password

                if(password.length() < 6){

                    Toast.makeText(
                            LoginActivity.this,
                            "Mot de passe faible",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                // API LOGIN

                apiService.loginUser(email, password)

                        .enqueue(new Callback<ResponseBody>() {

                            @Override
                            public void onResponse(
                                    Call<ResponseBody> call,
                                    Response<ResponseBody> response) {

                                Toast.makeText(
                                        LoginActivity.this,
                                        "Connexion réussie",
                                        Toast.LENGTH_SHORT
                                ).show();

                                startActivity(
                                        new Intent(
                                                LoginActivity.this,
                                                MainActivity.class
                                        )
                                );

                                finish();
                            }

                            @Override
                            public void onFailure(
                                    Call<ResponseBody> call,
                                    Throwable t) {

                                Toast.makeText(
                                        LoginActivity.this,
                                        "Erreur : " + t.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        });

            } catch (Exception e){

                Toast.makeText(
                        LoginActivity.this,
                        "Exception : " + e.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        // PAGE REGISTER

        textRegister.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            LoginActivity.this,
                            RegisterActivity.class
                    )
            );
        });
    }
}
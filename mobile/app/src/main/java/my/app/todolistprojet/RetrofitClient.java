package my.app.todolistprojet;

import retrofit2.Retrofit;
// Convertisseur JSON → Objet Java avec Gson
import retrofit2.converter.gson.GsonConverterFactory;
// Cette classe permet de configurer Retrofit
// afin de communiquer avec l'API MySQL
public class RetrofitClient {

    // Adresse du serveur local XAMPP
    // 10.0.2.2 représente localhost
    // depuis l'émulateur Android
    private static final String BASE_URL =
            "http://10.0.2.2/todolistapi/";
    // Objet Retrofit unique
    private static Retrofit retrofit;

    // ================= MÉTHODE getClient =================

    // Cette méthode retourne l'objet Retrofit
    public static Retrofit getClient(){
// Vérifie si Retrofit n'existe pas encore
        if(retrofit == null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    // Convertisseur JSON avec Gson

                    // les réponses JSON en objets Java
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
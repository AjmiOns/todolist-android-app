package my.app.todolistprojet;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import my.app.todolistprojet.databinding.ActivityMainBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    // Binding du layout activity_main.xml
    private ActivityMainBinding binding;

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        // Récupération du BottomNavigationView
        BottomNavigationView navView =
                findViewById(R.id.nav_view);
        // Configuration des fragments de navigation
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(
                        R.id.navigation_home,
                        R.id.navigation_dashboard,
                        R.id.navigation_notifications)
                        .build();
        // Contrôleur de navigation
        NavController navController =
                Navigation.findNavController(
                        this,
                        R.id.nav_host_fragment_activity_main);
// Liaison BottomNavigation avec NavController
        NavigationUI.setupWithNavController(
                binding.navView,
                navController);

        // ================= INITIALISATION API =================

        // Création de l'objet API Retrofit

        apiService = RetrofitClient
                .getClient()
                .create(ApiService.class);
        // Charger badge notifications
        loadNotificationBadge(navView);
    }
    // Cette méthode est appelée quand l'activité revient au premier plan
    @Override
    protected void onResume() {
        super.onResume();

        BottomNavigationView navView =
                findViewById(R.id.nav_view);

        loadNotificationBadge(navView);
    }
    // Cette méthode affiche le nombre de tâches
    // ayant la date d'aujourd'hui
    private void loadNotificationBadge(
            BottomNavigationView navView) {
// Appel API pour récupérer les tâches
        apiService.getTasks()
                .enqueue(new Callback<List<Task>>() {
                    // ================= SUCCÈS API =================
                    @Override
                    public void onResponse(
                            Call<List<Task>> call,
                            Response<List<Task>> response) {
// Compteur des tâches du jour
                        int count = 0;
                        // Récupérer la date actuelle
                        String todayDate =
                                new SimpleDateFormat(
                                        "d/M/yyyy",
                                        Locale.getDefault())
                                        .format(new Date());

                        if(response.body() != null){

                            for(Task task : response.body()){

                                if(task.getDate_task() != null){

                                    String dbDate =
                                            task.getDate_task().trim();

                                    String today =
                                            todayDate.trim();
                                    // Supprimer les zéros inutiles
                                    // Exemple : 01/06/2025 → 1/6/2025
                                    dbDate =
                                            dbDate.replace("/0", "/");

                                    today =
                                            today.replace("/0", "/");
                                    // Comparer les deux dates
                                    if(dbDate.equals(today)){
                                        // Incrémenter compteur
                                        count++;
                                    }
                                }
                            }
                        }
                        // Créer ou récupérer le badge
                        BadgeDrawable badge =
                                navView.getOrCreateBadge(
                                        R.id.navigation_notifications);

                        badge.setVisible(true);

                        badge.setMaxCharacterCount(3);
                        // Couleur du badge
                        badge.setBackgroundColor(
                                getResources().getColor(
                                        android.R.color.holo_red_dark
                                )
                        );
// ================= AFFICHAGE DU NOMBRE =================

                        // Si au moins une tâche existe
                        if(count > 0){
                            // Afficher le nombre
                            badge.setNumber(count);

                        }else{

                            badge.clearNumber();
                        }
                    }
                    // ================= ÉCHEC API =================
                    @Override
                    public void onFailure(
                            Call<List<Task>> call,
                            Throwable t) { //classe mère des erreurs et exceptions

                    }
                });
    }}
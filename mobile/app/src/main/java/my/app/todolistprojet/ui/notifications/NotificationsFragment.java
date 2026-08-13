package my.app.todolistprojet.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import my.app.todolistprojet.ApiService;
import my.app.todolistprojet.R;
import my.app.todolistprojet.RetrofitClient;
import my.app.todolistprojet.Task;
import my.app.todolistprojet.TaskAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    ListView listNotification;

    ApiService apiService;

    List<Task> todayTasks;

    TaskAdapter adapter;
    // ================= onCreateView =================
    // Cette méthode crée l'interface du fragment
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
// Charger le layout XML
        View view = inflater.inflate(
                R.layout.fragment_notifications,
                container,
                false);
        // ================= INITIALISATION DES COMPOSANTS =================

        // Récupération du ListView
        listNotification =
                view.findViewById(
                        R.id.listNotification);
// ================= INITIALISATION LISTE =================

        // Création d'une liste vide
        todayTasks = new ArrayList<>();

        adapter = new TaskAdapter(
                getContext(),
                todayTasks
        );
        // Liaison adapter avec ListView
        listNotification.setAdapter(adapter);
        // ================= INITIALISATION API =================
        apiService = RetrofitClient
                .getClient()
                .create(ApiService.class);

        loadTodayTasks();
        // Charger les tâches du jour
        return view;
    }
    // ================= MÉTHODE loadTodayTasks =================
    // Cette méthode récupère les tâches
    // ayant la date d'aujourd'hui
    private void loadTodayTasks() {
        // Récupérer la date actuelle
        String todayDate =
                new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault())
                        .format(new Date());
// Appel API GET TASKS
        apiService.getTasks()

                .enqueue(new Callback<List<Task>>() {
                    // ================= SUCCÈS API =================
                    @Override
                    public void onResponse(
                            Call<List<Task>> call,
                            Response<List<Task>> response) {
// Vider ancienne liste
                        todayTasks.clear();
                        // Vérifie si la réponse existe
                        if(response.body() != null){
// Parcourir toutes les tâches
                            for(Task task
                                    : response.body()){
                                // Vérifie :
                                // la date existe ; la date correspond à aujourd'hui
                                if(task.getDate_task()
                                        != null &&

                                        task.getDate_task()
                                                .equals(todayDate)){
                                    // Ajouter tâche à la liste
                                    todayTasks.add(task);
                                }
                            }
                        }
                        // Actualiser ListView
                        adapter.notifyDataSetChanged();

                        if(todayTasks.isEmpty()){

                            Toast.makeText(
                                    getContext(),
                                    "Aucune tâche aujourd'hui",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                    // ================= ÉCHEC API =================
                    @Override
                    public void onFailure(
                            Call<List<Task>> call,
                            Throwable t) {

                    }
                });
    }
}
package my.app.todolistprojet.ui.dashboard;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import my.app.todolistprojet.ApiService;
import my.app.todolistprojet.R;
import my.app.todolistprojet.RetrofitClient;
import my.app.todolistprojet.Task;
import my.app.todolistprojet.TaskAdapter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    EditText editTask, editDate;

    Button btnAdd;

    ListView listView;

    List<Task> taskList;

    TaskAdapter adapter;

    ApiService apiService;
    // Cette méthode crée l'interface du fragment
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Charger le layout XML fragment_dashboard
        View view = inflater.inflate(
                R.layout.fragment_dashboard,
                container,
                false);
        // ================= INITIALISATION DES COMPOSANTS =================
        editTask = view.findViewById(R.id.editTask);

        editDate = view.findViewById(R.id.editDate);

        btnAdd = view.findViewById(R.id.btnAdd);

        listView = view.findViewById(R.id.listView);
        // ================= INITIALISATION DE LA LISTE =================
        // Création d'une liste vide
        taskList = new ArrayList<>();
        // Création de l'adapter personnalisé
        adapter = new TaskAdapter(
                getContext(),
                taskList);

        listView.setAdapter(adapter);

        apiService = RetrofitClient
                .getClient()
                .create(ApiService.class);
// Charger les tâches depuis la base de données
        loadTasks();

        // DATE PICKER
        // Lorsqu'on clique sur le champ date
        editDate.setOnClickListener(v -> {
// Obtenir la date actuelle
            Calendar calendar =
                    Calendar.getInstance();

            int year =
                    calendar.get(Calendar.YEAR);

            int month =
                    calendar.get(Calendar.MONTH);

            int day =
                    calendar.get(Calendar.DAY_OF_MONTH);
// Création du DatePickerDialog
            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(

                            getContext(),
                            // Action après sélection d'une date
                            (view1,
                             year1,
                             month1,
                             dayOfMonth) -> {

                                String date =
                                        String.format(
                                                "%04d-%02d-%02d",
                                                year1,
                                                (month1 + 1),
                                                dayOfMonth
                                        );
                                // Afficher la date choisie
                                editDate.setText(date);
                            },
                            // Date par défaut
                            year,
                            month,
                            day
                    );
            // Afficher le calendrier
            datePickerDialog.show();
        });

        // AJOUTER TÂCHE

        btnAdd.setOnClickListener(v -> {
            // Récupérer le titre
            String title =
                    editTask.getText()
                            .toString()
                            .trim();
            // Récupérer la date
            String date =
                    editDate.getText()
                            .toString()//Convertit le texte récupéré en chaîne de caractères
                            .trim(); //Supprime les espaces

            if(title.isEmpty()){

                Toast.makeText(
                        getContext(),
                        "Entrer une tâche",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if(date.isEmpty()){

                Toast.makeText(
                        getContext(),
                        "Choisir une date",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }
            // ================= APPEL API AJOUT =================

            apiService.addTask(title, date)

                    .enqueue(new Callback<ResponseBody>() {
                        // Succès de la requête
                        @Override
                        public void onResponse(
                                Call<ResponseBody> call,
                                Response<ResponseBody> response) {
                            // Vider les champs
                            editTask.setText("");

                            editDate.setText("");

                            loadTasks();
// Message succès
                            Toast.makeText(
                                    getContext(),
                                    "Tâche ajoutée",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                        // Échec de connexion
                        @Override
                        public void onFailure(
                                Call<ResponseBody> call,
                                Throwable t) {
                            // Afficher l'erreur
                            Toast.makeText(
                                    getContext(),
                                    t.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
        });
        // Retourner la vue du fragment
        return view;
    }

    // ================= MÉTHODE loadTasks =================
    // Cette méthode récupère les tâches depuis la base MySQL
    private void loadTasks() {
        // Appel API GET
        apiService.getTasks()

                .enqueue(new Callback<List<Task>>() {
                    // Succès récupération
                    @Override
                    public void onResponse(
                            Call<List<Task>> call,
                            Response<List<Task>> response) {
// Vider ancienne liste
                        taskList.clear();
                        // Vérifie si données existent
                        if(response.body() != null){

                            taskList.addAll(
                                    response.body());
                        }
                        // Actualiser ListView
                        adapter.notifyDataSetChanged();
                    }
                    // Échec récupération
                    @Override
                    public void onFailure(
                            Call<List<Task>> call,
                            Throwable t) {

                    }
                });
    }
}
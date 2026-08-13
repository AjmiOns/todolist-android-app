package my.app.todolistprojet;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
// Adaptateur pour afficher une liste d'objets
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import java.util.Calendar;

public class TaskAdapter extends ArrayAdapter<Task> {

    Context context;

    List<Task> tasks;

    ApiService apiService;

    // Cette classe permet d'afficher les tâches dans une ListView

    // ================= CONSTRUCTEUR =================
    public TaskAdapter(Context context, List<Task> tasks) {
        // Appel du constructeur parent ArrayAdapter
        super(context, 0, tasks);

        this.context = context;
        this.tasks = tasks;
// Création de l'objet Retrofit API
        apiService = RetrofitClient
                .getClient()
                .create(ApiService.class);
    }

    @Override
    public View getView(int position,
                        View convertView,
                        ViewGroup parent) {
        // Vérifie si la vue existe déjà
        // Sinon on charge le fichier XML

        if(convertView == null){

            convertView = LayoutInflater
                    .from(context)
                    .inflate(R.layout.item_task,
                            parent,
                            false);
        }

        // récupérer tâche

        Task task = tasks.get(position);

        // composants

        TextView textTitle =
                convertView.findViewById(R.id.textTitle);

        TextView textDate =
                convertView.findViewById(R.id.textDate);

        CheckBox checkCompleted =
                convertView.findViewById(R.id.checkCompleted);

        Button btnDelete =
                convertView.findViewById(R.id.btnDelete);

        Button btnEdit =
                convertView.findViewById(R.id.btnEdit);

        // afficher données

        textTitle.setText(task.getTitle());

        textDate.setText(task.getDate_task());

        checkCompleted.setChecked(
                task.getCompleted() == 1);

        // checkbox terminer tâche

        checkCompleted.setOnClickListener(v -> {
            // Si cochée => 1 sinon => 0
            int completed = checkCompleted.isChecked() ? 1 : 0;

            apiService.updateCompleted(task.getId(), completed)
                    .enqueue(new Callback<ResponseBody>() {
                        // Succès de la requête
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                // Modifier localement l'état
                                task.setCompleted(completed);
                                // Actualiser la ListView
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            checkCompleted.setChecked(!checkCompleted.isChecked()); // rollback UI
                        }
                    });
        });

        // supprimer tâche

        btnDelete.setOnClickListener(v -> {
            // Appel API suppression
            apiService.deleteTask(task.getId())

                    .enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(
                                Call<ResponseBody> call,
                                Response<ResponseBody> response) {

                            tasks.remove(position);

                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(
                                Call<ResponseBody> call,
                                Throwable t) {

                        }
                    });
        });

        // modifier tâche

        btnEdit.setOnClickListener(v -> {
            // Charger le layout du dialogue
            View dialogView = LayoutInflater
                    .from(context)
                    .inflate(R.layout.dialog_update_task,
                            null);

            EditText editTitle =
                    dialogView.findViewById(
                            R.id.editUpdateTask);

            EditText editDate =
                    dialogView.findViewById(
                            R.id.editUpdateDate);
            // Mettre les anciennes valeurs

            editTitle.setText(task.getTitle());

            editDate.setText(task.getDate_task());

            // DATE PICKER

            editDate.setOnClickListener(v2 -> {
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

                                context,
// Quand l'utilisateur choisit une date
                                (view1,
                                 year1,
                                 month1,
                                 dayOfMonth) -> {
                                    // Création de la nouvelle date

                                    String newDate =
                                            dayOfMonth + "/"
                                                    + (month1 + 1)
                                                    + "/"
                                                    + year1;

                                    editDate.setText(newDate);
                                },

                                year,
                                month,
                                day
                        );

                datePickerDialog.show();
            });
            // ================= ALERT DIALOG =================
            new AlertDialog.Builder(context)

                    .setTitle("Modifier tâche")

                    .setView(dialogView)

                    .setPositiveButton("Modifier",
                            (dialog, which) -> {

                                String newTitle =
                                        editTitle
                                                .getText()
                                                .toString();

                                String newDate =
                                        editDate
                                                .getText()
                                                .toString();

                                apiService.updateTask(
                                        task.getId(),
                                        newTitle,
                                        newDate
                                ).enqueue(
                                        new Callback<ResponseBody>() {

                                            @Override
                                            public void onResponse(
                                                    Call<ResponseBody> call,
                                                    Response<ResponseBody> response) {
                                                // Modifier localement
                                                task.setTitle(newTitle);

                                                task.setDate_task(newDate);

                                                notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onFailure(
                                                    Call<ResponseBody> call,
                                                    Throwable t) {

                                            }
                                        });
                            })

                    .setNegativeButton(
                            "Annuler",
                            null)

                    .show();
        });
        // Retourner la vue finale

        return convertView;
    }
}
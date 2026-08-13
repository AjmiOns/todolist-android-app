package my.app.todolistprojet;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    // GET TASKS
    @GET("get_tasks.php")
    Call<List<Task>> getTasks();

    // ADD TASK
    @FormUrlEncoded
    @POST("add_task.php")
    Call<ResponseBody> addTask(
            @Field("title") String title,
            @Field("date") String date
    );

    // UPDATE TASK (EDIT)
    @FormUrlEncoded
    @POST("update_task.php")
    Call<ResponseBody> updateTask(
            @Field("id") int id,
            @Field("title") String title,
            @Field("date") String date
    );

    // DELETE TASK
    @FormUrlEncoded
    @POST("delete_task.php")
    Call<ResponseBody> deleteTask(
            @Field("id") int id
    );

    // TOGGLE COMPLETE
    @FormUrlEncoded
    @POST("toggle_task.php")
    Call<ResponseBody> toggleTask(
            @Field("id") int id,
            @Field("completed") int completed
    );
    @FormUrlEncoded
    @POST("toggle_task.php")
    Call<ResponseBody> updateCompleted(
            @Field("id") int id,
            @Field("completed") int completed
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> registerUser(

            @Field("username") String username,

            @Field("email") String email,

            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> loginUser(

            @Field("email") String email,

            @Field("password") String password
    );
}
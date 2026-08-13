package my.app.todolistprojet;

public class Task {
    private String date_task;

    private int id;
    private String title;
    private int completed;
    public String getDate_task() {
        return date_task;
    }

    public void setDate_task(String date_task) {
        this.date_task = date_task;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
}
<?php

include 'db.php';

$sql = "SELECT * FROM tasks ORDER BY id DESC";

$result = $conn->query($sql);

$tasks = array();

while($row = $result->fetch_assoc()){

    $tasks[] = array(
        "id" => $row["id"],
        "title" => $row["title"],
        "date_task" => $row["date_task"],
        "completed" => $row["completed"]
    );
}

header('Content-Type: application/json');

echo json_encode($tasks);
?>
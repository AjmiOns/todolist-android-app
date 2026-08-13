<?php

include 'db.php';

$title = $_POST['title'];
$date = $_POST['date'];

$sql = "INSERT INTO tasks(title, date_task, completed)
        VALUES('$title', '$date', 0)";

if($conn->query($sql) === TRUE){
    echo "success";
}else{
    echo "error";
}
?>
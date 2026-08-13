<?php
include 'db.php';

$id = $_POST['id'];
$title = $_POST['title'];
$date = $_POST['date'];

$sql = "UPDATE tasks 
        SET title='$title',
            date_task='$date'
        WHERE id='$id'";

echo $conn->query($sql) ? "success" : "error";
?>
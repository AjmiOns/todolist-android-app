<?php
include 'db.php';

$id = $_POST['id'];
$completed = $_POST['completed'];

$sql = "UPDATE tasks
        SET completed='$completed'
        WHERE id='$id'";

if($conn->query($sql) === TRUE){
    echo "success";
}else{
    echo "error";
}
?>
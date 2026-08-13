<?php
include 'db.php';

if(isset($_POST['id'])){

    $id = $_POST['id'];

    $sql = "DELETE FROM tasks WHERE id=$id";

    echo $conn->query($sql) ? "success" : "error";
}
?>
<?php

include 'db.php';

$username = $_POST['username'];
$email = $_POST['email'];
$password = $_POST['password'];

$sql = "INSERT INTO users(username,email,password)

VALUES('$username','$email','$password')";

echo $conn->query($sql)
? "success"
: "error";

?>
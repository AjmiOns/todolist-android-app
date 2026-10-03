<?php
include 'db.php';

$username = trim($_POST['username'] ?? '');
$email    = trim($_POST['email'] ?? '');
$password = $_POST['password'] ?? '';

// Basic validation
if ($username === '' || $email === '' || $password === '') {
    echo "error";
    exit;
}
if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    echo "error";
    exit;
}
if (strlen($password) < 8) {
    echo "error";
    exit;
}

$hash = password_hash($password, PASSWORD_DEFAULT);

try {
    $stmt = $conn->prepare(
        "INSERT INTO users (username, email, password) VALUES (?, ?, ?)"
    );
    $stmt->bind_param("sss", $username, $email, $hash);
    $stmt->execute();
    echo "success";
} catch (mysqli_sql_exception $e) {
    // Includes duplicate email (UNIQUE constraint)
    echo "error";
}

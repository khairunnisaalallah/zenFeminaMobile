<?php
if(!empty($_POST['username']) && !empty($_POST['email']) && !empty($_POST['password'])){
    $con = mysqli_connect("localhost", "root","", "ZenFemina");
    $username = $_POST['username'];
    $email = $_POST['email'];
    $password = $_POST['password'];
    if ($con) {
        $sql= "INSERT INTO users (username, email, password) VALUES ('".$username."','".$email."','".$password."')";
        if (mysqli_query($con, $sql)){
            echo "Sukses";
            }else echo "Registrasi gagal";
        }else echo "Database koneksi gagal";
    }else echo "Semua kolom wajib diisi";
    ?>
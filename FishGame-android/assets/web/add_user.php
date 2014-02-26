<?php

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['intial']) && isset($_POST['score']) && isset($_POST['vmode']) && isset($_POST['age'])) {

    $intial = $_POST['intial'];
    $score = $_POST['score'];
    $age=$_POST['age'];
     settype($age, "integer");
    settype($score, "integer");
    $vmode = $_POST['vmode'];
    settype($vmode, "bool");

    $ip=$_SERVER['REMOTE_ADDR'];
    $fecha=date("Y-m-d");
    $hora=date("H:i:s");

    $db = new SQLite3('development.sqlite3');

            $db->query("INSERT into users(intial, vmode, score,created_at,updated_at,age)VALUES('$intial', '$vmode', '$score' ,'$fecha','$hora' ,'$age')");
            
             $response["last player Id"]= $db->lastInsertRowID();
            // echoing JSON response
            echo json_encode($response);
             
}

?>
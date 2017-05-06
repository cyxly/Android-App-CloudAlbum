
<?php

//create database
$db_server="sophia.cs.hku.hk";
$db_user="yxchen";
$db_pwd="1993cyx";
$link = mysql_connect($db_server, $db_user, $db_pwd) or die(mysql_error());
$db_selected = mysql_select_db($db_user, $link);

// insert  5.1
$action=(isset($_GET['action']) ? $_GET['action'] : "");
$user_id=(isset($_GET['id']) ? $_GET['id'] : "");
$user_pwd=(isset($_GET['password']) ? $_GET['password'] : "");

if ($action == "insert" && $user_id){
    $sql = "INSERT INTO users (id,password) values ('$user_id','$user_pwd');";
    $res = mysql_query($sql) or die(mysql_error());

}



$course_code="COMP7506";
$course_name="Smart Phone Apps Development";
$instructors = array("a" => "TJX","b"=>"Dr.Tong" );
echo'{';
echo '"course_code":"' .$course_code .'"';
echo ', "course_name":"' .$course_name .'"';
// foreach ($instructors as $key => $val) {
//     echo', "instructor_subclass_'. $key.'":"'.$val. '"';
// }


// 4.2  FETCH_DATA FROM DATABASE
$id=array();
$pwd=array();
$sql1="select id from users;";
$sql2="select password from users;";

$res_id=mysql_query($sql1) or die(mysql_error());
$res_pwd=mysql_query($sql2) or die(mysql_error());

while($row=mysql_fetch_array($res_id)){
    array_push($id, $row['id']);
    // echo $row['id'];
}

while ($col=mysql_fetch_array($res_pwd)) {
    array_push($pwd, $col['password']);
}

echo ', "id":[';
$add_delimiter=false;
for ($i=0; $i < count($id); $i++) { 
    echo ($add_delimiter ? ', ' : '') . '"' . $id[$i] . '"';
    $add_delimiter=true;
}
echo ']';

echo ', "password":[';
$add_delimiter=false;
for ($i=0; $i < count($pwd); $i++) { 
    echo ($add_delimiter ? ', ' : '') . '"' . $pwd[$i] . '"';
    $add_delimiter=true;
}
echo ']';

echo "}";
?>
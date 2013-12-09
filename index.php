<?php

require 'Slim/Slim.php';

$app = new Slim();
//$app->response()->header('Content-Type', 'application/json');

//Generate Response headers
header("Content-Type: application/json");

$app->get('/', 'getpost_err'); // adresse erronée
$app->post('/', 'getpost_err'); // adresse erronée


$app->get('/post', 'getpost_err'); // adresse erronée
$app->get('/post/:err', 'getpost_err'); // adresse erronée
$app->get('/post/:err/:err2', 'getpost_err'); // adresse erronée
$app->post('/post', 'getpost_err'); // adresse erronée
$app->post('/post/:err', 'getpost_err'); // adresse erronée
$app->post('/post/:id_generator/:id_sensor', 'post');

function getpost_err() {
	$app = Slim::getInstance();
	//$app->response()->body(json_encode(array("a" => "Err")));
	echo json_encode(array("result" => "Err"));
	exit ;
}

//$request = Slim::getInstance()->request();
//$wine = json_decode($request->getBody());

function is_int_sup($x) {
	return(intval($x) == $x and $x >= 1) ;
}

function post($id_generator, $id_sensor) {
	$app = Slim::getInstance();
	$request = Slim::getInstance()->request();
	
	if ( $request->post('id_generator') != $id_generator or $request->post('id_sensor') != $id_sensor )
	{
		echo json_encode(array("result" => "Err1"));
		exit ;
	}
	
	$id_generator = $request->post('id_generator') ;
	$id_sensor = $request->post('id_sensor') ;
	$value = $request->post('value') ;
	$pass = $request->post('pass') ;
	// $date = $request->post('date') ;
	
	if ( !is_numeric($value) or !is_int_sup($id_generator) or !is_int_sup($id_sensor) )
	{
		echo json_encode(array("result" => "Err3"));
		exit ;
	}
	
	$sql = "SELECT id_generator FROM generators WHERE `id_generator` = :id_generator AND `password` = :pass" ;
	try {
		$db = getConnection();
		$stmt = $db->prepare($sql);
		$stmt->bindParam("id_generator", $id_generator);
		$stmt->bindParam("pass", $pass);
		$stmt->execute();
		$generators = $stmt->fetchAll(PDO::FETCH_ASSOC);
        $db = null;
		// echo $generators[0]['id_generator'] ; // id_generator result
        if ( count($generators) != 1 )
		{
			echo json_encode(array("result" => "Err1")); // err d'authentification
			exit ;
		}
	} catch(PDOException $e) {
		echo json_encode(array("result" => "Err4"));
		exit ;
	}
	
	$sql = "INSERT INTO data (`id_generator`,`id_sensor`,`value`) VALUES (:id_generator, :id_sensor, :value)" ;
	try {
		$db = getConnection();
		$stmt = $db->prepare($sql);
		$stmt->bindParam("id_generator", $id_generator);
		$stmt->bindParam("id_sensor", $id_sensor);
		$stmt->bindParam("value", $value);
		//$stmt->bindParam("date", time());
		$stmt->execute();
		$db = null;
	} catch(PDOException $e) {
		echo json_encode(array("result" => "Err4"));
		exit ;
	}
	
	echo json_encode(array("result" => "OK"));
	exit ;
}





// $_GET['pass'] $_GET['id_generator'] as input
$app->get('/get', 'getpost_err');
$app->get('/get/:err', 'getpost_err');
$app->get('/get/:err/:err2', 'getpost_err');
$app->get('/get/:pass/:id_generator/:id_sensor', 'get');
$app->get('/get/:pass/:id_generator/:id_sensor/:nb', 'get');

function get($pass, $id_generator, $id_sensor, $nb = 50) {
	$app = Slim::getInstance();
	$request = Slim::getInstance()->request();
	
	if ( !is_int_sup($id_generator) or !is_int_sup($id_sensor) ) // TODO : tester $nb
	{
		echo json_encode(array("result" => "Err3"));
		exit ;
	}
	
	$sql = "SELECT id_generator FROM generators WHERE `id_generator` = :id_generator AND `password` = :pass" ;
	try {
		$db = getConnection();
		$stmt = $db->prepare($sql);
		$stmt->bindParam("id_generator", $id_generator);
		$stmt->bindParam("pass", $pass);
		$stmt->execute();
		$generators = $stmt->fetchAll(PDO::FETCH_ASSOC);
        $db = null;
		// echo $generators[0]['id_generator'] ; // id_generator result
        if ( count($generators) != 1 )
		{
			echo json_encode(array("result" => "Err1")); // err d'authentification
			exit ;
		}
	} catch(PDOException $e) {
		echo json_encode(array("result" => "Err4"));
		exit ;
	}
	
	$sql = "SELECT * FROM data WHERE `id_generator` = :id_generator AND `id_sensor` = :id_sensor ORDER BY id_data DESC LIMIT 0, $nb" ;
	try {
		$db = getConnection();
		$stmt = $db->prepare($sql);
		$stmt->bindParam("id_generator", $id_generator);
		$stmt->bindParam("id_sensor", $id_sensor);
		//$stmt->bindParam("nb", $nb, PDO::PARAM_INT);
		$stmt->execute();
		$data = $stmt->fetchAll(PDO::FETCH_ASSOC);
        $db = null;
        if ( count($data) <= 0 )
		{
			echo json_encode(array("result" => "Err1")); // err pas de data
			exit ;
		}
		
		echo json_encode(array("result" => "OK", "data" => $data));
		exit ;
	} catch(PDOException $e) {
		echo json_encode(array("result" => $e));
		exit ;
	}
}

$app->run();



function getConnection() {
	$mysql_host = "mysql10.000webhost.com";
	$mysql_database = "a1929666_gene";
	$mysql_user = "a1929666_gene";
	$mysql_password = "Harduino1";
    $dbh = new PDO("mysql:host=$mysql_host;dbname=$mysql_database", $mysql_user, $mysql_password);
    $dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    return $dbh;
}

?>

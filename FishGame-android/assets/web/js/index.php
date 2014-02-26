<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="refresh" content="15" />
    <title>Fish Police !</title>
   
    <script src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
    <script src="js/jquery.mobile-1.0rc1.min.js"></script>
    <script src="js/rails.js"></script>
    <script src="js/get-year.php"></script>  
    <script src="js/highcharts.js"></script>

    </head>
<body>
<script type="text/javascript" charset="utf-8">
<?php
    $db = new SQLite3('development.sqlite3');
    
    $results = $db->query('select * from (select * from users order by id DESC limit 11) order by id DESC;');
    $storeIntial = Array();
    $storeScore=Array();
    while ($row = $results->fetchArray()) {
        $storeIntial[]= $row['intial'] ;
        $storeScore[]=$row['score'];
    }
    ?>
    $(function(){
      var score=<?php echo  JSON_encode($storeScore);?>;
      var intial=<?php echo JSON_encode($storeIntial); ?>;

           
           <center>     new Highcharts.Chart({
                    title: {
                        text: 'The Last 10 '
                    },
                    chart:{
                        renderTo : "users_chart",

                        type:'column',

                        marginRight: 80

                    },

                    xAxis: {
                        title: {
                            text: 'Initial'
                        },
                        categories:intial
            },
            yAxis: [{
        title: {
            text: 'Accuracy Score'
        }

    }],

            series: [{

        data: score

    }]
    });</center>

    });
</script>
   
<h1> Welcome To Fish Police !</h1>
<div id="users_chart" style="width:800px;height:500px;"></div>

</body>
</html>
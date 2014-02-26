<!DOCTYPE html>
<html>
<head>
  
    <meta http-equiv="refresh" content="15" />
    <title>Fish Police!</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

    <script src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
    <script src="js/jquery.mobile-1.0rc1.min.js"></script>
    <script src="js/rails.js"></script>
       <script src="js/highcharts.js"></script>

    </head>
<body>
<script type="text/javascript" charset="utf-8">
<?php
    $db = new SQLite3('development.sqlite3');
    
    $results = $db->query('select * from (select * from users where vmode=0  order by id DESC limit 11) order by id DESC;');
    $storeIntial = Array();
    $storeScore=Array();
    while ($row = $results->fetchArray()) {
        $storeIntial[]= $row['intial'] ;
        $storeScore[]=$row['score'];
    }
     $results = $db->query('select * from (select * from users where vmode=1 order by id DESC limit 11) order by id DESC;');
    $storeIntialv = Array();
    $storeScorev=Array();
    while ($row = $results->fetchArray()) {
        $storeIntialv[]= $row['intial'] ;
        $storeScorev[]=$row['score'];
    }

    ?>
    $(function(){
      var scorev=<?php echo  JSON_encode($storeScorev);?>;
      var intialv=<?php echo JSON_encode($storeIntialv); ?>;

           
               new Highcharts.Chart({
                    title: {
                        text: 'The Last 10 who Play visual mode '
                    },
                    chart:{
                        renderTo : "users_chart",
                        borderColor: '#EBBA95',
                        borderWidth: 2,

                        type:'column',

                        marginRight: 80

                    },

                    xAxis: {
                        title: {
                            text: 'Initial'
                        },
                        categories:intialv
            },
            yAxis: [{
        title: {
            text: 'Accuracy Score'
        }
      ,
      min: 0,
      max: 100
    }],

            series: [{

        data: scorev

    }]
    });
      var score=<?php echo  JSON_encode($storeScore);?>;
      var intial=<?php echo JSON_encode($storeIntial); ?>;
      

      new Highcharts.Chart({
                           title: {
                           text: 'The Last 10  who play Audio mode'
                           },
                           chart:{
                           renderTo : "users_chart2",
                           borderColor: '#EBBA95',
                           borderWidth: 2,
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
                           ,
                           min: 0,
                           max: 100
                           }],
                           
                           series: [{
                           
                           data: score
                           
                           }]
                           });

    });
</script>

<table>
<tr>
<td colspan="2">
<div style="background-image:url(images/header.jpg);height:70px;">
</div></td>
</tr>
<tr>
<td><div id="users_chart" style="width:630px;height:600px ;align:center ;margin: 0 auto 0 auto;"></div></td>
<td><div id="users_chart2" style="width:630px;height:600px ;align:center ;margin: 0 auto 0 auto;"></div></td>

</tr>
</table>

</body>
</html>
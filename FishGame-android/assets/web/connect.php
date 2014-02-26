 <?php
        $db = new SQLite3('development.sqlite3');
        
        $results = $db->query('SELECT intial FROM users');
        while ($row = $results->fetchArray()) { 
          echo "  <p> ID = ". $row['intial'] . "\n </p>";
        }
      ?>

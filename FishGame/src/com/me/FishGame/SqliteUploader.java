package com.me.FishGame;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;

public class SqliteUploader {
	private static final  String url = "http://moore.cs-i.brandeis.edu/fishpolice!/add_user2.php";
	public static String pre_post(Player p,int[]type, int[] count, int[] response ) {

        String typefish=Arrays.toString(type).replace("[","").replace("]", "");
        String countfish=Arrays.toString(count).replace("[","").replace("]", "");
        String responsefish=Arrays.toString(response).replace("[","").replace("]", "");
		String urlParameters = "";
        System.out.println (" fish type " + Arrays.toString(type));
		try {
			
			urlParameters = "intial=" + URLEncoder.encode(p.intial, "UTF-8") +
					"&score=" + URLEncoder.encode(""+p.score, "UTF-8") +
					"&vmode=" + URLEncoder.encode(""+p.vmode, "UTF-8") +
					"&age=" + URLEncoder.encode(""+p.age, "UTF-8")+
					"&fishtype="+URLEncoder.encode(""+typefish, "UTF-8")+
					"&response="+URLEncoder.encode(""+responsefish, "UTF-8")+
					"&fishcount="+URLEncoder.encode(""+countfish, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Gdx.app.log("error",  "Error21: " + e.getMessage());   
			e.printStackTrace();
		}


		String post = executePost( url , urlParameters ) ;

		Gdx.app.log( " s","HttpPost: " + post);
	
		return post;   


	}


	private static String executePost(String targetURL, String urlParameters)
	{
		URL url;
		HttpURLConnection connection = null;  
		try {
			//Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", 
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length", "" + 
					Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");  

			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			//Send request
			DataOutputStream wr = new DataOutputStream (
					connection.getOutputStream ());
			wr.writeBytes (urlParameters);
			wr.flush ();
			wr.close ();

			//Get Response   
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer(); 
			while((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (Exception e) {

			e.printStackTrace();
			return "** error **";

		} finally {

			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}
}

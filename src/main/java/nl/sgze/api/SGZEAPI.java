package nl.sgze.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class SGZEAPI{

	public static final String BASE_URL = "https://my.sgze.nl/api";

	public static Response login(String login_name,String password) throws IOException{
		String url = SGZEAPI.BASE_URL+"/login";
		Map<String,String> parameters = new HashMap<>();
		parameters.put("login_name",login_name);
		parameters.put("password",password);
		return SGZEAPI.call(url,null,SGZEAPI.mapToContent(parameters));
	}

	public static class Systems{

		public static final String BASE_URL = SGZEAPI.BASE_URL+"/systems";

		public static Response check(String api_token,String zipcode,String email) throws IOException{
			return SGZEAPI.Systems.check(api_token,zipcode,email,false);
		}

		public static Response check(String api_token,String zipcode,String email,boolean useBearer) throws IOException{
			String url = SGZEAPI.Systems.BASE_URL+"/check";
			Map<String,String> parameters = new HashMap<>();
			if(!useBearer){
				parameters.put("api_token",api_token);
			}
			parameters.put("zipcode",zipcode);
			parameters.put("email",email);
			return SGZEAPI.call(url,useBearer?api_token:null,SGZEAPI.mapToContent(parameters));
		}

		public static Response create(String api_token,String gender,String initials,String insertion,String lastname,String address,String zipcode,String city,String email,String phone,String wattpeak) throws IOException{
			return SGZEAPI.Systems.create(api_token,gender,initials,insertion,lastname,address,zipcode,city,email,phone,wattpeak,false);
		}

		public static Response create(String api_token,String gender,String initials,String insertion,String lastname,String address,String zipcode,String city,String email,String phone,String wattpeak,boolean useBearer) throws IOException{
			String url = SGZEAPI.Systems.BASE_URL+"/check";
			Map<String,String> parameters = new HashMap<>();
			if(!useBearer){
				parameters.put("api_token",api_token);
			}
			parameters.put("gender",gender);
			parameters.put("initials",initials);
			parameters.put("insertion",insertion);
			parameters.put("lastname",lastname);
			parameters.put("address",address);
			parameters.put("zipcode",zipcode);
			parameters.put("city",city);
			parameters.put("email",email);
			parameters.put("phone",gender);
			parameters.put("wattpeak",gender);
			return SGZEAPI.call(url,useBearer?api_token:null,SGZEAPI.mapToContent(parameters));
		}

	}

	private static Response call(String url,String bearer,String parameters) throws IOException{
		HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		if(bearer!=null){
			conn.setRequestProperty("Authorization","Bearer "+bearer);
		}
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		conn.setUseCaches(false);
		conn.connect();
		conn.getOutputStream().write(parameters.getBytes());
		conn.getOutputStream().flush();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while((line = br.readLine())!=null){
			sb.append(line);
		}
		Response response = new Response();
		response.statusCode = conn.getResponseCode();
		response.content = sb.toString();
		return response;
	}

	private static String mapToContent(Map<String,String> map){
		List<String> list = new ArrayList<>();
		for(Map.Entry<String,String> entry : map.entrySet()){
			list.add(entry.getKey()+"="+entry.getValue());
		}
		return String.join("&",list);
	}

	public static class Response{

		public int statusCode;
		public String content;

	}

}
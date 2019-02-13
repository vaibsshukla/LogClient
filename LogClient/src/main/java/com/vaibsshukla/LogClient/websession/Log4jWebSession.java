package com.vaibsshukla.LogClient.websession;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.apache.logging.log4j.LogManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
//import org.stompclient.pkg.MyHandler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Log4jWebSession 
{
	private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
	private StompSession session = null;
	
	public void sendLog(byte[] bytes ) throws UnsupportedEncodingException
	{
        Gson gson = new Gson();
        JsonObject json=new JsonObject();
        String str=new String(bytes, "UTF-8");
        json.addProperty("name", str);
        String jsonString =gson.toJson(json);
        session.send("/app/logs", jsonString.getBytes()).getReceiptId();		
	}
	
	public void connects() throws InterruptedException, ExecutionException
	   {
		   WebSocketClient transport = new StandardWebSocketClient();
		   WebSocketStompClient stompClient = new WebSocketStompClient(transport);
		   stompClient.setMessageConverter(new StringMessageConverter());
	        String url = "ws://localhost:8081/gs-guide-websocket";
	        StompSessionHandler handler = new stompHandler();
	        ListenableFuture<StompSession> f = stompClient.connect(url,handler);
	        session = f.get();       
	   }
}


class stompHandler extends StompSessionHandlerAdapter {
	
	public static org.apache.logging.log4j.Logger logger=LogManager.getLogger(stompHandler.class);
	
	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		System.out.println("Connected");
	}   
}

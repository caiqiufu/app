package com.slipper.unieap.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class MyWebSocketClient extends WebSocketClient {

	public MyWebSocketClient(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("Connected to WebSocket server");
	}

	@Override
	public void onMessage(String message) {
		System.out.println("Received message: " + message);
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("Connection closed with code: " + code + ", reason: " + reason);
	}

	@Override
	public void onError(Exception ex) {
		if (ex.getMessage().contains("403 Forbidden")) {
			System.err.println(
					"WebSocket connection failed due to a 403 Forbidden error. Check authentication and access permissions.");
		} else {
			System.err.println("WebSocket error: " + ex.getMessage());
		}
	}

	public static void main(String[] args) {
		try {
			String serverUri = "wss://data.tradingview.com/socket.io/websocket?from=symbols/GOLD/technicals/&date=2024_01_14-15_33"; // Replace with your WebSocket server URI
			MyWebSocketClient client = new MyWebSocketClient(new URI(serverUri), new Draft_6455());
			client.connect();

			// ... (other parts of the main method remain the same)
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}

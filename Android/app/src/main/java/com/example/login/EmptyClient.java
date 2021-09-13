package com.example.login;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

//WebSocketClient에서 파생
public class EmptyClient extends WebSocketClient{

    public EmptyClient(URI serverUri, Draft draft) {
        super(serverUri,draft);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("new connection opened");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("received message: "+message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code "+code +"additional info "+reason);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("an error occured "+ex);
    }
}

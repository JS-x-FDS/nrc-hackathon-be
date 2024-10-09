package jds.nrc.be.socket.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {

    @Value("${server.socket.host}")
    private String host;

    @Value("${server.socket.port}")
    private int port;

    @Value("${server.socket.context}")
    private String context;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);
//        config.setContext(context);
        config.setTransports(Transport.WEBSOCKET, Transport.POLLING);

        config.setOrigin("*");

        return new SocketIOServer(config);
    }
}

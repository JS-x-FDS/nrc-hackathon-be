package jds.nrc.be.socket;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketClient {

    private final SocketIOServer server;

    private UUID AI_UUID;

    @PostConstruct
    private void startServer() {
        configAINamespace();
        configFENamespace();

        server.start();
        log.info("server started at {}:{}{}", server.getConfiguration().getHostname(), server.getConfiguration().getPort(), server.getConfiguration().getContext());
    }

    private void configAINamespace() {
        SocketIONamespace aiNamespace = server.addNamespace("/ai");

        aiNamespace.addConnectListener((client) -> {
            AI_UUID = client.getSessionId();
            log.info("AI connected: {}", AI_UUID.toString());
        });

        aiNamespace.addEventListener(SocketEvents.ON_RECEIVE_AI_RESPONSE, String.class, (client, data, ackSender) -> {
            try {
                log.info("AI -> BE: {}", data);
                server.getNamespace("/fe")
                        .getBroadcastOperations()
                        .sendEvent(SocketEvents.EMIT_DATA_TO_FE, data);
                log.info("BE -> FE: {}", data);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
    }

    private void configFENamespace() {
        SocketIONamespace feNamespace = server.addNamespace("/fe");

        feNamespace.addConnectListener((client) -> {
            log.info("FE connected: {}", client.getSessionId().toString());
        });

        feNamespace.addEventListener(SocketEvents.ON_RECEIVE_FE_REQUEST, String.class, (client, data, ackSender) -> {
            try {
                log.info("FE -> BE: {}", data);
                server.getNamespace("/ai")
                        .getClient(AI_UUID)
                        .sendEvent(SocketEvents.EMIT_DATA_TO_AI, data);
                log.info("BE -> AI: {}", data);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
    }
}

package com.demo.emsed_rtsc.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.demo.emsed_rtsc.incidents.Incident;
import com.demo.emsed_rtsc.incidents.IncidentSearchDto;

@Controller
public class WebsocketController {
    
    @MessageMapping("/greeting")
    @SendTo("/topic/incidents")
    public Incident greeting(IncidentSearchDto searchDTO) {
        Incident i = new Incident();
        i.setId("blub");
        return i;
    }

}

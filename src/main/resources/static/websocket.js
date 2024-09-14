let knownIds = [];

const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8081/gs-guide-websocket'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/incidents', (incidentsSearchResponse) => {
        showIncidents(JSON.parse(incidentsSearchResponse.body));
    });
    stompClient.subscribe('/topic/incident', (incidentCreationResponse) => {
        showIncident(JSON.parse(incidentCreationResponse.body));
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#incidents").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function searchIncidents() {
    let severityLevels = $("#severityLevel").val() === "" ? null : [...$("#severityLevel").val().filter((level) => {return (level !== "")})]
    let incidentTypes = $("#incidentType").val() === "" ? null : [...$("#incidentType").val().filter((level) => {return (level !== "")})]
    let body = {'severityLevels': severityLevels, 'incidentTypes': incidentTypes};
    console.log("sending: " + JSON.stringify(body))
    stompClient.publish({
        destination: "/app/search",
        body: JSON.stringify(body)
    });
}

function showIncidents(message) {
    knownIds = [];
    $("#incidents").empty();
    message.content.forEach(element => {
        showIncident(element);
    });
}

function showIncident(incident) {
    console.log("received: " + JSON.stringify(incident))
    if (!knownIds.includes(incident.id)) {
        let incidentRow = "<td>" + incident.incidentType + "</td>";
        incidentRow += "<td>" + incident.severityLevel + "</td>";
        incidentRow += "<td>" + (incident.location ? incident.location.latitude + "/" + incident.location.longitude : "not known") + "</td>";
        incidentRow += "<td>" + incident.timestamp + "</td>";
        $("#incidents").prepend("<tr>" + incidentRow + "</tr>");
        knownIds.push(incident.id);
    }
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => searchIncidents());
});
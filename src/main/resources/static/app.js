var stompClient = null;

$(function () {
    var socket = new SockJS('/trades');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/trades', function (greeting) {
            showGreeting(greeting.body);
        });
    });
});

function showGreeting(message) {
    var obj = JSON.parse(message);
    $("#trades").append("<tr>" +
        "<td>" + obj.tradeId + "</td>" +
        "<td>" + obj.version + "</td>" +
        "<td>" + obj.counterPartyId + "</td>" +
        "<td>" + obj.bookId + "</td>" +
        "<td>" + obj.maturityDate + "</td>" +
        "<td>" + obj.createdDate + "</td>" +
        "<td>" + obj.expired + "</td>" +
        "</tr>");

    var timeDelay = 4900;       // DELAY IN MILLISECONDS (OR SIMPLY, 5 SECONDS DELAY).
    setTimeout(clearContents, timeDelay);
    function clearContents() {
        $("#trades").empty();
    }
}
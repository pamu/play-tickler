$(function() {
    var websocket = new Webocket("/socket")
    websocket.onopen = function(evt) {
    }
    websocket.onclose = function(evt) {
    }
    websocket.onmessage = function(evt) {
        console.log(evt)
    }
});
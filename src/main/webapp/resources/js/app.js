var ping = function(){
    $.ajax("/ping", {
        accepts: {
            text: "application/json"
        },
        success: function(data) {
            console.dir(data);
            alert(data.message);
        },
        error: function() {
            alert("Error");
        }
    });
};

var poll = function () {
    $.ajax("/poll", {
        accepts: {
            text: "application/json"
        },
        success: function(data) {
            console.dir(data);
            alert(data.message);
        },
        error: function() {
            alert("Error");
        }
    });
};

$( document ).ready(function() {
    $('#pingBtn').click(function(){
        ping();
    });
    $('#pollBtn').click(function(){
        poll();
    })
});

var ping = function(){
    var pingMsg = $('#pingMsg').val();
    if (_.isEmpty(pingMsg)){
        bootbox.alert("The messsage cannot be empty.", function () {});
        return false;
    }

    $.ajax("/ping/" + pingMsg, {
        accepts: {
            text: "application/json"
        },
        success: function(data) {
            console.dir(data);
            bootbox.alert(data.message, function () {});
        },
        error: function() {
            bootbox.alert("Error", function () {});
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

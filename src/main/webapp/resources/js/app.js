APP.syncPoll = function (index) {
    return $.ajax({
        url: SERVLET_CONTEXT + "/appMsgs?startId=" + index,
        accepts: {
            text: "application/json"
        }

    });
};

APP.asyncPoll = function (index) {
    return $.ajax({
        url: SERVLET_CONTEXT + "/appMsgsAsync?startId=" + index,
        accepts: {
            text: "application/json"
        }
    });
};

APP.updateSync = function (data) {
    _.each(data, function (appMsg) {
        var id = appMsg.id;
        var latency = Date.now() - appMsg.timeStamp;
        if (latency > 1000){
            latency = Math.floor(latency / 1000)  + ' second(s)'
        }
        else {
            latency = latency +  ' ms';
        }
        APP.syncIndex = (id >= APP.syncIndex) ? id + 1 : APP.syncIndex;
        $('#syncTable tbody').append('<tr><td>'+appMsg.id+'</td><td>'+appMsg.message+'</td><td>'+latency+'</td></tr>');
    });
    ($('#syncTable tbody tr').length > 0) ? $('#syncTable').show() : $('#syncTable').hide();

}

APP.updateAsync = function (data) {
    _.each(data, function (appMsg) {
        var id = appMsg.id;
        var latency = Date.now() - appMsg.timeStamp;
        if (latency > 1000){
            latency = Math.floor(latency / 1000)  + ' second(s)'
        }
        else {
            latency = latency +  ' ms';
        }
        APP.asyncIndex = (id >= APP.asyncIndex) ? id + 1 : APP.asyncIndex;
        $('#asyncTable tbody').append('<tr><td>'+appMsg.id+'</td><td>'+appMsg.message+'</td><td>'+latency+'</td></tr>');
    });
    ($('#asyncTable tbody tr').length > 0) ? $('#asyncTable').show() : $('#asyncTable').hide();

};

APP.create = function () {
    var newMsg = $('#postMsg').val();
    if (_.isEmpty(newMsg)) {
        bootbox.alert("The messsage cannot be empty.", function () {});
        return false;
    }

    $.ajax({
        url: SERVLET_CONTEXT + "/appMsgs",
        type: "POST",
        data: {
            msg: newMsg
        },
        accepts: {
            text: "application/json"
        },
        success: function (data) {
            $.bootstrapGrowl("Added message (" + data.id + "): " + data.message, {
                align: 'right',
                type: 'success',
                width: 'auto',
                offset: {from: 'bottom', amount: 20}, // 'top', or 'bottom'

            });
        },
        error: function (xhr, errorMsg, msg) {
            console.dir(arguments);
            bootbox.alert("Error: " +msg, function () {
            });
        },
        complete: function () {
            $('#postMsg').val('').focus();
        }

    });
};

//APP.deleteMsgs = function () {
//    $.ajax("/appMsgs", {
//        type: "DELETE",
//        accepts: {
//            text: "application/json"
//        },
//        success: function (data) {
//            $.bootstrapGrowl("Deleted " + data + ' messages.', {
//                align: 'right',
//                type: 'success',
//                width: 'auto',
//                offset: {from: 'bottom', amount: 20}, // 'top', or 'bottom'
//            });
//        },
//        error: function () {
//            bootbox.alert("Error", function () {
//            });
//        },
//    });
//};


var readAll = function () {
    $.ajax("/appMsgs", {
        type: "GET",
        accepts: {
            text: "application/json"
        },
        success: function (data) {
            console.dir(data);
            bootbox.alert(JSON.stringify(data), function () {});
        },
        error: function () {
            alert("Error");
        }
    });
};

$(document).ready(function () {
    $('#postMsg').focus();
    $('#postBtn').click(function () {
        APP.create();
    });

    //$('#postMsg').keypress('keyup', function(e) {
    //    if ( e.keyCode === 13 ) { // 13 is enter key
    //        APP.create();
    //    }
    //});
    //$('#deleteBtn').click(function () {
    //    APP.deleteMsgs();
    //});

    //setup the sync poller
    APP.poll = function(){
        $('#syncSpinner').show();
        APP.syncPoll(APP.syncIndex).
            always(function (result) {
                //add a bit of delay to the hide so that the user actually sees that the request is happening
                setTimeout(function(){
                    $('#syncSpinner').hide();
                }, 2000);

                var node = $('#syncCount');
                var count = $(node).attr('data-count');
                count = parseInt(count) + 1;
                $(node).attr('data-count', count.toString()).text(count.toString());
            }).
            done(function (result) {
                APP.updateSync(result)
            }).
            error(function(result) {
                console.dir(arguments);
                console.log("error making sync call");
            });
    };
    //initial call
    APP.poll();
    //poll using setInterval
    setInterval(APP.poll, APP.pollTime);

    APP.recurseAsync = function(){
        $('#asyncSpinner').show();
        APP.asyncPoll(APP.asyncIndex).
            always(function (result) {
                $('#asyncSpinner').hide();
                var node = $('#asyncCount');
                var count = $(node).attr('data-count');
                count = parseInt(count) + 1;
                $(node).attr('data-count', count.toString()).text(count.toString());
            }).
            done(function (result) {
                APP.updateAsync(result);
                APP.recurseAsync();
            }).
            error(function(xhr, errorStr, statusTxt) {
                console.dir(arguments);
                console.log("error making async call - waiting 60 seconds until next try");
                //proxy timeout - let's just start again
                if (xhr.status === 504){
                    APP.recurseAsync();
                }
                //some other error - possibly the server went down
                else {
                    setTimeout(APP.recurseAsync, 60000);
                }
            });
    };
    APP.recurseAsync();
});

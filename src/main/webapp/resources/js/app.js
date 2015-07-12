//Naive Poller just runs again and again, trying for new messages.
setInterval(function () {
    $.ajax({
        url: "/appMsgs?startId=" + APP.naiveIndex,
        accepts: {
            text: "application/json"
        },
        success: function (data) {
            var timeInSecs = Math.floor(Date.now() / 1000);
            var msgList = $('#naivePollList');
            _.each(data, function (appMsg) {
                var id = appMsg.id;
                var latency = Math.floor(timeInSecs - (appMsg.timeStamp / 1000));
                APP.naiveIndex = (id >= APP.naiveIndex) ? id + 1 : APP.naiveIndex;
                var childLi = '<li>' + id + ':   ' + appMsg.message + ' [Latency=' + latency + ' seconds]' + '</li>';
                msgList.append(childLi);
            });
        },
        error: function () {
            bootbox.alert("Error", function () {
            });
        },
        complete: function () {
            var node = $('#naiveCount');
            var count = node.data('count');
            count = parseInt(count) + 1;
            node.attr('data-count', count).text(count);
        }

    });
}, APP.pollTime);

//Long Poller - uses Spring deferredResults and async servlet handling
(function longPoll() {
    debugger;
    setTimeout(function () {
        $.ajax({
            url: "appMsgsAsync?startId=" + APP.longIndex,
            accepts: {
                text: "application/json"
            },
            success: function (data) {
                //Update your dashboard gauge
                var timeInSecs = Math.floor(Date.now() / 1000);
                var msgList = $('#longPollList');
                _.each(data, function (appMsg) {
                    var id = appMsg.id;
                    var latency = Math.floor(timeInSecs - (appMsg.timeStamp / 1000));
                    APP.longIndex = (id >= APP.longIndex) ? id + 1 : APP.longIndex;
                    var childLi = '<li>' + id + ':   ' + appMsg.message + ' [Latency=' + latency + ' seconds]' + '</li>';
                    msgList.append(childLi);
                });


                //Setup the next poll recursively
                longPoll();
            },
            error: function () {
                bootbox.alert("Error", function () {
                });
            },
            complete: function () {
                var node = $('#longCount');
                var count = node.data('count');
                count = parseInt(count) + 1;
                node.attr('data-count', count).text(count);
            }


        });
    }, APP.asyncTimeout);
})();

var create = function () {
    var newMsg = $('#postMsg').val();
    if (_.isEmpty(newMsg)) {
        bootbox.alert("The messsage cannot be empty.", function () {
        });
        return false;
    }

    $.ajax("/appMsgs", {
        type: "POST",
        data: {
            msg: newMsg
        },
        accepts: {
            text: "application/json"
        },
        success: function (data) {
            $.bootstrapGrowl("Added message: " + data.id + " ->" + data.message, {
                align: 'right',
                type: 'success',
                width: 'auto',
                offset: {from: 'bottom', amount: 20}, // 'top', or 'bottom'

            });
        },
        error: function () {
            bootbox.alert("Error", function () {
            });
        },
        complete: function () {
            $('#postMsg').val('');
        }

    });
};

var deleteMsgs = function () {
    $.ajax("/appMsgs", {
        type: "DELETE",
        accepts: {
            text: "application/json"
        },
        success: function (data) {
            $.bootstrapGrowl("Deleted " + data + ' messages.', {
                align: 'right',
                type: 'success',
                width: 'auto',
                offset: {from: 'bottom', amount: 20}, // 'top', or 'bottom'
            });
        },
        error: function () {
            bootbox.alert("Error", function () {
            });
        },
    });
};


var readAll = function () {
    $.ajax("/appMsgs", {
        type: "GET",
        accepts: {
            text: "application/json"
        },
        success: function (data) {
            console.dir(data);
            bootbox.alert(JSON.stringify(data), function () {
            });
        },
        error: function () {
            alert("Error");
        }
    });
};

$(document).ready(function () {
    $('#postBtn').click(function () {
        create();
    });
    $('#deleteBtn').click(function () {
        deleteMsgs();
    });
});

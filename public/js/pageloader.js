var current = 1;
function more(){
    current++;
    pageLoader(current, 10)
}
function pageLoader(page, pageSize){
    jsRoutes.controllers.Application.tickles(page, pageSize).ajax({
        success: function(data) {
            if (data.success) {
                var list = data.success.tickles;
                for(var i = 0; i < list.length; i++) {
                    $("#tickles").append('<div class="alert alert-success">' + list[i].text + '</div>')
                }
                $("#tickles").append('<div class="alert alert-success"><button id="more" class="btn">more</button></div>')
            }
            if (data.failure) {
                current--;
                emsg('Error retrieving information, reason: ' + data.failure.reason)
            }
        }
    });
}
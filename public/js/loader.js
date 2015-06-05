function pageLoader(page, pageSize){
    jsRoutes.controllers.Application.tickles(page, pageSize).ajax({
        success: function(data) {
            if (data.success) {
                var list = data.success.tickles;
                for(var i = 0; i < list.length; i++) {
                    $("#tickles").append('<div class="alert alert-success">' + list[i].text + '</div>')
                }
                return true;
            }
            if (data.failure) {
                emsg('Error retrieving information, reason: ' + data.failure.reason)
                return false;
            }
        }
    });
}
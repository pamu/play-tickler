function pageLoader(page, pageSize){
    jsRoutes.controllers.Application.tickles(page, pageSize).ajax({
        success: function(data) {
            if (data.success) {
                var list = data.success.tickles;
                for(var i = 0; i < list.length; i++) {
                    $("#tickles").append('<div class="alert alert-success">' + list[i].text + '</div>');
                }
            }
            if (data.failure) {
                emsg(data.failure);
            }
        }
    });
}
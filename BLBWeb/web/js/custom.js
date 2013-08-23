function logout(){
    $.ajax({
        url: "/BLBWeb/logout",
        success: function(){
            window.location.href = window.location.href;
        }
    });
}
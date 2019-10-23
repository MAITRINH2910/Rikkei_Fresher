new WOW().init();

function printError(elemId, hintMsg) {
    document.getElementById(elemId).innerHTML = hintMsg;
}

function validate() {

    if (document.f.username.value == "" && document.f.password.value == "") {
        printError("bothErr", "Username and Password are required");
        document.f.username.focus();
        return false;
    } else {
        printError("bothErr", "");
        document.f.username.focus();
        bothErr = false;
    }
    if (document.f.username.value == "") {
        printError("nameErr", "Please enter your username");
        document.f.username.focus();
        return false;
    } else {
        printError("nameErr", "");
        document.f.username.focus();
        nameErr = false;
    }
    if (document.f.password.value == "") {
        printError("pwErr", "Please enter your password");
        document.f.password.focus();
        return false;
    } else {
        printError("pwErr", "");
        document.f.password.focus();
        pwErr = false;
    }

}

$('.username').on('keyup', function () {
    //Check UserName existed when user type input
    if (this.value.length > 0) {
        var userName = this.value;
        //Check in DB system
        $.ajax({
            url: "/checkUserName",
            type: "POST",
            data: {
                userName: userName
            },
            success: function (value) {
                // If result = true, UserName exsts
                if (value == "true") {
                    // Message when UserName exists
                    $("#kqCheckName").text("User name exists in system !");
                    // Disable input in form sign up
                    var inputs = document.getElementsByClassName("disinput");
                    for (i = 0; i < inputs.length; i++) {
                        inputs[i].disabled = true;
                    }
                    // If result = false
                } else {
                    $("#kqCheckName").text("")
                    var inputs = document.getElementsByClassName("disinput");
                    for (i = 0; i < inputs.length; i++) {
                        inputs[i].disabled = false;
                    }
                }
            }
        })
    } else {
        $("#kqCheckName").text("");
    }
});

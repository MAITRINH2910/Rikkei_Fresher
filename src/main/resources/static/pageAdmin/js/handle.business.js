/**
 * Change Status User
 */
$(document).ready(function () {
    $('.checkbox1').change(function () {
        if ($(this).is(":checked")) {
            var returnVal = confirm("Do you want to enable this account?");
            if (returnVal) {
                var id = $(this).closest("tr").attr("data-id");
                $.ajax({
                    url: "/admin/edit-status-user",
                    type: "GET",
                    data: {
                        id: id,

                    },
                })
            } else {
                $(this).prop('checked', returnVal);
            }
        } else {
            var returnVal = confirm("Do you want to disable this account?");
            // $(this).attr("checked", returnVal);
            if (returnVal) {
                var id = $(this).closest("tr").attr("data-id");
                $.ajax({
                    url: "/admin/edit-status-user",
                    type: "GET",
                    data: {
                        id: id,
                    },
                })
            } else {
                $(this).prop('checked', returnVal);
                location.reload();
            }
        }
    });
})

/**
 * Change Role User
 */
$(document).ready(function () {
    $("select.role").change(function () {
        var selectedRole = $(this).children("option:selected").val();
        // $("#inputGroupSelect02").val(selectedRole);

        alert("You has changed ROLE of this account is " + selectedRole);
        var id = $(this).closest("tr").attr("data-id");
        console.log(id);
        $.ajax({
            url: "/admin/change-role",
            type: "GET",
            data: {
                id: id,
                role: selectedRole
            },
        })
    });
});


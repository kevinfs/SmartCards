$(function () {
    $("form").submit(function (event) {

        var password = $("input[name=password]").val();
        var sel = $("input[name=sel]").val();

        var v1 = md5(password + sel);

        event.preventDefault();

        var login = $("input[name=login]").val();

        $.post("/subscribe",
            {
                login: login,
                password: v1,
                sel: sel
            },
            function (data) {
                alert(data);
            }
        );
    });
});


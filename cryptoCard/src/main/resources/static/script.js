$(function () {
    $("form").submit(function (event) {

        var password = $("input[name=password]").val();
        var sel = $("input[name=sel]").val();
        var graine = $("input[name=graine]").val();

        var v1 = md5(password + sel);
        var v2 = md5(v1 + graine);

        event.preventDefault();

        var login = $("input[name=login]").val();

        $.post("/password",
            {
                login: login,
                password: v2,
                graine: graine
            },
            function (data) {
                alert(data);
            }
        );
    });
});


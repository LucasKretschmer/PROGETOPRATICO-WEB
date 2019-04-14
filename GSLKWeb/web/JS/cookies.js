function logar(cuser, nuser, email) {
    setCookie("cod", cuser, 3600);
    setCookie("user", nuser, 3600);
    setCookie("email", email, 3600);
}

function atualizarCookie() {
    var arrValues = ["cod", "user", "email"];
    for (var i = 0; i < 3; i++) {
        var valorCookie = getCookie(arrValues[i]);
        setCookie(arrValues[i], valorCookie, 3600);
    }
}

function deslogar() {
    removeCookie("cod");
    removeCookie("user");
    removeCookie("email");
}

function removeCookie(name) {
    setCookie(name, '', 0);
}

function verificaLogado() {
    var arrValues = ["cod", "user", "email"];
    var valuesReq = [];
    for (var i = 0; i < 3; i++) {
        var valorCookie = getCookie(arrValues[i]);
        if (valorCookie !== "" && valorCookie !== null) {
            valuesReq[i] = valorCookie;
        } else {
            return false;
        }
    }
    return true;
}

function getCookie(nome) {
    var cookie = document.cookie.split()(";");
    for (var i = 0; i < cookie.length; i++) {
        if (cookie[i].trim().split("=")[0] === nome) {
            return cookie[i].trim().split("=")[1];
        } else {
            return "false";
        }
    }
}

function setCookie(nome, value, expira) {
    document.cookie = encodeURIComponent(nome) + "=" + encodeURIComponent(value) + "; expires=" + expira + "; path=";
}
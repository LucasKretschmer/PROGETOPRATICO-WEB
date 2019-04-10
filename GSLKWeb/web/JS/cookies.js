function logar(cuser, nuser, email, senha) {
    window.coduser = cuser;
    window.nomeuser = nuser;
    setCookie("cod", window.coduser, 3600);
    setCookie("user", window.nomeuser, 3600);
    setCookie("email", email, 3600);
    setCookie("password", senha, 3600);
}

function atualizarCookie() {
    var arrValues = ["cod", "user", "email", "password"];
    for (var i = 0; i < 4; i++) {
        var valorCookie = getCookie(arrValues[i]);
        setCookie(arrValues[i], valorCookie, 3600);
    }
}

function deslogar() {
    removeCookie("cod");
    removeCookie("user");
    removeCookie("email");
    removeCookie("password");
}

function removeCookie(name) {
    setCookie(name, '', 0);
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
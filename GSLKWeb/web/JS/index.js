// SCRIPT-GERAL //
function init() {
    document.querySelector('.h1Entrar').addEventListener('click', getTela);
    document.querySelector('.h1Cadastrese').addEventListener('click', getTela);
    document.querySelector('#btnFechaTelaLoguin').addEventListener('click', fecharTela);
    document.querySelectorAll('#header_btnEntrar')[0].addEventListener('click', abrirTela);
    document.querySelectorAll('#header_btnEntrar')[1].addEventListener('click', abrirTela);
    document.querySelectorAll("#header_btnHome")[0].addEventListener('click', irHome);
    document.querySelectorAll("#header_btnHome")[1].addEventListener('click', irHome);
    document.querySelectorAll("#header_btnContato")[0].addEventListener('click', irContato);
    document.querySelectorAll("#header_btnContato")[1].addEventListener('click', irContato);
    document.querySelector("#btnEntrar").addEventListener('click', fazerLogin);
    document.querySelector("#btnCadastrar").addEventListener('click', fazerCadastro);

    setInterval(function () {
        document.querySelector(".next").click();
    }, 3500);
    //projeto, classe, metodo, funcaoOK, funcaoErro, parametros
    executaServico("GSLKJava", "planos", "retornarPlanos",
            function (data) {
                if (data[0].STATUS === "TRUE") {
                    montaProdutos(data);
                } else {
                    alert("Não existem itens cadastrados para serem listados :)");
                }
            }, function (erro) {
        alert("Vish não consegui fazer a requisição, alguem chama um programador por favor? Aconteceu o erro:" + erro);

    }, "");

}

function montaProdutos(data) {
    for (var l = 1; l < data.length; l++) {
        var linhas = '<hr style="border-color: darkseagreen;">'
                + '<div class="main-planos-container">'
                + '    <div class="hexagono hexagono-medidas">'
                + '        <div class="hexagono-div1">'
                + '            <div class="hexagono-div2">'
                + '                <span> R$' + data[l].VALOR + '</span>'
                + '            </div>'
                + '        </div>'
                + '    </div>'
                + '    <div class="main-planos-mid">'
                + '        <h1>' + data[l].NOME + '</h1>'
                + '        <p>' + data[l].DESC + '</p><br>'
                + '        <p>Quantidade de pessoas que o plano comporta: ' + data[l].QTDEPESSU + '</p>'
                + '    </div>'
                + '    <div class="main-planos-button ' + data[l].COD + '"><span>Assine já</span></div>'
                + '</div>';
        document.querySelector("#main-planos").innerHTML += linhas;
    }
}

window.onscroll = function () {
    if (document.documentElement.scrollTop >= 200) {
        document.querySelector(".header-mid-fixo").classList.add("header-mid-fixo-visivel");
    } else {
        document.querySelector(".header-mid-fixo").classList.remove("header-mid-fixo-visivel");
    }
};

function executaServico(projeto, classe, metodo, funcaoOK, funcaoErro, parametros) {
    var http = new XMLHttpRequest();
    http.open('POST', 'http://portal.tecnicon.com.br:7078/TecniconPCHttp/ConexaoHttp?p=evento=ERPMetodos|sessao=|empresa=|filial=|local=|parametro=' +
            'projeto=' + projeto + '|classe=' + classe + '|metodo=' + metodo + '|recurso=metadados' + parametros, true);

    http.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    http.addEventListener('load', function () {
        if (http.status === 200) {
            var dados = xmlToJSON(http.responseXML);
            if (dados.erro) {
                funcaoErro(dados.erro);
            } else if (dados.result) {
                funcaoOK(dados.result);
            }
        }
    });
    http.send(null);
}

function xmlToJSON(XMLDocument) {
    var retorno = {result: XMLDocument.getElementsByTagName('result')[0].textContent,
        erro: XMLDocument.getElementsByTagName('erro')[0].textContent};
    try {
        retorno.result = JSON.parse(retorno.result);
    } catch (e) {
    }
    try {
        retorno.erro = JSON.parse(retorno.erro);
    } catch (e) {
    }
    return retorno;
}

function serializeForm(idForm, classCampos) {
    var arrCampos = document.querySelectorAll('#' + idForm + ' .' + classCampos);
    var arrParams = [], i, qtde;
    for (i = 0, qtde = arrCampos.length; i < qtde; i++) {
        arrParams.push(arrCampos[i].id + '=' + encodeURIComponent(arrCampos[i].value));
    }
    return '&' + arrParams.join('&');
}

function fazerLogin() {
    var email = document.querySelector("#email").value;
    var senha = document.querySelector("#senha").value;

    var param = "&EMAIL=" + email + "&SENHA=" + senha;
    // projeto, classe, metodo, funcaoOK, funcaoErro, parametros
    executaServico("GSLKJava", "login", "fazerLogin", function (data) {
        alert(data);
        var jData = data;

        if (jData.STATUS === true) {
            document.querySelector("#email").value = "";
            document.querySelector("#senha").value = "";
            document.querySelector("#btnFechaTelaLoguin").click();
            document.querySelector("#nome-cliente").value = '<i class="fas fa-user-circle"></i> ' + jData.NOME;
            document.querySelector("#cod-cliente").value = jData.CCLIFOR;


        }


    }, function (erro) {
        alert(erro);
    }, param);
}

function fazerCadastro() {
    document.querySelector("");
    document.querySelector("");
    document.querySelector("");
    document.querySelector("");
}

function irHome(e) {
    document.querySelector(".contato").classList.remove("cont-visivel");
    document.querySelector(".contato").classList.add("cont-inVisivel");

    for (var i = 0; i < document.querySelectorAll(".index").length; i++) {
        document.querySelectorAll(".index")[i].classList.remove("cont-inVisivel");
        document.querySelectorAll(".index")[i].classList.add("cont-visivel");
    }
}

function irContato(e) {
    document.querySelector(".contato").classList.remove("cont-inVisivel");
    document.querySelector(".contato").classList.add("cont-visivel");

    for (var i = 0; i < document.querySelectorAll(".index").length; i++) {
        document.querySelectorAll(".index")[i].classList.add("cont-inVisivel");
        document.querySelectorAll(".index")[i].classList.remove("cont-visivel");
    }
}

function getTela(e) {
    document.querySelector('.visivel').classList.remove('visivel');
    document.querySelector('.selected').classList.remove('selected');
    e.target.classList.add('selected');
    document.querySelector('.' + e.target.dataset.settela).classList.add('visivel');
}

function fecharTela(e) {
    document.querySelector('.visivell').classList.remove('visivell');
    document.querySelector('.tampaBackBody').classList.remove('tampaBackBody');
}

function abrirTela(e) {
    document.querySelector('#tbody').classList.add('tampaBackBody');
    document.querySelector('#divEntrar').classList.add('visivell');
}


// SCRIPT-ESTILIZAÇÃO //
var slideIndex = 1;
showSlides(slideIndex);

function plusSlides(n) {
    showSlides(slideIndex += n);
}

function currentSlide(n) {
    showSlides(slideIndex = n);
}

function showSlides(n) {
    var i;
    var slides = document.getElementsByClassName("mySlides");
    var dots = document.getElementsByClassName("dot");
    if (n > slides.length) {
        slideIndex = 1;
    }
    if (n < 1) {
        slideIndex = slides.length;
    }
    for (i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";
    }
    for (i = 0; i < dots.length; i++) {
        dots[i].className = dots[i].className.replace(" active", "");
    }
    slides[slideIndex - 1].style.display = "block";
    dots[slideIndex - 1].className += " active";
}
init();
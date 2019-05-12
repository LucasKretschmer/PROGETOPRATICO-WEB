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
    document.querySelectorAll("#btn_usuario_conta_conta")[0].addEventListener('click', irConta);
    document.querySelectorAll("#btn_usuario_conta_conta")[1].addEventListener('click', irConta);
    document.querySelectorAll("#header_btnContato")[0].addEventListener('click', irContato);
    document.querySelectorAll("#header_btnContato")[1].addEventListener('click', irContato);
    document.querySelector("#btnEntrar").addEventListener('click', fazerLogin);
    document.querySelector("#listaOpcoes_fatura").addEventListener('click', mudaAbaOpcoesFatu);
    document.querySelector("#listaOpcoes_config").addEventListener('click', mudaAbaOpcoesConf);
    document.querySelector("#btn_editar_opcoes").addEventListener('click', editarOpcoes);
    document.querySelector("#btn_salvar_opcoes").addEventListener('click', salvarOpcoes);
    document.querySelector("#btn_editar_senha_opcoes").addEventListener('click', editarSenhaOpcoes);
    document.querySelector("#btn_salvar_senha_opcoes").addEventListener('click', salvarSenhaOpcoes);
    document.querySelector("#btnCadastrar").addEventListener('click', fazerCadastro);
    document.querySelectorAll("#btn_usuario_conta_sair")[0].addEventListener('click', fazerDeslogin);
    document.querySelectorAll("#btn_usuario_conta_sair")[1].addEventListener('click', fazerDeslogin);
    if (verificaLogado()) {
        var cod = getCookie("cod");
        if (cod !== "") {
// projeto, classe, metodo, funcaoOK, funcaoErro, parametros
            executaServico("GSLKJava", "login", "verificaLogado", function (data) {
                if (data.STATUS) {
                    var jData = data;
                    var arrNome = jData.NOME.split(" ");
                    var nomeUser = arrNome[0] + " " + (arrNome[arrNome.length - 1]).split("")[0];
                    document.querySelectorAll("#btn_usuario_conta")[0].classList.remove("user-inVisivel");
                    document.querySelectorAll("#btn_usuario_conta")[1].classList.remove("user-inVisivel");
                    document.querySelectorAll("#btn_usuario_conta_nome")[0].innerHTML = '<i class="fas fa-user"></i> ' + nomeUser;
                    document.querySelectorAll("#btn_usuario_conta_nome")[1].innerHTML = '<i class="fas fa-user"></i> ' + nomeUser;
                    document.querySelectorAll("#header_btnEntrar")[0].classList.add("user-inVisivel");
                    document.querySelectorAll("#header_btnEntrar")[1].classList.add("user-inVisivel");
                    logar(jData.CCLIFOR, nomeUser);
                }
            }, function (erro) {
                alert(erro);
            }, "&COD=" + cod + "&");
        }

    }


    setInterval(function () {
        document.querySelector(".next").click();
    }, 3500);
    //projeto, classe, metodo, funcaoOK, funcaoErro, parametros
    executaServico("GSLKJava", "planos", "retornarPlanos",
            function (data) {
                if (data[0].STATUS) {
                    montaProdutos(data);
                    var quantPlanos = document.querySelectorAll("#main-planos-button").length;
                    for (var k = 0; k < quantPlanos; k++) {
                        document.querySelectorAll("#main-planos-button")[k].addEventListener('click', contrataPlano);
                    }
                } else {
                    alert("Não existem itens cadastrados para serem listados :)");
                }
            }, function (erro) {
        alert("Vish não consegui fazer a requisição, alguem chama um programador por favor? Aconteceu o erro:" + erro);
    }, "");
}

function contrataPlano(e) {
    var codPromocao = e.target.classList.toString().split("_")[1];
    var cclifor = getCookie("cod");
    if (cclifor !== "" || cclifor !== null) {
        var parametros = "&CODUSER=" + cclifor + "&CODPROMOCAO=" + codPromocao + "&";
        //projeto, classe, metodo, funcaoOK, funcaoErro, parametros
        executaServico("GSLKJava", "contrataPlano", "contrataPlano",
                function (data) {
                    if (data[0].STATUS) {



                    } else {
                        alert("Você tem informações não preenchidas para poder fazer a sua assinatura!");
                    }
                }, function (erro) {
            alert("Vish não consegui fazer a requisição, alguem chama um programador por favor? \r\nErro:" + erro);
        }, parametros);
    } else {
        document.querySelector("#").click();
        alert("Você deve fazer o loguin antes de contratar um plano!");
    }
}

function salvarOpcoes() {
    document.querySelector("#nome").setAttribute("disabled", true);
    document.querySelector("#cep").setAttribute("disabled", true);
    document.querySelector("#emaill").setAttribute("disabled", true);
    document.querySelector("#endereco").setAttribute("disabled", true);
    document.querySelector("#bairro").setAttribute("disabled", true);
    document.querySelector("#cidade").setAttribute("disabled", true);
    document.querySelector("#telefone").setAttribute("disabled", true);
    document.querySelector("#celular").setAttribute("disabled", true);
    document.querySelector("#cpf").setAttribute("disabled", true);
}
function editarOpcoes() {
    var nome = document.querySelector("#nome").value;
    var emaill = document.querySelector("#emaill").value;
    var cep = document.querySelector("#cep").value;
    var endereco = document.querySelector("#endereco").value;
    var bairro = document.querySelector("#bairro").value;
    var cidade = document.querySelector("#cidade").value;
    var telefone = document.querySelector("#telefone").value;
    var celular = document.querySelector("#celular").value;
    var cpf = document.querySelector("#cpf").value;

    document.querySelector("#nome").removeAttribute("disabled");
    document.querySelector("#emaill").removeAttribute("disabled");
    document.querySelector("#cep").removeAttribute("disabled");
    document.querySelector("#endereco").removeAttribute("disabled");
    document.querySelector("#bairro").removeAttribute("disabled");
    document.querySelector("#cidade").removeAttribute("disabled");
    document.querySelector("#telefone").removeAttribute("disabled");
    document.querySelector("#celular").removeAttribute("disabled");
    document.querySelector("#cpf").removeAttribute("disabled");
}
function editarSenhaOpcoes() {
    document.querySelector("#emaill").removeAttribute("disabled");
    document.querySelector("#senhaa").removeAttribute("disabled");
    document.querySelector("#confSenha").removeAttribute("disabled");
}
function salvarSenhaOpcoes() {
    document.querySelector("#emaill").setAttribute("disabled", true);
    document.querySelector("#senhaa").setAttribute("disabled", true);
    document.querySelector("#confSenha").setAttribute("disabled", true);
}
function mudaAbaOpcoesFatu() {
    document.querySelector("#opcoes_fatura").classList.remove("cont-inVisivel");
    document.querySelector("#opcoes_config").classList.add("cont-inVisivel");
}

function mudaAbaOpcoesConf() {
    document.querySelector("#opcoes_fatura").classList.add("cont-inVisivel");
    document.querySelector("#opcoes_config").classList.remove("cont-inVisivel");
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
                + '    <div class="plano_' + data[l].COD + '" id="main-planos-button"><span>Assine já</span></div>'
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

function fazerDeslogin() {
    document.querySelectorAll("#header_btnEntrar")[0].classList.remove("user-inVisivel");
    document.querySelectorAll("#header_btnEntrar")[1].classList.remove("user-inVisivel");
    document.querySelectorAll("#btn_usuario_conta_nome")[0].innerHTML = '';
    document.querySelectorAll("#btn_usuario_conta_nome")[1].innerHTML = '';
    document.querySelectorAll("#btn_usuario_conta")[0].classList.add("user-inVisivel");
    document.querySelectorAll("#btn_usuario_conta")[1].classList.add("user-inVisivel");
    deslogar();
    document.querySelector("#header_btnHome").click();
}

function fazerLogin() {
    var email = document.querySelector("#email").value;
    var senha = document.querySelector("#senha").value;
    var param = "&EMAIL=" + email + "&SENHA=" + senha;
    // projeto, classe, metodo, funcaoOK, funcaoErro, parametros
    executaServico("GSLKJava", "login", "fazerLogin", function (data) {
        var jData = data;
        if (jData.STATUS) {
            var arrNome = jData.NOME.split(" ");
            var nomeUser = arrNome[0] + " " + (arrNome[arrNome.length - 1]).split("")[0];
            var email = document.querySelector("#email").value;
            document.querySelector("#email").value = "";
            document.querySelector("#senha").value = "";
            document.querySelector("#btnFechaTelaLoguin").click();
            document.querySelectorAll("#btn_usuario_conta_nome")[0].innerHTML = '<i class="fas fa-user"></i> ' + nomeUser;
            document.querySelectorAll("#btn_usuario_conta_nome")[1].innerHTML = '<i class="fas fa-user"></i> ' + nomeUser;
            document.querySelectorAll("#btn_usuario_conta")[0].classList.remove("user-inVisivel");
            document.querySelectorAll("#btn_usuario_conta")[1].classList.remove("user-inVisivel");
            document.querySelectorAll("#header_btnEntrar")[0].classList.add("user-inVisivel");
            document.querySelectorAll("#header_btnEntrar")[1].classList.add("user-inVisivel");
            logar(jData.CCLIFOR, nomeUser, email);
//            preencheDadosCliente(jData.CCLIFOR);
        }
    }, function (erro) {
        alert(erro);
    }, param);
}

function preencheDadosCliente(cod) {
// projeto, classe, metodo, funcaoOK, funcaoErro, parametros
    executaServico("GSLKJava", "buscaDados", "buscaDados", function (data) {
        var jData = data;
        document.querySelector("#cclifor").value = jData.CCLIFOR;
        document.querySelector("#dtCadastro").value = jData.DTCADASTRO;
        document.querySelector("#emaill").value = jData.EMAIL;
        document.querySelector("#nome").value = jData.NOME;

        if (jData.AVANCADO) {
            document.querySelector("#endereco").value = jData.ENDERECO;
            document.querySelector("#bairro").value = jData.BAIRRO;
            document.querySelector("#cep").value = jData.CEP;
            document.querySelector("#cidade").value = jData.CIDADE;
            document.querySelector("#telefone").value = jData.FONE;
            document.querySelector("#celular").value = jData.CELULAR;
            document.querySelector("#cpf").value = jData.CPF;
            document.querySelector("#ccontrato").value = jData.CCONTRATO;
            document.querySelector("#dataContrato").value = jData.DATACONTRATO;
            document.querySelector("#valor").value = jData.VALOR;
            document.querySelector("#nomePlano").value = jData.NOMEPLANO;
            document.querySelector("#nomePagamento").value = jData.NOMEPAGAMENTO;
            document.querySelector("#qtdedias").value = jData.QTDEDIAS;
            document.querySelector("#qtdePessoas").value = jData.QTDEDIAS;

        }

    }, function (erro) {
        alert(erro);
    }, "&COD=" + cod + "&");
}

function fazerCadastro() {
    var nome = document.querySelector("#cadastrar-nome").value;
    var email = document.querySelector("#cadastrar-email").value;
    var senha = document.querySelector("#cadastrar-senha").value;
    var confSenha = document.querySelector("#cadastrar-confsenha").value;
    var parametro = "&NOME=" + nome + "&EMAIL=" + email + "&SENHA=" + senha + "&CONFSENHA=" + confSenha;
    // projeto, classe, metodo, funcaoOK, funcaoErro, parametros
    executaServico("GSLKJava", "login", "fazerCadastro", function (data) {
        var jData = data;
        if (jData.STATUS) {
            var arrNome = jData.NOME.split(" ");
            var nomeUser = arrNome[0] + " " + (arrNome[arrNome.length - 1]).split("")[0];
            var email = jData.EMAIL;
            document.querySelector("#cadastrar-nome").value = "";
            document.querySelector("#cadastrar-email").value = "";
            document.querySelector("#cadastrar-senha").value = "";
            document.querySelector("#cadastrar-confsenha").value = "";
            document.querySelector("#btnFechaTelaLoguin").click();
            document.querySelectorAll("#btn_usuario_conta_nome")[0].innerHTML = '<i class="fas fa-user"></i> ' + nomeUser;
            document.querySelectorAll("#btn_usuario_conta_nome")[1].innerHTML = '<i class="fas fa-user"></i> ' + nomeUser;
            document.querySelectorAll("#btn_usuario_conta")[0].classList.remove("user-inVisivel");
            document.querySelectorAll("#btn_usuario_conta")[1].classList.remove("user-inVisivel");
            document.querySelectorAll("#header_btnEntrar")[0].classList.add("user-inVisivel");
            document.querySelectorAll("#header_btnEntrar")[1].classList.add("user-inVisivel");
            logar(jData.CCLIFOR, nomeUser, email);
        } else {
            alert(data);
        }
    }, function (erro) {
        alert(erro);
    }, parametro);
}

function irHome(e) {
    document.querySelector(".contato").classList.remove("cont-visivel");
    document.querySelector(".contato").classList.add("cont-inVisivel");
    document.querySelector(".minhaConta").classList.remove("cont-visivel");
    document.querySelector(".minhaConta").classList.add("cont-inVisivel");
    for (var i = 0; i < document.querySelectorAll(".index").length; i++) {
        document.querySelectorAll(".index")[i].classList.remove("cont-inVisivel");
        document.querySelectorAll(".index")[i].classList.add("cont-visivel");
    }
}

function irContato(e) {
    document.querySelector(".contato").classList.remove("cont-inVisivel");
    document.querySelector(".contato").classList.add("cont-visivel");
    document.querySelector(".minhaConta").classList.remove("cont-visivel");
    document.querySelector(".minhaConta").classList.add("cont-inVisivel");
    for (var i = 0; i < document.querySelectorAll(".index").length; i++) {
        document.querySelectorAll(".index")[i].classList.add("cont-inVisivel");
        document.querySelectorAll(".index")[i].classList.remove("cont-visivel");
    }
}

function irConta(e) {
    document.querySelectorAll(".index")[0].classList.remove("cont-visivel");
    document.querySelectorAll(".index")[0].classList.add("cont-inVisivel");
    document.querySelectorAll(".index")[1].classList.remove("cont-visivel");
    document.querySelectorAll(".index")[1].classList.add("cont-inVisivel");
    document.querySelector(".contato").classList.remove("cont-visivel");
    document.querySelector(".contato").classList.add("cont-inVisivel");
    document.querySelector(".minhaConta").classList.add("cont-visivel");
    document.querySelector(".minhaConta").classList.remove("cont-inVisivel");
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
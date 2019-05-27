/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacoteplanos;

import br.com.tecnicon.enviaemail.TEnviarEmail;
import br.com.tecnicon.server.context.TClassLoader;
import br.com.tecnicon.server.dataset.TClientDataSet;
import br.com.tecnicon.server.execoes.ExcecaoTecnicon;
import br.com.tecnicon.server.model.EmailConfig;
import br.com.tecnicon.server.sessao.VariavelSessao;
import br.com.tecnicon.server.util.funcoes.Funcoes;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;
import org.json.JSONObject;

/**
 *
 * @author Lucas Kretschmer
 */
@Stateless
public class contrataPlano {

    public String contrataPlano(VariavelSessao vs) throws ExcecaoTecnicon {
        Funcoes.validaVSNNNome(vs, new String[]{
            "CUSER", "Código do Cliente"
        }, new String[]{
            "CPLANO", "Código do Plano"
        }, new String[]{
            "CPAGAMENTO", "Código do Tipo de Pagamento"
        }, new String[]{
            "DATA", "Data Atual"
        });

        JSONObject jData = new JSONObject();
        double valorPlano;

        TClientDataSet cdsCliforend = TClientDataSet.create(vs, "CLIFOREND");
        cdsCliforend.createDataSet();
        cdsCliforend.condicao("WHERE CLIFOREND.CCLIFOR = " + vs.getParameter("CUSER"));
        cdsCliforend.open();

        if (!cdsCliforend.isEmpty()) {
            jData.put("STATUS", true);

            TClientDataSet cdsPlano = TClientDataSet.create(vs, "GSPLANO");
            cdsPlano.createDataSet();
            cdsPlano.condicao("WHERE GSPLANO.CPLANO = " + vs.getParameter("CPLANO"));
            cdsPlano.open();

            TClientDataSet cdsPagamento = TClientDataSet.create(vs, "GSPAGAMENTO");
            cdsPagamento.createDataSet();
            cdsPagamento.condicao("WHERE GSPAGAMENTO.CPAGAMENTO = " + Funcoes.strToInt(vs.getParameter("CPAGAMENTO")));
            cdsPagamento.open();

            valorPlano = Funcoes.divideDouble(cdsPlano.fieldByName("VALORMES").asDouble(), 30d, 2, "A");
            valorPlano = Funcoes.multiplDouble(valorPlano, cdsPagamento.fieldByName("QTDEDIAS").asDouble(), 2, "A");

            TClientDataSet cdsContrato = TClientDataSet.create(vs, "GSCONTRATO1");
            cdsContrato.createDataSet();
            cdsContrato.insert();
            cdsContrato.fieldByName("CPLANO").asInteger(Funcoes.strToInt(vs.getParameter("CPLANO")));
            cdsContrato.fieldByName("CCLIFOR").asInteger(cdsCliforend.fieldByName("CCLIFOR").asInteger());
            cdsContrato.fieldByName("CPAGAMENTO").asInteger(Funcoes.strToInt(vs.getParameter("CPAGAMENTO")));
            cdsContrato.fieldByName("VALOR").asDouble(valorPlano);
            cdsContrato.fieldByName("DATA").asDate(Funcoes.strToDate(vs, vs.getParameter("DATA")));
            cdsContrato.post();

            return jData.put("MSG", "Sua assinatura foi concluída com sucesso!\r\nConfira seu email com as informações do plano contratado...").toString();
        } else {
            jData.put("STATUS", false);
            jData.put("MSG", "Existem algumas informações inconsistentes em seu cadastro, \r\nverifique as configurações e tente novamente!");
            return jData.toString();
        }
    }

    public String teste01(VariavelSessao vs) throws ExcecaoTecnicon {
        envioEmail(vs, "Titulo teste", "Email Teste, vamos ver em!", "lukretschmer15@gmail.com", "Titulo só pra ver msm! \n Verifique seu E-Mail!");

        return "Bem loko";
    }

    public void envioEmail(VariavelSessao vs, String titulo, String mensagem, String destinatario, String msgRetorno) throws ExcecaoTecnicon {
        TClientDataSet usuarioEmail = TClientDataSet.create(vs, "USUARIOEMAIL");
        usuarioEmail.createDataSet();
        usuarioEmail.condicao("WHERE USUARIOEMAIL.CUSUARIO = 27 AND USUARIOEMAIL.SUSUARIOEMAIL = 8"); //Setado fixo pois nao temos de onde pegar
        usuarioEmail.open();
        StringBuilder textAdd = new StringBuilder();
        textAdd.append(" \n Att. CampoNovo \n");
        textAdd.append("<img class=\"img-logo\" id=\"index\"   style=\"width: 210px;\" src=\"http://portal.tecnicon.com.br:7078/GSLKWeb/img/logomarca.png\">");
        String content = mensagem + textAdd.toString();
        Map<String, byte[]> anexos = new HashMap<>();
        Map<String, String> inlineImages = new HashMap<>();
        EmailConfig config = new EmailConfig(usuarioEmail.fieldByName("USUARIO").asString(), usuarioEmail.fieldByName("SENHA").asString(),
                usuarioEmail.fieldByName("HOSTSMTP").asString(), usuarioEmail.fieldByName("EMAIL").asString(),
                usuarioEmail.fieldByName("NOME").asString(), "", "", usuarioEmail.fieldByName("PORTSMTP").asInteger(),
                usuarioEmail.fieldByName("SSL").asString(), 17, 0);
        TEnviarEmail email = new TEnviarEmail();
        email.enviarEmail(destinatario,
                "",
                "",
                titulo,
                content,
                config,
                anexos,
                false,
                inlineImages);
        vs.setRetornoOK(msgRetorno);
    }

    private void enviarBoleto(VariavelSessao vs, int pessoa, double valor, String duplicata, String obs, int parcela, int totParcelas, Date dtVenc) throws ExcecaoTecnicon {
        if (!Funcoes.varIsNull(pessoa)) {
            TClientDataSet receber = TClientDataSet.create(vs, "RECEBER");
            receber.createDataSet();
            receber.insert();
            receber.fieldByName("DATA").asDate(new Date());
            receber.fieldByName("CFILIAL").asInteger(1);
            receber.fieldByName("CCLIFOR").asInteger(pessoa);
            receber.fieldByName("FILIALCF").asInteger(1);
            receber.fieldByName("CCARTEIRA").asInteger(1);
            receber.fieldByName("DUPLICATA").asString(duplicata);
            receber.fieldByName("PARCELA").asString(parcela + "/" + totParcelas);
            receber.fieldByName("VCTO").asDate(dtVenc);
            receber.fieldByName("VCTOP").asDate(dtVenc);
            receber.fieldByName("VALOR").asDouble(valor);
            receber.fieldByName("OBS").asString(obs);
            receber.post();

            vs.addParametros("filial", "1");
            vs.addParametros("cusuario", "27");
            vs.addParametros("empresa", "17");
            vs.addParametros("usuario", "CFJL.LUCAS");
            vs.addParametros("CCARTEIRA", "8");
            vs.addParametros("cbMsgDescVcto", "false");
            vs.addParametros("CUSTOBLOQUETO", receber.fieldByName("VALOR").asString());
            vs.addParametros("SRECEBER", receber.fieldByName("SRECEBER").asString());

            try {
                TClassLoader.execMethod("BloquetoImprime/BloquetoImprime", "enviarSelecionados", vs);

            } catch (ExcecaoTecnicon ex) {
                throw new ExcecaoTecnicon(vs, ex.getMessage());
            }

        }
    }

}

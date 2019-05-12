/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import br.com.tecnicon.server.dataset.TClientDataSet;
import br.com.tecnicon.server.dataset.TSQLDataSetEmp;
import br.com.tecnicon.server.execoes.ExcecaoTecnicon;
import br.com.tecnicon.server.sessao.VariavelSessao;
import br.com.tecnicon.server.util.funcoes.Funcoes;
import javax.ejb.Stateless;
import org.json.JSONObject;

/**
 *
 * @author Lucas Kretschmer
 */
@Stateless
public class buscaDados {

    public String buscaDados(VariavelSessao vs) throws ExcecaoTecnicon {
        Funcoes.validaVSNNNome(vs, new String[]{
            "COD", "Código do Usuário"
        });

        JSONObject jData = new JSONObject();

        TClientDataSet cdsAcesso = TClientDataSet.create(vs, "GSACESSO");
        cdsAcesso.createDataSet();
        cdsAcesso.condicao("WHERE GSACESSO.CCLIFOR = " + vs.getParameter("COD"));
        cdsAcesso.open();

        TClientDataSet cdsCliforend = TClientDataSet.create(vs, "CLIFOREND");
        cdsCliforend.createDataSet();
        cdsCliforend.condicao("WHERE CLIFOREND.CCLIFOR = " + vs.getParameter("COD"));
        cdsCliforend.open();

        TClientDataSet cdsClifor = TClientDataSet.create(vs, "CLIFOR");
        cdsClifor.createDataSet();
        cdsClifor.condicao("WHERE CLIFOR.CCLIFOR = " + vs.getParameter("COD"));
        cdsClifor.open();

        jData.put("CCLIFOR", cdsClifor.fieldByName("CCLIFOR").asInteger());
        jData.put("NOME", cdsClifor.fieldByName("NOME").asString());
        jData.put("DTCADASTRO", cdsClifor.fieldByName("DTCADASTRO").asDate());
        jData.put("EMAIL", cdsAcesso.fieldByName("EMAIL").asString());

        if (!cdsCliforend.isEmpty()) {
            TSQLDataSetEmp cteContrato = TSQLDataSetEmp.create(vs);
            cteContrato.createDataSet();
            cteContrato.commandText(new StringBuilder()
                    .append(" SELECT GSCONTRATO.CCONTRATO,")
                    .append("        GSCONTRATO.DATA,")
                    .append("        GSCONTRATO.VALOR,")
                    .append("        GSPLANO.NOME AS NOMEPLANO,")
                    .append("        GSPAGAMENTO.NOME AS NOMEPAGAMENTO,")
                    .append("        GSPAGAMENTO.QTDEDIAS ")
                    .append(" FROM GSCONTRATO ")
                    .append(" INNER JOIN GSPLANO ON (GSPLANO.CPLANO = GSCONTRATO.CPLANO)")
                    .append(" INNER JOIN GSPAGAMENTO ON (GSPAGAMENTO.CPLANO = GSCONTRATO.CPAGAMENTO)")
                    .append(" WHERE GSCONTRATO.CCLIFOREND = ").append(cdsCliforend.fieldByName("CCLIFOREND"))
                    .toString());
            cteContrato.open();

            jData.put("AVANCADO", true);
            jData.put("ENDERECO", cdsCliforend.fieldByName("ENDERECO").asString());
            jData.put("BAIRRO", cdsCliforend.fieldByName("BAIRRO").asString());
            jData.put("CEP", cdsCliforend.fieldByName("CEP").asString());
            jData.put("CIDADE", cdsCliforend.fieldByName("CIDADE").asString());
            jData.put("FONE", cdsCliforend.fieldByName("FONE").asInteger());
            jData.put("CELULAR", cdsCliforend.fieldByName("CELULAR").asInteger());
            jData.put("CCONTRATO", cteContrato.fieldByName("CCONTRATO").asInteger());
            jData.put("DATACONTRATO", cteContrato.fieldByName("DATA").asString());
            jData.put("VALOR", cteContrato.fieldByName("VALOR").asDouble());
            jData.put("NOMEPLANO", cteContrato.fieldByName("NOMEPLANO").asString());
            jData.put("NOMEPAGAMENTO", cteContrato.fieldByName("NOMEPAGAMENTO").asString());
            jData.put("QTDEDIAS", cteContrato.fieldByName("QTDEDIAS").asInteger());

            return jData.toString();
        }

        jData.put("AVANCADO", false);
        return jData.toString();
    }
}

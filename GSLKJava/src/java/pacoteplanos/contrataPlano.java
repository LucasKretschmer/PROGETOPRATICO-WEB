/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacoteplanos;

import br.com.tecnicon.server.dataset.TClientDataSet;
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
public class contrataPlano {

    public String contrataPlano(VariavelSessao vs) throws ExcecaoTecnicon {
        Funcoes.validaVSNNNome(vs, new String[]{
            "CCLIENTE", "Código do Cliente"
        }, new String[]{
            "CPLANO", "Código do Plano"
        }, new String[]{
            "CPAGAMENTO", "Código do Tipo de Pagamento"
        });

        JSONObject jData = new JSONObject();
        double valorPlano;

        TClientDataSet cdsCliforend = TClientDataSet.create(vs, "CLIFOREND");
        cdsCliforend.createDataSet();
        cdsCliforend.condicao("WHERE CCLIFOR = " + vs.getParameter("CCLIENTE"));
        cdsCliforend.open();

        if (!cdsCliforend.isEmpty()) {
            jData.put("STATUS", true);

            TClientDataSet cdsPlano = TClientDataSet.create(vs, "GSPLANO");
            cdsPlano.createDataSet();
            cdsPlano.condicao("WHERE CPLANO = " + vs.getParameter("CPLANO"));
            cdsPlano.open();

            TClientDataSet cdsPagamento = TClientDataSet.create(vs, "GSPAGAMENTO");
            cdsPlano.createDataSet();
            cdsPlano.condicao("WHERE CPAGAMENTO = " + vs.getParameter("CPAGAMENTO"));
            cdsPlano.open();

            valorPlano = Funcoes.divideDouble(cdsPlano.fieldByName("VALORMES").asDouble(), 30d, 2, "A");
            valorPlano = Funcoes.multiplDouble(valorPlano, cdsPagamento.fieldByName("QTDEDIAS").asDouble(), 2, "A");

            TClientDataSet cdsContrato = TClientDataSet.create(vs, "CONTRATO");
            cdsContrato.createDataSet();
            cdsContrato.insert();
            cdsContrato.fieldByName("CPLANO").asInteger(Funcoes.strToInt(vs.getParameter("CPLANO")));
            cdsContrato.fieldByName("CCLIFOREND").asInteger(cdsCliforend.fieldByName("CCLIFOREND").asInteger());
            cdsContrato.fieldByName("CPAGAMENTO").asInteger(Funcoes.strToInt(vs.getParameter("CPAGAMENTO")));
            cdsContrato.fieldByName("VALOR").asDouble(valorPlano);
            cdsContrato.post();

            return jData.put("MSG", "Contrato foi inserido com sucesso!\r\n Confira seu email com as informações do plano contratado...").toString();
        } else {
            return jData.put("STATUS", false).toString();
        }

    }

}

package boletos;

import br.com.tecnicon.server.dataset.TSQLDataSetEmp;
import br.com.tecnicon.server.execoes.ExcecaoTecnicon;
import br.com.tecnicon.server.sessao.VariavelSessao;
import br.com.tecnicon.server.util.funcoes.Funcoes;
import java.util.Date;
import javax.ejb.Stateless;
import org.json.JSONObject;

/**
 *
 * @author Lucas Kretschmer
 */
@Stateless
public class boletos {

    public String retornaDuplicatas(VariavelSessao vs) throws ExcecaoTecnicon {
        String dadosCli = vs.getParameter("CCLIFOR");
        JSONObject obj = new JSONObject();
        if (!dadosCli.equals("")) {
            obj.put("STATUS", "OK");
            StringBuilder html = new StringBuilder();
            TSQLDataSetEmp dupl = TSQLDataSetEmp.create(vs);
            dupl.commandText("SELECT "
                    + "	RECEBER.SRECEBER, "
                    + "	RECEBER.DUPLICATA, "
                    + "	RECEBER.DATA, "
                    + "	RECEBER.VCTO, "
                    + "	LOTEBXRECEBER.DATA AS DTPAGAMENTO, "
                    + "	CASE WHEN (COALESCE(TDATEDIFF(COALESCE(LOTEBXRECEBER.DATA,TCURRENT_DATE()),RECEBER.VCTO),0)) < 0 THEN 0 ELSE COALESCE(TDATEDIFF(COALESCE(LOTEBXRECEBER.DATA,TCURRENT_DATE()),RECEBER.VCTO),0) END AS DIASATRASO, "
                    + "	BXRECEBER.SPAGO, "
                    + "	RECEBER.PARCELA, "
                    + "	TPBXCR.NOME, "
                    + "	LOTEBXRECEBER.LOTEBXREC, "
                    + "	LOTEBXRECEBER.CFILIAL, "
                    + "	BXRECEBER.SJURO, "
                    + "	BXRECEBER.SDESPESA, "
                    + "	BXRECEBER.SDESCONTO"
                    + "FROM RECEBER "
                    + "LEFT JOIN BXRECEBER BX ON (BX.sreceber = RECEBER.sreceber) "
                    + "LEFT JOIN NFSAIDA ON (NFSAIDA.NFS = RECEBER.NFS) "
                    + "LEFT JOIN LOTEBXRECEBER ON (LOTEBXRECEBER.lotebxrec = BX.lotebxrec) "
                    + "LEFT JOIN TPBXCR ON (TPBXCR.CTPBXCR = LOTEBXRECEBER.CTPBXCR) "
                    + "LEFT JOIN CLIFOR ON (RECEBER.CCLIFOR = CLIFOR.CCLIFOR) "
                    + "LEFT JOIN(SELECT "
                    + "        BXRECEBER.LOTEBXREC, "
                    + "        SUM(BXRECEBER.VALOR) SVALOR, "
                    + "        SUM(BXRECEBER.JURO) SJURO, "
                    + "        SUM(BXRECEBER.DESCONTO) SDESCONTO, "
                    + "        SUM(BXRECEBER.DESPESA) SDESPESA, "
                    + "        SUM(BXRECEBER.TARIFA) STARIFA, "
                    + "        SUM(BXRECEBER.ADIANTAMENTO) SADIANTAMENTO, "
                    + "        SUM(BXRECEBER.PISRETIDO) SPISRETIDO, "
                    + "        SUM(BXRECEBER.COFINSRETIDO) SCOFINSRETIDO, "
                    + "        SUM(BXRECEBER.PAGO) SPAGO "
                    + "    FROM BXRECEBER "
                    + "    GROUP BY BXRECEBER.LOTEBXREC) BXRECEBER ON (BXRECEBER.LOTEBXREC = LOTEBXRECEBER.LOTEBXREC) "
                    + "WHERE CLIFOR.CCLIFOR = " + dadosCli
                    + "AND RECEBER.CFILIAL = 1 "
                    + " AND RECEBER.VCTO >= '" + Funcoes.formatarDB(Funcoes.dateToStr(new Date()), "D") + "'"
                    + "AND COALESCE(BXRECEBER.SPAGO,0) = 0 "
                    + "GROUP BY RECEBER.DUPLICATA, COALESCE(NFSAIDA.NF,NFSAIDA.NF1),RECEBER.DATA,RECEBER.VCTO, "
                    + "RECEBER.VCTOP,RECEBER.DATA,BXRECEBER.LOTEBXREC,LOTEBXRECEBER.DATA,RECEBER.PARCELA, "
                    + "TPBXCR.NOME, LOTEBXRECEBER.LOTEBXREC,LOTEBXRECEBER.CFILIAL,BXRECEBER.SPAGO,BXRECEBER.SJURO,BXRECEBER.SDESPESA, "
                    + "BXRECEBER.SDESCONTO,RECEBER.SRECEBER "
                    + "ORDER BY RECEBER.VCTO ");
            dupl.open();
            dupl.first();
            while (!dupl.eof()) {
                html.append("<tr>\n"
                        + "  <td class=\"TMcod\">" + dupl.fieldByName("SRECEBER").asString() + "</td>\n"
                        + "  <td class=\"TMDtVencimento\">" + dupl.fieldByName("VCTO").asString() + "</td>\n"
                        + "  <td class=\"TMStatus\"><span class=\"pendente\">Pendente</span></td>\n"
                        + "  <td class=\"TMDtLiquidado\">" + dupl.fieldByName("DTPAGAMENTO").asString() + "</td>\n"
                        + "  <td class=\"TMDescricao\"> is simply dummy text of the printing and typesetting industry. Lorem Ipsum has</td>\n"
                        + "</tr>");
                dupl.next();
            }
            obj.put("PENDENTES", html.toString());
            html.setLength(0);
            dupl.close();
            //Pagas
            dupl.commandText("SELECT "
                    + "	RECEBER.SRECEBER, "
                    + "	RECEBER.DUPLICATA, "
                    + "	RECEBER.DATA, "
                    + "	RECEBER.VCTO, "
                    + "	LOTEBXRECEBER.DATA AS DTPAGAMENTO, "
                    + "	CASE WHEN (COALESCE(TDATEDIFF(COALESCE(LOTEBXRECEBER.DATA,TCURRENT_DATE()),RECEBER.VCTO),0)) < 0 THEN 0 ELSE COALESCE(TDATEDIFF(COALESCE(LOTEBXRECEBER.DATA,TCURRENT_DATE()),RECEBER.VCTO),0) END AS DIASATRASO, "
                    + "	BXRECEBER.SPAGO, "
                    + "	RECEBER.PARCELA, "
                    + "	TPBXCR.NOME, "
                    + "	LOTEBXRECEBER.LOTEBXREC, "
                    + "	LOTEBXRECEBER.CFILIAL, "
                    + "	BXRECEBER.SJURO, "
                    + "	BXRECEBER.SDESPESA, "
                    + "	BXRECEBER.SDESCONTO"
                    + "FROM RECEBER "
                    + "LEFT JOIN BXRECEBER BX ON (BX.sreceber = RECEBER.sreceber) "
                    + "LEFT JOIN NFSAIDA ON (NFSAIDA.NFS = RECEBER.NFS) "
                    + "LEFT JOIN LOTEBXRECEBER ON (LOTEBXRECEBER.lotebxrec = BX.lotebxrec) "
                    + "LEFT JOIN TPBXCR ON (TPBXCR.CTPBXCR = LOTEBXRECEBER.CTPBXCR) "
                    + "LEFT JOIN CLIFOR ON (RECEBER.CCLIFOR = CLIFOR.CCLIFOR) "
                    + "LEFT JOIN(SELECT "
                    + "        BXRECEBER.LOTEBXREC, "
                    + "        SUM(BXRECEBER.VALOR) SVALOR, "
                    + "        SUM(BXRECEBER.JURO) SJURO, "
                    + "        SUM(BXRECEBER.DESCONTO) SDESCONTO, "
                    + "        SUM(BXRECEBER.DESPESA) SDESPESA, "
                    + "        SUM(BXRECEBER.TARIFA) STARIFA, "
                    + "        SUM(BXRECEBER.ADIANTAMENTO) SADIANTAMENTO, "
                    + "        SUM(BXRECEBER.PISRETIDO) SPISRETIDO, "
                    + "        SUM(BXRECEBER.COFINSRETIDO) SCOFINSRETIDO, "
                    + "        SUM(BXRECEBER.PAGO) SPAGO "
                    + "    FROM BXRECEBER "
                    + "    GROUP BY BXRECEBER.LOTEBXREC) BXRECEBER ON (BXRECEBER.LOTEBXREC = LOTEBXRECEBER.LOTEBXREC) "
                    + "WHERE CLIFOR.CCLIFOR = " + dadosCli
                    + "AND RECEBER.CFILIAL = 1 "
                    + " AND RECEBER.VCTO >= '" + Funcoes.formatarDB(Funcoes.dateToStr(new Date()), "D") + "'"
                    + "AND COALESCE(BXRECEBER.SPAGO,0) <> 0 "
                    + "GROUP BY RECEBER.DUPLICATA, COALESCE(NFSAIDA.NF,NFSAIDA.NF1),RECEBER.DATA,RECEBER.VCTO, "
                    + "RECEBER.VCTOP,RECEBER.DATA,BXRECEBER.LOTEBXREC,LOTEBXRECEBER.DATA,RECEBER.PARCELA, "
                    + "TPBXCR.NOME, LOTEBXRECEBER.LOTEBXREC,LOTEBXRECEBER.CFILIAL,BXRECEBER.SPAGO,BXRECEBER.SJURO,BXRECEBER.SDESPESA, "
                    + "BXRECEBER.SDESCONTO,RECEBER.SRECEBER "
                    + "ORDER BY RECEBER.VCTO ");
            dupl.open();
            dupl.first();
            while (!dupl.eof()) {
                html.append("<tr>\n"
                        + "  <td class=\"TMcod\">" + dupl.fieldByName("SRECEBER").asString() + "</td>\n"
                        + "  <td class=\"TMDtVencimento\">" + dupl.fieldByName("VCTO").asString() + "</td>\n"
                        + "  <td class=\"TMStatus\"><span class=\"lquidado\">Liquidado</span></td>\n"
                        + "  <td class=\"TMDtLiquidado\">" + dupl.fieldByName("DTPAGAMENTO").asString() + "</td>\n"
                        + "  <td class=\"TMDescricao\"></td>\n"
                        + "</tr>");
                dupl.next();
            }
            obj.put("PAGAS", html.toString());
            html.setLength(0);
            dupl.close();
            //Atrasadas
            dupl.commandText("SELECT "
                    + "	RECEBER.SRECEBER, "
                    + "	RECEBER.DUPLICATA, "
                    + "	RECEBER.DATA, "
                    + "	RECEBER.VCTO, "
                    + "	LOTEBXRECEBER.DATA AS DTPAGAMENTO, "
                    + "	CASE WHEN (COALESCE(TDATEDIFF(COALESCE(LOTEBXRECEBER.DATA,TCURRENT_DATE()),RECEBER.VCTO),0)) < 0 THEN 0 ELSE COALESCE(TDATEDIFF(COALESCE(LOTEBXRECEBER.DATA,TCURRENT_DATE()),RECEBER.VCTO),0) END AS DIASATRASO, "
                    + "	BXRECEBER.SPAGO, "
                    + "	RECEBER.PARCELA, "
                    + "	TPBXCR.NOME, "
                    + "	LOTEBXRECEBER.LOTEBXREC, "
                    + "	LOTEBXRECEBER.CFILIAL, "
                    + "	BXRECEBER.SJURO, "
                    + "	BXRECEBER.SDESPESA, "
                    + "	BXRECEBER.SDESCONTO "
                    + "FROM RECEBER "
                    + "LEFT JOIN BXRECEBER BX ON (BX.sreceber = RECEBER.sreceber) "
                    + "LEFT JOIN NFSAIDA ON (NFSAIDA.NFS = RECEBER.NFS) "
                    + "LEFT JOIN LOTEBXRECEBER ON (LOTEBXRECEBER.lotebxrec = BX.lotebxrec) "
                    + "LEFT JOIN TPBXCR ON (TPBXCR.CTPBXCR = LOTEBXRECEBER.CTPBXCR) "
                    + "LEFT JOIN CLIFOR ON (RECEBER.CCLIFOR = CLIFOR.CCLIFOR) "
                    + "LEFT JOIN(SELECT "
                    + "        BXRECEBER.LOTEBXREC, "
                    + "        SUM(BXRECEBER.VALOR) SVALOR, "
                    + "        SUM(BXRECEBER.JURO) SJURO, "
                    + "        SUM(BXRECEBER.DESCONTO) SDESCONTO, "
                    + "        SUM(BXRECEBER.DESPESA) SDESPESA, "
                    + "        SUM(BXRECEBER.TARIFA) STARIFA, "
                    + "        SUM(BXRECEBER.ADIANTAMENTO) SADIANTAMENTO, "
                    + "        SUM(BXRECEBER.PISRETIDO) SPISRETIDO, "
                    + "        SUM(BXRECEBER.COFINSRETIDO) SCOFINSRETIDO, "
                    + "        SUM(BXRECEBER.PAGO) SPAGO "
                    + "    FROM BXRECEBER "
                    + "    GROUP BY BXRECEBER.LOTEBXREC) BXRECEBER ON (BXRECEBER.LOTEBXREC = LOTEBXRECEBER.LOTEBXREC) "
                    + "WHERE CLIFOR.CCLIFOR = " + dadosCli
                    + "AND RECEBER.CFILIAL = 1 "
                    + " AND RECEBER.VCTO <= '" + Funcoes.formatarDB(Funcoes.dateToStr(new Date()), "D") + "'"
                    + "GROUP BY RECEBER.DUPLICATA, COALESCE(NFSAIDA.NF,NFSAIDA.NF1),RECEBER.DATA,RECEBER.VCTO, "
                    + "RECEBER.VCTOP,RECEBER.DATA,BXRECEBER.LOTEBXREC,LOTEBXRECEBER.DATA,RECEBER.PARCELA, "
                    + "TPBXCR.NOME, LOTEBXRECEBER.LOTEBXREC,LOTEBXRECEBER.CFILIAL,BXRECEBER.SPAGO,BXRECEBER.SJURO,BXRECEBER.SDESPESA, "
                    + "BXRECEBER.SDESCONTO,RECEBER.SRECEBER "
                    + "ORDER BY RECEBER.VCTO ");
            dupl.open();
            dupl.first();
            while (!dupl.eof()) {
                html.append("<tr>\n"
                        + "  <td class=\"TMcod\">" + dupl.fieldByName("SRECEBER").asString() + "</td>\n"
                        + "  <td class=\"TMDtVencimento\">" + dupl.fieldByName("VCTO").asString() + "</td>\n"
                        + "  <td class=\"TMStatus\"><span class=\"lquidado\">Liquidado</span></td>\n"
                        + "  <td class=\"TMDtLiquidado\">" + dupl.fieldByName("DTPAGAMENTO").asString() + "</td>\n"
                        + "  <td class=\"TMDtLiquidado\">" + dupl.fieldByName("DTPAGAMENTO").asString() + "</td>\n"
                        + "</tr>");
                dupl.next();
            }
            obj.put("ATRASADAS", html.toString());
            obj.put("MSG", "");
        } else {
            obj.put("STATUS", "FALSE");
            obj.put("MSG", "Cliente não localizado! \n Favor recaregar a página!");
        }
        return obj.toString();
    }
}

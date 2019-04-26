package login;

import br.com.tecnicon.server.dataset.TClientDataSet;
import br.com.tecnicon.server.execoes.ExcecaoMsg;
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
public class login {

    public String fazerLogin(VariavelSessao vs) throws ExcecaoTecnicon {
        Funcoes.validaVSNome(vs, new String[]{
            "EMAIL", "Email de login"
        }, new String[]{
            "SENHA", "Senha"
        });

        boolean status = false;
        int cclifor;
        JSONObject jsDados = new JSONObject();

        TClientDataSet cdsLogin = TClientDataSet.create(vs, "GSACESSO");
        cdsLogin.createDataSet();
        cdsLogin.condicao("WHERE EMAIL = '" + vs.getParameter("EMAIL").trim() + "' AND SENHA = '" + vs.getParameter("SENHA").trim() + "'");
        cdsLogin.open();
        cdsLogin.first();

        if (!cdsLogin.isEmpty()) {
            cclifor = cdsLogin.fieldByName("CCLIFOR").asInteger();
            cdsLogin.close();

            TClientDataSet cdsDados = TClientDataSet.create(vs, "GSCLIFOR");
            cdsDados.createDataSet();
            cdsDados.condicao(new StringBuilder("WHERE CCLIFOR = '").append(cclifor).append("'").toString());
            cdsDados.open();

            if (cdsDados.fieldByName("ATIVO").asString().trim().equals("N")) {
                jsDados.put("STATUS", status);
                jsDados.put("MSG", "Esse usuário foi desabilitado! Contate um administrador para desbloquea-lo...");
            } else {
                jsDados.put("ATIVO", cdsDados.fieldByName("ATIVO"));
                jsDados.put("NOME", cdsDados.fieldByName("NOME"));
                jsDados.put("CCLIFOR", cdsDados.fieldByName("CCLIFOR"));

                status = true;
                jsDados.put("STATUS", status);
            }
            return jsDados.toString();
        } else {
            jsDados.put("STATUS", status);
            jsDados.put("MSG", "Usuário ou senha inválidos! Tente novamente!");
            return jsDados.toString();
        }
    }

    public String fazerCadastro(VariavelSessao vs) throws ExcecaoTecnicon {
        Funcoes.validaVSNNNome(vs, new String[]{
            "NOME", "Nome de Usuário"
        }, new String[]{
            "EMAIL", "Email de Login"
        }, new String[]{
            "SENHA", "Senha"
        }, new String[]{
            "CONFSENHA", "Confirmação da Senha"
        });

        if (vs.getParameter("SENHA").equals(vs.getParameter("CONFSENHA"))) {
            int cclifor;
            JSONObject objeto = new JSONObject();

            TClientDataSet cdsCadastro = TClientDataSet.create("GSACESSO");
            cdsCadastro.createDataSet();
            cdsCadastro.condicao("WHERE EMAIL = '" + vs.getParameter("EMAIL") + "' ");
            cdsCadastro.open();

            if (!cdsCadastro.isEmpty()) {
                throw new ExcecaoMsg(vs, "O email já está cadastrado em uma conta!");
            } else {
                cdsCadastro.close();
                TClientDataSet cdsCadastro2 = TClientDataSet.create(vs, "GSCLIFOR");
                cdsCadastro2.createDataSet();
                cdsCadastro2.insert();

                cdsCadastro2.fieldByName("NOME").asString(vs.getParameter("NOME"));
                cdsCadastro2.fieldByName("FANTASIA").asString(vs.getParameter("NOME"));
                cdsCadastro2.fieldByName("DTCADASTRO").asDate(new Date());
                cdsCadastro2.fieldByName("DTALT").asDate(new Date());
                cdsCadastro2.fieldByName("TIPO").asString("C");
                cdsCadastro2.fieldByName("ATIVO").asString("S");
                cdsCadastro2.fieldByName("CFILIAL").asInteger(1);
                cdsCadastro2.fieldByName("MALA").asString("W");

                cdsCadastro2.post();

                cclifor = cdsCadastro2.fieldByName("CCLIFOR").asInteger();

                cdsCadastro = TClientDataSet.create(vs, "GSACESSO");
                cdsCadastro.createDataSet();
                cdsCadastro.insert();

                cdsCadastro.fieldByName("CCLIFOR").asInteger(cclifor);
                cdsCadastro.fieldByName("EMAIL").asString(vs.getParameter("EMAIL"));
                cdsCadastro.fieldByName("SENHA").asString(vs.getParameter("SENHA"));
                cdsCadastro.fieldByName("CONFSENHA").asString(vs.getParameter("CONFSENHA"));
                cdsCadastro.post();

                objeto.put("NOME", vs.getParameter("NOME"));
                objeto.put("COD", cclifor);
                objeto.put("EMAIL", vs.getParameter("EMAIL"));
                objeto.put("STATUS", true);

                return objeto.toString();
            }
        } else {
            if (true) {
                throw new ExcecaoMsg(vs, "asdasdasdsadsa");
            }
            throw new ExcecaoMsg(vs, "As senhas devem ser iguais, por favor digite novamente!");
        }
    }

    public String verificaLogado(VariavelSessao vs) throws ExcecaoTecnicon {
        Funcoes.validaVSNNNome(vs, new String[]{
            "COD", "Codigo do Usuário"
        });

        boolean status = false;
        int cclifor = Funcoes.strToInt(vs.getParameter("COD"));
        JSONObject jsDados = new JSONObject();

        TClientDataSet cdsDados = TClientDataSet.create("GSACESSO");
        cdsDados.createDataSet();
        cdsDados.condicao("WHERE CCLIFOR = " + cclifor);
        cdsDados.open();

        if (!cdsDados.isEmpty()) {
            cdsDados.close();

            cdsDados = TClientDataSet.create(vs, "GSCLIFOR");
            cdsDados.createDataSet();
            cdsDados.condicao(new StringBuilder("WHERE CCLIFOR = '").append(cclifor).append("'").toString());
            cdsDados.open();

            if (cdsDados.fieldByName("ATIVO").asString().trim().equals("N")) {
                jsDados.put("STATUS", status);
                jsDados.put("MSG", "Esse usuário foi desabilitado! Contate um administrador para desbloquea-lo...");
            } else {
                jsDados.put("ATIVO", cdsDados.fieldByName("ATIVO"));
                jsDados.put("NOME", cdsDados.fieldByName("NOME"));
                jsDados.put("CCLIFOR", cdsDados.fieldByName("CCLIFOR"));

                status = true;
                jsDados.put("STATUS", status);
            }
        } else {
            jsDados.put("STATUS", status);
        }
        return jsDados.toString();
    }
}

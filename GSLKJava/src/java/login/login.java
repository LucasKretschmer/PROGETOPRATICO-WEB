package login;

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
public class login {

    public String fazerLogin(VariavelSessao vs) throws ExcecaoTecnicon {
        Funcoes.validaVSNome(vs, new String[]{
            "EMAIL", "Email de login"
        }, new String[]{
            "SENHA", "Senha"
        });

        boolean status = false;
        int cclifor;
        String email;
        String senha;
        JSONObject jsDados = new JSONObject();

        TClientDataSet cdsLogin = TClientDataSet.create(vs, "GSACESSO");
        cdsLogin.createDataSet();
        cdsLogin.condicao("WHERE EMAIL = '" + vs.getParameter("EMAIL").trim() + "' AND SENHA = '" + vs.getParameter("SENHA").trim() + "'");
        cdsLogin.open();
        cdsLogin.first();

        if (!cdsLogin.isEmpty()) {
            email = cdsLogin.fieldByName("EMAIL").asString();
            senha = cdsLogin.fieldByName("SENHA").asString();
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

        boolean status = false;
        int cclifor;
        String nome;
        String email;

        TClientDataSet cdsCadastro = TClientDataSet.create("GSCLIFOR");
        cdsCadastro.createDataSet();
        cdsCadastro.condicao("WHERE EMAIL = '" + vs.getParameter("EMAIL").trim() + "'");
        cdsCadastro.open();
        cdsCadastro.first();

        if (!cdsCadastro.isEmpty()) {
            JSONObject retErro = new JSONObject();
            retErro.put("STATUS", status);
            retErro.put("MSG", "Esse nome e email já estão vinculados a uma conta! Faça login ou caso você tenha perdido a senha entre em contato com o administrador!");
            return retErro.toString();
        } else {
            nome = cdsCadastro.fieldByName("NOME").asString();
            email = cdsCadastro.fieldByName("EMAIL").asString();

            if (vs.getParameter("SENHA").equals(vs.getParameter("CONFSENHA"))) {
                cdsCadastro.insert();

                cdsCadastro.fieldByName("NOME").asString(vs.getParameter("NOME"));
                cdsCadastro.fieldByName("FANTASIA").asString(vs.getParameter("NOME"));
                cdsCadastro.fieldByName("CFILIAL").asInteger(1);
                cdsCadastro.fieldByName("TIPO").asString("C");
                cdsCadastro.post();
                cclifor = cdsCadastro.fieldByName("CCLIFOR").asInteger();
                cdsCadastro.close();
                TClientDataSet cdsCadastro2 = TClientDataSet.create("GSACESSO");
                cdsCadastro2.createDataSet();
                cdsCadastro2.insert();

                cdsCadastro2.fieldByName("CCLIFOR").asInteger(cclifor);
                cdsCadastro2.fieldByName("EMAIL").asString(vs.getParameter("EMAIL"));
                cdsCadastro2.fieldByName("SENHA").asString(vs.getParameter("SENHA"));
                cdsCadastro2.fieldByName("CONFSENHA").asString(vs.getParameter("CONFSENHA"));
                cdsCadastro2.post();

                return fazerLogin(vs);
            } else {
                JSONObject retErro = new JSONObject();
                retErro.put("STATUS", status);
                retErro.put("MSG", "As senhas digitadas não conferem, digite novamente!!!");
                return retErro.toString();
            }
        }
    }

    public String logado(VariavelSessao vs) throws ExcecaoTecnicon {
        Funcoes.validaVSNNNome(vs, new String[]{
            "COD", "Codigo do Usuário"
        });

        boolean status = false;
        int cclifor = Funcoes.strToInt(vs.getParameter("COD"));
        JSONObject jsDados = new JSONObject();
        TClientDataSet cdsDados = TClientDataSet.create("GSACESSO");
        cdsDados.createDataSet();
        cdsDados.condicao("WHERE CCLIFOR = " + vs.getParameter("COD"));
        cdsDados.open();

        if (!cdsDados.isEmpty()) {
            cdsDados.close();

            TClientDataSet cdsDadosc = TClientDataSet.create(vs, "GSCLIFOR");
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
        }
        return jsDados.toString();
    }
}

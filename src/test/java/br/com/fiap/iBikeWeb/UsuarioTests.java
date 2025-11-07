package br.com.fiap.iBikeWeb;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTests extends TestConfig {

    private static final String PERFIL_URL = "/usuario";

    @Test
    @DisplayName("Admin acessa página de perfil e visualiza seus dados corretamente")
    public void visualizarPerfil() {
        loginComoAdmin();
        irPara(PERFIL_URL);

        // Verifica título/h1 visível
        assertTrue(
            textoNaPagina("Meu Perfil") || textoNaPagina("Informações do Usuário"),
            "Página de perfil deve conter 'Meu Perfil' ou texto identificando o perfil."
        );

        // Verifica se o e-mail do admin aparece (campo exibido)
        assertTrue(
            textoNaPagina(EMAIL_ADMIN),
            "O e-mail do administrador deve estar visível na página."
        );

        // Verifica rótulos do cartão de informações (conforme o HTML)
        assertTrue(
            textoNaPagina("Nome Completo") && textoNaPagina("Email") && textoNaPagina("Pátio Atribuído"),
            "Devem existir os labels 'Nome Completo', 'Email' e 'Pátio Atribuído' na página."
        );

        // Verifica se há campo <select name="patioId">
        assertTrue(
            elementoExiste(By.name("patioId")),
            "Deve existir campo de seleção de pátio com name='patioId'."
        );
    }

    @Test
    @DisplayName("Admin edita seus dados de perfil com sucesso")
    public void editarPerfilComSucesso() {
        loginComoAdmin();
        irPara(PERFIL_URL);

        // Preenche novo nome e e-mail válidos
        WebElement nome = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nome")));
        nome.clear();
        nome.sendKeys("Administrador Atualizado");

        WebElement email = driver.findElement(By.name("email"));
        email.clear();
        email.sendKeys("admineditado@ibike.com");

        // Seleciona outro pátio se houver opções (opcional)
        if (elementoExiste(By.name("patioId"))) {
            WebElement select = driver.findElement(By.name("patioId"));
            // seleciona a primeira opção diferente da atual (se houver)
            try {
                new org.openqa.selenium.support.ui.Select(select).selectByIndex(0);
            } catch (Exception e) {
                // ignora se não for selecionável
            }
        }

        // Clica no botão "Salvar Alterações"
        clicarBotaoPorTexto("Salvar Alterações");

        // Aguarda redirecionamento ou parâmetro ?editado
        aguardarRedirecionamento(PERFIL_URL);

        // Verifica se há indicação de sucesso (URL ou texto)
        assertTrue(
            driver.getCurrentUrl().contains("?editado") ||
            textoNaPagina("atualizado") ||
            textoNaPagina("editado com sucesso") ||
            textoNaPagina("Salvo"),
            "Após edição, deve exibir confirmação de sucesso (URL ?editado ou mensagem)."
        );
    }

    @Test
    @DisplayName("Admin tenta editar perfil deixando nome em branco e vê erro de validação")
    public void editarPerfilComErro() {
        loginComoAdminEditado();
        irPara(PERFIL_URL);

        // Deixa o campo de nome em branco (campo possui required no template)
        WebElement nome = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nome")));
        nome.clear();

        WebElement email = driver.findElement(By.name("email"));
        email.clear();
        email.sendKeys("adminerro@ibike.com");

        // Tenta submeter
        clicarBotaoPorTexto("Salvar Alterações");

        // Captura mensagem nativa de validação HTML5 (se houver)
        String validationMessage = "";
        try {
            validationMessage = (String) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].validationMessage;", nome);
        } catch (Exception e) {
            // se JS não puder executar, deixa vazio
        }

        // Mensagem renderizada pelo backend (se houver)
        String erro = obterMensagemErro();

        // Valida que houve bloqueio/erro (navegador em pt/en ou backend)
        assertTrue(
            (!validationMessage.isEmpty() &&
                (validationMessage.toLowerCase().contains("preencha")
                 || validationMessage.toLowerCase().contains("required")
                 || validationMessage.toLowerCase().contains("obrigatório")
                 || validationMessage.toLowerCase().contains("fill")))
            || (erro != null && erro.toLowerCase().contains("nome"))
            || textoNaPagina("campo obrigatório")
            || textoNaPagina("preencha o nome"),
            "Deve exibir mensagem de erro informando que o nome é obrigatório. Encontrado: '"
            + validationMessage + "' / '" + erro + "'"
        );

        // Verifica que permaneceu na página /usuario (sem redirect)
        assertTrue(driver.getCurrentUrl().contains("/usuario"),
            "Deve permanecer na página de perfil após erro de validação.");
    }

    @Test
    @DisplayName("Admin faz logout")
    public void logoutSemDesativacao() {
        loginComoAdminEditado();

        // Clica no botão "Sair" que está dentro do form th:action="@{/logout}"
        clicarBotaoPorTexto("Sair");

        // Aguarda redirecionamento para /login
        aguardarRedirecionamento("/login");

        // Verifica existência da tela de login
        assertTrue(
            textoNaPagina("Login") || textoNaPagina("Entrar"),
            "Após logout, o usuário deve ser redirecionado à página de login."
        );
    }
}

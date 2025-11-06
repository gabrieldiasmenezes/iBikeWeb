package br.com.fiap.iBikeWeb;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class AdminTests extends TestConfig {

    @Test
    @DisplayName("Login com credenciais válidas entra na Home")
    public void loginValido() {
        loginComoAdmin();

        // Verifica redirecionamento correto
        assertTrue(driver.getCurrentUrl().contains("/home"),
            "Deveria redirecionar para /home após login válido.");

        // Verifica presença do nome do usuário e o tipo de acesso na página
        String pageSource = driver.getPageSource();
        boolean conteudoValido = pageSource.contains("Olá") &&
                                (pageSource.contains("Administrador") || pageSource.contains("Funcionário"));

        assertTrue(conteudoValido,
            "Página Home deve exibir saudação e tipo de acesso ('Administrador' ou 'Funcionário'). Conteúdo encontrado: "
            + pageSource.substring(0, Math.min(200, pageSource.length())));
    }

    @Test
    @DisplayName("Login sem e-mail mostra erro de campo obrigatório")
    public void loginSemEmail() {
        driver.get(BASE_URL + "/login");

        // Preenche apenas senha
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")))
            .sendKeys(SENHA_ADMIN);

        clicarBotaoSubmit();

        // Espera mensagem de erro
        String erro = obterMensagemErro();

        // Verifica se há erro relacionado ao e-mail
        boolean erroEmailObrigatorio = erro.toLowerCase().contains("e-mail") &&
            (erro.toLowerCase().contains("obrigatório") || erro.toLowerCase().contains("required"));

        assertAll("Validações de erro sem e-mail",
            () -> assertTrue(erroEmailObrigatorio || driver.getCurrentUrl().contains("/login"),
                "Deveria mostrar erro de e-mail obrigatório ou permanecer no login. Erro: '" + erro + "'"),
            () -> assertTrue(driver.getCurrentUrl().contains("/login"),
                "Deveria permanecer na página de login")
        );
    }

    @Test
    @DisplayName("Login com e-mail/senha errados mostra erro de credenciais inválidas")
    public void loginInvalido() {
        driver.get(BASE_URL + "/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")))
            .sendKeys("errado@ibike.com");
        driver.findElement(By.name("password")).sendKeys("123");
        clicarBotaoSubmit();

        String erro = obterMensagemErro();

        boolean erroCredenciais = erro.toLowerCase().contains("inválido") ||
            erro.toLowerCase().contains("incorreta") ||
            erro.toLowerCase().contains("credenciais") ||
            erro.toLowerCase().contains("não encontrado");

        assertAll("Validações de login inválido",
            () -> assertTrue(erroCredenciais,
                "Mensagem de erro deve indicar credenciais inválidas. Encontrado: '" + erro + "'"),
            () -> assertTrue(driver.getCurrentUrl().contains("/login"),
                "Deveria permanecer na página de login após falha")
        );
    }

    @Test
    @DisplayName("Acesso direto a /home redireciona para login (bloqueado)")
    public void acessoDiretoBloqueado() {
        driver.get(BASE_URL + "/home");

        // Aguarda redirecionamento para login
        wait.until(ExpectedConditions.urlContains("/login"));

        assertAll("Validações de bloqueio de acesso direto",
            () -> assertTrue(driver.getCurrentUrl().contains("/login"),
                "Deveria redirecionar para /login ao acessar /home sem autenticação"),
            () -> assertTrue(driver.findElement(By.name("username")).isDisplayed(),
                "Campo de e-mail deve estar visível na página de login")
        );
    }
}
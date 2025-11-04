package br.com.fiap.iBikeWeb;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.chrome.ChromeDriver;
import br.com.fiap.iBikeWeb.TestConfig;

public class AdminTests { 

    // POSITIVO
    @Test
    @DisplayName("Login com credenciais válidas entra no admin")
    public void loginValido() {
        loginComoAdmin();
        assertTrue(driver.getCurrentUrl().contains("/admin"), "Deveria estar em /admin");
        String pagina = driver.getPageSource();
        assertTrue(pagina.contains(EMAIL_ADMIN) || pagina.contains("Admin"), "Admin não identificado na página");
    }

    // NEGATIVO 1: Sem e-mail
    @Test
    @DisplayName("Login sem e-mail mostra erro")
    public void loginSemEmail() {
        driver.get(BASE_URL + "/login");
        driver.findElement(By.name("password")).sendKeys(SENHA_ADMIN);
        clicarBotaoSubmit();

        String erro = obterMensagemErro();
        boolean temErro = erro.toLowerCase().contains("e-mail") ||
                          erro.toLowerCase().contains("obrigatório") ||
                          erro.toLowerCase().contains("required");

        assertTrue(temErro || driver.getCurrentUrl().contains("/login"),
            "Deveria mostrar erro ou permanecer no login. Erro encontrado: '" + erro + "'");
    }

    // NEGATIVO 2: Credenciais erradas
    @Test
    @DisplayName("Login com e-mail/senha errados mostra erro")
    public void loginInvalido() {
        driver.get(BASE_URL + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")))
            .sendKeys("errado@ibike.com");
        driver.findElement(By.name("password")).sendKeys("123");
        clicarBotaoSubmit();

        String erro = obterMensagemErro();
        assertTrue(
            erro.toLowerCase().contains("inválido") ||
            erro.toLowerCase().contains("incorreta") ||
            erro.toLowerCase().contains("credenciais"),
            "Mensagem de erro esperada. Encontrado: '" + erro + "'"
        );
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    // NEGATIVO 3: Acesso direto sem login
    @Test
    @DisplayName("Acesso direto a /admin")
    public void acessoDiretoBloqueado() {
        driver.get(BASE_URL + "/admin");
        wait.until(ExpectedConditions.urlContains("/login"));

        assertTrue(driver.getCurrentUrl().contains("/login"));
        assertTrue(driver.findElement(By.name("email")).isDisplayed());
    }
}
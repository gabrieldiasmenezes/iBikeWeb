package br.com.fiap.iBikeWeb;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import static org.junit.jupiter.api.Assertions.*;

public class AdminTests extends TestConfig {

    @Test
    @DisplayName("Cadastro de novo administrador com sucesso")
    public void cadastroComSucesso() {
        driver.get(BASE_URL + "/admin/cadastro");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nome"))).sendKeys("Julia Santos");
        driver.findElement(By.name("email")).sendKeys("novo@ibike.com");
        driver.findElement(By.name("cpf")).sendKeys("98765432100");
        driver.findElement(By.name("password")).sendKeys("123456");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Redireciona para lista ou mostra sucesso
        wait.until(ExpectedConditions.urlContains("/admin"));
        String pageSource = driver.getPageSource();

        assertTrue(pageSource.contains("novo@ibike.com") || pageSource.contains("Ana Silva"));
        assertFalse(pageSource.contains("123456")); // senha NUNCA aparece
    }

    @Test
    @DisplayName("Tentativa de cadastro com CPF já existente")
    public void cadastroComCpfJaExistente() {
        driver.get(BASE_URL + "/admin/cadastro");

        driver.findElement(By.name("nome")).sendKeys("João");
        driver.findElement(By.name("email")).sendKeys("duplicado@ibike.com");
        driver.findElement(By.name("cpf")).sendKeys("98765432100"); // mesmo CPF do teste anterior
        driver.findElement(By.name("password")).sendKeys("123456");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String erro = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".alert-danger, .text-danger, [th\\:errors]"))).getText();

        assertTrue(erro.contains("CPF já existe") || erro.contains("já cadastrado"));
    }
    
    @Test
    @DisplayName("Tentativa de login com e-mail ou senha inválidos")
    public void loginComEmailOuSenhaInvalidos() {
        driver.get(BASE_URL + "/login");

        // Preenche campos de login
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email"))).sendKeys("admin@ibike.com");
        driver.findElement(By.name("password")).sendKeys("senhaErrada");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Aguarda mensagem de erro na tela
        String erro = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".alert-danger, .text-danger, [th\\:errors]"))).getText();

        // Valida mensagem esperada
        assertTrue(
            erro.contains("E-mail ou senha inválidos") ||
            erro.contains("Credenciais incorretas") ||
            erro.contains("falha no login"),
            "Mensagem de erro de login não exibida corretamente"
        );

        // Continua na página de login
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }
}
package br.com.fiap.iBikeWeb;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class TestConfig {

    // === CONFIGURAÇÕES GLOBAIS ===
    public static final String BASE_URL = "http://localhost:8080";
    public static final String EMAIL_ADMIN = "admin@ibike.com";
    public static final String SENHA_ADMIN = "123456";

    // === DRIVER E WAIT ===
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // === MÉTODOS REUTILIZÁVEIS ===

    protected void clicarBotaoSubmit() {
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button[type='submit'], #btn-salvar, #btn-login, input[type='submit']")
        ));
        botao.click();
    }

    protected void loginComoAdmin() {
        driver.get(BASE_URL + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")))
            .sendKeys(EMAIL_ADMIN);
        driver.findElement(By.name("password")).sendKeys(SENHA_ADMIN);
        clicarBotaoSubmit();
        wait.until(ExpectedConditions.urlContains("/admin"));
    }

    protected String obterMensagemErro() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".alert-danger, .text-danger, .error, .invalid-feedback, [role='alert'], .text-danger")
            )).getText().trim();
        } catch (TimeoutException e) {
            return "";
        }
    }

    protected String obterMensagemSucesso() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".alert-success, .text-success, .success, .alert-info")
            )).getText().trim();
        } catch (TimeoutException e) {
            return "";
        }
    }

    protected void preencherFormularioPatio(String nome, String capacidade) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("nome")))
            .clear();
        driver.findElement(By.name("nome")).sendKeys(nome);

        driver.findElement(By.name("capacidade")).clear();
        driver.findElement(By.name("capacidade")).sendKeys(capacidade);
    }

    protected void preencherFormularioMoto(String placa, String modelo, String patioId) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("placa")))
            .clear();
        driver.findElement(By.name("placa")).sendKeys(placa);

        driver.findElement(By.name("modelo")).clear();
        driver.findElement(By.name("modelo")).sendKeys(modelo);

        WebElement selectPatio = driver.findElement(By.name("patio.id"));
        new Select(selectPatio).selectByValue(patioId);
    }

    protected boolean elementoExiste(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected boolean textoNaPagina(String texto) {
        return driver.getPageSource().contains(texto);
    }

    protected boolean patioNaLista(String nome) {
        return elementoExiste(By.xpath("//td[normalize-space()='" + nome + "']"));
    }

    protected boolean statusPatioNaLista(String nome, String status) {
        return elementoExiste(
            By.xpath("//td[normalize-space()='" + nome + "']/following-sibling::td//span[normalize-space()='" + status + "']")
        );
    }

    protected boolean motoNaLista(String placa) {
        return elementoExiste(By.xpath("//td[normalize-space()='" + placa + "']"));
    }

    protected boolean statusMotoNaLista(String placa, String status) {
        return elementoExiste(
            By.xpath("//td[normalize-space()='" + placa + "']/following-sibling::td//span[normalize-space()='" + status + "']")
        );
    }

    protected void irPara(String url) {
        driver.get(BASE_URL + url);
    }

    protected void aguardarRedirecionamento(String parteUrl) {
        wait.until(ExpectedConditions.urlContains(parteUrl));
    }

    protected void clicarBotaoPorTexto(String texto) {
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), '" + texto + "')] | //a[contains(text(), '" + texto + "')]")
        ));
        botao.click();
    }

    protected void clicarDesativarPatio(String nomePatio) {
        WebElement botao = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//td[normalize-space()='" + nomePatio + "']/following-sibling::td//button[contains(text(), 'Desativar')]")
        ));
        botao.click();
    }
}
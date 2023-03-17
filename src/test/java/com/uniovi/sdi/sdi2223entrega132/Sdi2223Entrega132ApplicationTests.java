package com.uniovi.sdi.sdi2223entrega132;

import com.uniovi.sdi.sdi2223entrega132.repositories.UsersRepository;
import com.uniovi.sdi.sdi2223entrega132.services.InsertSampleDataService;
import com.uniovi.sdi.sdi2223entrega132.util.SeleniumUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.uniovi.sdi.sdi2223entrega132.pageobjects.*;

import java.util.List;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sdi2223Entrega132ApplicationTests {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private InsertSampleDataService insertSampleDataService;
    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    static String Geckodriver = "C:\\Dev\\geckodriver\\geckodriver-v0.30.0-win64.exe";
    static WebDriver driver = getDriver(PathFirefox, Geckodriver);
    static String URL = "http://localhost:8090";

    public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        System.setProperty("webdriver.gecko.driver", Geckodriver);
        driver = new FirefoxDriver();
        return driver;
    }

    @BeforeEach
    public void setUp() {
        driver.navigate().to(URL);
        usersRepository.deleteAll();

        // Metemos otra vez los datos iniciales de prueba
        insertSampleDataService.init();
    }

    //Después de cada prueba se borran las cookies del navegador
    @AfterEach
    public void tearDown() {
        driver.manage().deleteAllCookies();
    }

    //Antes de la primera prueba
    @BeforeAll
    static public void begin() {
    }

    //Al finalizar la última prueba
    @AfterAll
    static public void end() {
        //Cerramos el navegador al finalizar las pruebas
        driver.quit();
    }

    /*
    @Test
    public void PR15(){
        // Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user@email.com", "123456");

        //Pinchamos en la opción de menú de ofertas: //li[contains(@id, 'offers-menu')]/a
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@id, 'offers-menu')]/a",0);
        //Esperamos a que aparezca la opción de añadir oferta: //a[contains(@href, 'offer/add')]
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/add')]",0);

        // Rellenamos el formulario de alta de oferta con datos validos
        PO_PrivateView.fillFormAddOffer(driver, "PruebaTitulo", "PruebaDescripcion", "0.21");

        // Comprobamos que la oferta recien añadida sale en la lista de ofertas propias
        // del usuario
        PO_PrivateView.checkElement(driver, "PruebaTitulo");
        PO_PrivateView.checkElement(driver, "PruebaDescripcion");
        PO_PrivateView.checkElement(driver,"0.21");

        // Hacemos logout
        PO_PrivateView.logout(driver);
    }
    */

    @Test
    @Order(12)
    public void PR12(){
        // Iniciamos sesión como administrador
        PO_PrivateView.login(driver, "admin@email.com", "admin");
        //Contamos el número de filas de usuarios
        List<WebElement> userList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",PO_View.getTimeout());
        Assertions.assertEquals(5, userList.size());
        WebElement firstCheckbox = driver.findElement(By.xpath("//input[@type='checkbox'][1]"));
        firstCheckbox.click();
        userList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",PO_View.getTimeout());
        Assertions.assertEquals(5, userList.size());
        List<WebElement> submitButtons = driver.findElements(By.xpath("//button[@type='submit']"));
        submitButtons.get(1).click();
        userList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",PO_View.getTimeout());
        Assertions.assertEquals(4, userList.size());
        PO_PrivateView.logout(driver);
    }

    @Test
    @Order(26)
    public void PR26(){
        // Iniciamos sesión como administrador
        PO_PrivateView.login(driver, "jincho@email.com", "jincho");
        //Obtenemos el numero de dropdowns
        List<WebElement> dropdownButtons = driver.findElements(By.xpath("//li[@class='nav-item dropdown']"));
        Assertions.assertEquals(2, dropdownButtons.size());
        dropdownButtons.get(0).click();
        PO_PrivateView.checkViewAndClick(driver,"class","dropdown-item",3);
        PO_PrivateView.checkElement(driver,PO_HomeView.getP().getString("msg.nav.search", PO_Properties.getSPANISH()));
        WebElement searchText = driver.findElement(By.xpath("//input[@class='form-control']"));
        searchText.click();
        searchText.clear();
        searchText.sendKeys("Playeros");
        searchText.sendKeys(Keys.ENTER);

        List<WebElement> cards = SeleniumUtils.waitLoadElementsBy(driver, "class", "card-body",6);
        Assertions.assertEquals(1, cards.size());

        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'conversation/')]",1);

        PO_PrivateView.checkElement(driver,PO_HomeView.getP().getString("msg.conversation.header", PO_Properties.getSPANISH()));

        WebElement messageInput = driver.findElement(By.xpath("//input[@class='form-control']"));

        messageInput.click();
        messageInput.clear();
        messageInput.sendKeys("Hola");

        WebElement sendButton = driver.findElement(By.xpath("//button[@type='submit']"));

        sendButton.click();

        List<WebElement> messages = driver.findElements(By.xpath("//p[@class='small p-2 me-3 mb-3 text-white rounded-3 bg-dark']"));

        Assertions.assertEquals(1,messages.size());

        PO_PrivateView.logout(driver);
    }

    @Test
    @Order(27)
    public void PR27(){
        // Iniciamos sesión como administrador
        PO_PrivateView.login(driver, "pepe@email.com", "pepe");
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'conversation/')]",0);

        List<WebElement> conversationLinks = driver.findElements(By.xpath("//a[contains(@href, 'conversation/')]"));

        conversationLinks.get(0).click();

        PO_PrivateView.checkElement(driver,PO_HomeView.getP().getString("msg.conversation.list.info", PO_Properties.getSPANISH()));

        PO_PrivateView.checkElement(driver,"Cartera");

        conversationLinks = driver.findElements(By.xpath("//a[contains(@href, 'conversation/')]"));

        conversationLinks.get(1).click();

        PO_PrivateView.checkElement(driver,PO_HomeView.getP().getString("msg.conversation.header", PO_Properties.getSPANISH()));

        WebElement messageInput = driver.findElement(By.xpath("//input[@class='form-control']"));

        messageInput.click();
        messageInput.clear();
        messageInput.sendKeys("Hola");

        WebElement sendButton = driver.findElement(By.xpath("//button[@type='submit']"));

        sendButton.click();

        List<WebElement> messages = driver.findElements(By.xpath("//p[@class='small p-2 me-3 mb-3 text-white rounded-3 bg-dark']"));

        Assertions.assertEquals(messages.get(0).getText(),"Hola");

        Assertions.assertEquals(1,messages.size());

        PO_PrivateView.logout(driver);
    }
}


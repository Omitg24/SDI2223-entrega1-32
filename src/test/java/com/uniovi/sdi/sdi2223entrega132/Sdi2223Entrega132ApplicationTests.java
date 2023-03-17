package com.uniovi.sdi.sdi2223entrega132;

import com.uniovi.sdi.sdi2223entrega132.repositories.UsersRepository;
import com.uniovi.sdi.sdi2223entrega132.services.InsertSampleDataService;
import com.uniovi.sdi.sdi2223entrega132.util.SeleniumUtils;
import jdk.jfr.Timespan;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sdi2223Entrega132ApplicationTests {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private InsertSampleDataService insertSampleDataService;
    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    static String Geckodriver = "geckodriver-v0.30.0-win64.exe";
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

    // 1. Público: Registrarse como usuario

    /**
     * PR01. Registro de Usuario con datos válidos.
     * Realizada por: Omar
     */
    @Test
    @Order(1)
    public void PR01() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_SignUpView.fillForm(driver, "uo123456@uniovi.es", "Adrián", "García Fernández", "123456", "123456");
        //Comprobamos que entramos en la sección privada y nos nuestra el texto a buscar
        String checkText = "Listado de ofertas propias";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * PR02. Registro de Usuario con datos inválidos (email vacío, nombre vacío, apellidos vacíos).
     * Realizada por: Omar
     */
    @Test
    @Order(2)
    public void PR02() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_SignUpView.fillForm(driver, "", "", "", "123456", "123456");
        //Comprobamos los errores
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "error.empty",
                PO_Properties.getSPANISH());
        String checkText = PO_HomeView.getP().getString("error.empty",
                PO_Properties.getSPANISH()) + "\n" + PO_HomeView.getP().getString("error.user.email.length",
                PO_Properties.getSPANISH()) + "\n" + PO_HomeView.getP().getString("error.user.email.format",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * PR03. Registro de Usuario con datos inválidos (repetición de contraseña inválida).
     * Realizada por: Omar
     */
    @Test
    @Order(3)
    public void PR03() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_SignUpView.fillForm(driver, "uo123456@uniovi.es", "Adrián", "García Fernández", "123456", "654321");
        //Comprobamos los errores
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "error.user.passwordConfirm.coincidence",
                PO_Properties.getSPANISH());
        String checkText = PO_HomeView.getP().getString("error.user.passwordConfirm.coincidence",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * PR04. Registro de Usuario con datos inválidos (email existente).
     * Realizada por: Omar
     */
    @Test
    @Order(4)
    public void PR04() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_SignUpView.fillForm(driver, "user01@email.com", "Adrián", "García Fernández", "123456", "123456");
        //Comprobamos los errores
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "error.user.email.duplicate",
                PO_Properties.getSPANISH());
        String checkText = PO_HomeView.getP().getString("error.user.email.duplicate",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    // 2. Usuario Registrado: Iniciar sesión

    /**
     * PR05. Inicio de sesión con datos válidos (administrador).
     * Realizada por: Omar
     */
    @Test
    @Order(5)
    public void PR05() {
        //Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        //Comprobamos que entramos en la sección de listar usuarios
        String checkText = "Usuarios";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * PR06. Inicio de sesión con datos válidos (usuario estándar).
     * Realizada por: Omar
     */
    @Test
    @Order(6)
    public void PR06() {
        //Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Comprobamos que entramos en la sección de listado de ofertas propias
        String checkText = "Listado de ofertas propias";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * PR07. Inicio de sesión con datos inválidos (usuario estándar, campo email y contraseña vacíos).
     * Realizada por: Omar
     */
    @Test
    @Order(7)
    public void PR07() {
        //Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "", "");
        //Comprobamos los errores
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "msg.login.error",
                PO_Properties.getSPANISH());
        String checkText = PO_HomeView.getP().getString("msg.login.error",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * PR08. Inicio de sesión con datos válidos (usuario estándar, email existente, pero contraseña
     * incorrecta).
     * Realizada por: Omar
     */
    @Test
    @Order(8)
    public void PR08() {
        //Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user");
        //Comprobamos los errores
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "msg.login.error",
                PO_Properties.getSPANISH());
        String checkText = PO_HomeView.getP().getString("msg.login.error",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * PR09. Hacer clic en la opción de salir de sesión y comprobar que se redirige a la página de inicio de
     * sesión (Login).
     * Realizada por: Omar
     */
    @Test
    @Order(9)
    public void PR09() {
        //Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        //Nos desconectamos
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-dark");
        //Comprobamos que hemos cerrado la sesión
        String checkText = "Identificarse";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * PR10. Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado.
     * Realizada por: Omar
     */
    @Test
    @Order(10)
    public void PR10() {
        //Comprobamos que el elemento no está
        List<WebElement> result = driver.findElements(By.xpath("//a[contains(@href, '/logout')]"));
        Assertions.assertTrue(result.isEmpty());
    }

    /**
     * PR11. Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el
     * sistema.
     * Realizada por: Omar
     */
    @Test
    @Order(11)
    public void PR11() {
        //Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        //Comprobamos que entramos en la sección privada y nos nuestra el texto a buscar
        List<WebElement> result = new ArrayList<>();
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//a[contains(@class, 'page-link')]");

        //Añadimos los elementos en la primera página
        result.addAll(SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout()));

        //Vamos a la segunda pagina y añadimos los elementos
        elements = PO_View.checkElementBy(driver, "free", "//a[contains(@class, 'page-link')]");
        elements.get(2).click();
        result.addAll(SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout()));

        //Vamos a la tercera pagina y añadimos los elementos
        elements = PO_View.checkElementBy(driver, "free", "//a[contains(@class, 'page-link')]");
        elements.get(3).click();
        result.addAll(SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout()));

        //Vamos a la cuarta pagina y añadimos los elementos
        elements = PO_View.checkElementBy(driver, "free", "//a[contains(@class, 'page-link')]");
        elements.get(4).click();
        result.addAll(SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout()));

        Assertions.assertEquals(16, result.size());
    }

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
    /**
     * PR15. Añadir una nueva oferta y comprobar que se muestra en la vista.
     * Realizada por: David
     */
    @Test
    @Order(15)
    public void PR15() {
        // Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user15@email.com", "user15");

        //Pinchamos en la opción de menú de ofertas: //li[contains(@id, 'offers-menu')]/a
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]/a", 0);
        //Esperamos a que aparezca la opción de añadir oferta: //a[contains(@href, 'offer/add')]
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/add')]", 0);

        // Rellenamos el formulario de alta de oferta con datos validos
        PO_PrivateView.fillFormAddOffer(driver, "PruebaTitulo", "PruebaDescripcion", "0.21");

        // Comprobamos que la oferta recien añadida sale en la lista de ofertas propias
        // del usuario
        PO_PrivateView.checkElement(driver, "PruebaTitulo");
        PO_PrivateView.checkElement(driver, "PruebaDescripcion");
        PO_PrivateView.checkElement(driver, "0.21 EUR");

        // Hacemos logout
        PO_PrivateView.logout(driver);
    }

    /**
     * PR16. Añadir una nueva oferta con precio negativo y comprobar el mensaje de error.
     * Realizada por: David
     */
    @Test
    @Order(16)
    public void PR16() {
        // Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user15@email.com", "user15");

        //Pinchamos en la opción de menú de ofertas: //li[contains(@id, 'offers-menu')]/a
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]/a", 0);
        //Esperamos a que aparezca la opción de añadir oferta: //a[contains(@href, 'offer/add')]
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/add')]", 0);

        // Rellenamos el formulario de alta de oferta con datos validos, menos el precio
        PO_PrivateView.fillFormAddOffer(driver, "PruebaTitulo", "PruebaDescripcion", "-0.21");

        // Comprobamos que se muestra el mensaje de error
        String checkText = PO_HomeView.getP().getString("error.offer.price.positive",
                PO_Properties.getSPANISH());
        PO_PrivateView.checkElement(driver, checkText);
        // Hacemos logout
        PO_PrivateView.logout(driver);
    }

    /**
     * PR17. Mostrar todas las ofertas de un usuario, comprobando que se encuentran todas, 6 paginas (28 ofertas).
     * Realizada por: David
     */
    @Test
    @Order(17)
    public void PR17() {
        // Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user04@email.com", "user04");

        //Pinchamos en la opción de menú de ofertas: //li[contains(@id, 'offers-menu')]/a
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]/a", 0);
        //Esperamos a que aparezca la opción de mostrar oferta propias: //a[contains(@href, 'offer/ownedList')]
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/ownedList')]", 0);

        //Guardamos el número de ofertas de la primera página
        List<WebElement> offerList = SeleniumUtils.waitLoadElementsBy(driver, "free",
                "//div[contains(@class, 'card border-dark mb-3')]", PO_View.getTimeout());
        //Vamos a la segunda página
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@class, 'page-link')]", 2);
        offerList.addAll(SeleniumUtils.waitLoadElementsBy(driver, "free",
                "//div[contains(@class, 'card border-dark mb-3')]", PO_View.getTimeout()));
        //Recorremos las paginas guardando las ofertas
        for (int i = 0; i < 4; i++) {
            PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@class, 'page-link')]", 3);
            offerList.addAll(SeleniumUtils.waitLoadElementsBy(driver, "free",
                    "//div[contains(@class, 'card border-dark mb-3')]", PO_View.getTimeout()));
        }
        // Comprobamos que se encuentren todas las ofertas
        Assertions.assertEquals(28, offerList.size());
        // Hacemos logout
        PO_PrivateView.logout(driver);
    }

    /**
     * PR18. Borramos la primera oferta de la lista que tenemos de ofertas propias.
     * Realizada por: David
     */
    @Test
    @Order(18)
    public void PR18() {
        // Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user04@email.com", "user04");
        //Pinchamos en la opción de menú de ofertas: //li[contains(@id, 'offers-menu')]/a
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]/a", 0);
        //Esperamos a que aparezca la opción de mostrar oferta propias: //a[contains(@href, 'offer/ownedList')]
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/ownedList')]", 0);
        //Borramos la primera oferta de la pagina
        PO_PrivateView.checkViewAndClick(driver, "free",
                "//h6[contains(text(), 'Producto 3')]/following-sibling::*/a[contains(@href, 'offer/delete')]",0);
        //Comprobamos que ha desaparecido la oferta 'Producto 3'
        SeleniumUtils.waitTextIsNotPresentOnPage(driver, "Producto 3", PO_View.getTimeout());
        // Hacemos logout
        PO_PrivateView.logout(driver);
    }

    /**
     * PR19. Borramos la última oferta de la lista que tenemos de ofertas propias.
     * Realizada por: David
     */
    @Test
    @Order(19)
    public void PR19() {
        // Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user04@email.com", "user04");
        //Pinchamos en la opción de menú de ofertas: //li[contains(@id, 'offers-menu')]/a
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]/a", 0);
        //Esperamos a que aparezca la opción de mostrar oferta propias: //a[contains(@href, 'offer/ownedList')]
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/ownedList')]", 0);
        //Vamos a la última pagina
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@class, 'page-link')]", 3);
        //Borramos la primera oferta de la pagina
        PO_PrivateView.checkViewAndClick(driver, "free",
                "//h6[contains(text(), 'Producto 138')]/following-sibling::*/a[contains(@href, 'offer/delete')]",0);
        //Comprobamos que ha desaparecido la oferta 'Producto 138'
        SeleniumUtils.waitTextIsNotPresentOnPage(driver, "Producto 138", PO_View.getTimeout());
        // Hacemos logout
        PO_PrivateView.logout(driver);
    }

    /**
     * PR20. Buscra ofertas con el campo de texto vacío y comprobar que se muetran todas las ofertas.
     * Realizada por: David
     */
    @Test
    @Order(20)
    public void PR20() {
        // Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user10@email.com", "user10");
        //Pinchamos en la opción de menú de ofertas: //li[contains(@id, 'offers-menu')]/a
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]/a", 0);
        //Esperamos a que aparezca la opción de mostrar oferta: //a[contains(@href, 'offer/searchList')]
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/searchList')]", 0);
        //Dejamos el campo de busqueda vacio y buscamos
        PO_PrivateView.makeSearch(driver,"");
        //Guardamos los elemento de la primera pagina
        List<WebElement> offerList = SeleniumUtils.waitLoadElementsBy(driver, "free",
                "//div[contains(@class, 'card border-dark mb-3')]", PO_View.getTimeout());
        //Vamos a la segunda página
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@class, 'page-link')]", 2);
        offerList.addAll(SeleniumUtils.waitLoadElementsBy(driver, "free",
                "//div[contains(@class, 'card border-dark mb-3')]", PO_View.getTimeout()));
        //Recorremos las paginas guardando las ofertas
        for (int i = 0; i < 26; i++) {
            PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@class, 'page-link')]", 3);
            offerList.addAll(SeleniumUtils.waitLoadElementsBy(driver, "free",
                    "//div[contains(@class, 'card border-dark mb-3')]", PO_View.getTimeout()));
        }
        // Comprobamos que se encuentren todas las ofertas
        Assertions.assertEquals(140, offerList.size());
        // Hacemos logout
        PO_PrivateView.logout(driver);
    }

    /**
     * PR21. Buscra ofertas con el campo de texto que no existe y comprobar que no se muetran ofertas.
     * Realizada por: David
     */
    @Test
    @Order(21)
    public void PR21() {
        // Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user10@email.com", "user10");
        //Pinchamos en la opción de menú de ofertas: //li[contains(@id, 'offers-menu')]/a
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]/a", 0);
        //Esperamos a que aparezca la opción de mostrar oferta: //a[contains(@href, 'offer/searchList')]
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/searchList')]", 0);
        //Dejamos el campo de busqueda vacio y buscamos
        PO_PrivateView.makeSearch(driver,"SistemasDistribuidos");
        //Guardamos los elemento de la primera pagina
        List<WebElement> offerList = driver.findElements(By.xpath("//div[contains(@class, 'card border-dark mb-3')]"));
        // Comprobamos que se encuentren todas las ofertas
        Assertions.assertEquals(0, offerList.size());
        // Hacemos logout
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

    @Test
    @Order(28)
    public void PR28(){
        // Iniciamos sesión como administrador
        PO_PrivateView.login(driver, "pepe@email.com", "pepe");
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'conversation/')]",0);

        List<WebElement> conversationLinks = driver.findElements(By.xpath("//a[contains(@href, 'conversation/')]"));

        conversationLinks.get(0).click();

        PO_PrivateView.checkElement(driver,PO_HomeView.getP().getString("msg.conversation.list.info", PO_Properties.getSPANISH()));

        PO_PrivateView.checkElement(driver,"Cartera");

        List<WebElement> tableRows = driver.findElements(By.xpath("//table[@id='tableConversations']/tbody/tr"));

        Assertions.assertEquals(1,tableRows.size());
    }



}


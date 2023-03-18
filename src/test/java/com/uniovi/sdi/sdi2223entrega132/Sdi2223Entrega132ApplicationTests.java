package com.uniovi.sdi.sdi2223entrega132;

import com.uniovi.sdi.sdi2223entrega132.pageobjects.*;
import com.uniovi.sdi.sdi2223entrega132.repositories.OffersRepository;
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

import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sdi2223Entrega132ApplicationTests {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private OffersRepository offersRepository;
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
    }

    private void reiniciarDatos(){
        usersRepository.deleteAll();
        offersRepository.deleteAll();
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
        String checkText = "¡Bienvenido a UrWalletPop!";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
        reiniciarDatos();
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
        String checkText = "¡Bienvenido a UrWalletPop!";
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

    /**
     * PR12. Ir a la lista de usuarios, borrar el primer usuario de la lista, comprobar que la lista se actualiza
     * y dicho usuario desaparece.
     * Realizada por: Israel
     */
    @Test
    @Order(12)
    public void PR12(){
        // Iniciamos sesión como administrador
        PO_PrivateView.login(driver, "admin@email.com", "admin");
        //Seleccionamos el dropdown de gestion de usuarios
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]", 0);
        //Seleccionamos el enlace de gestión de usuarios
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@class, 'dropdown-item')]", 0);
        //Obtenemos el texto que debería aparecer en la vista de gestión de usuarios
        String checkText = PO_HomeView.getP().getString("msg.user.list.info",PO_Properties.getSPANISH());
        //Comprobamos que el texto aparece
        PO_PrivateView.checkElement(driver, checkText);
        //Obtenemos la primera fila que contenga un checkBox (ya que el admin no se puede eliminar)
        WebElement firstRowWithCheckbox = driver.findElement(By.xpath("//table//tr[td/input[@type='checkbox']][1]"));
        //Obtenemos la celda del checkbox
        WebElement checkBoxCell = firstRowWithCheckbox.findElement(By.xpath(".//input[@type='checkbox']"));
        //Clickamos el checkbox
        checkBoxCell.click();
        WebElement emailOfFirstRow = firstRowWithCheckbox.findElement(By.xpath(".//td[1]"));
        //Guardamos el texto para confirmar que se ha eliminado correctamente
        String checkTextAfterDelete = emailOfFirstRow.getText();
        //Obtenemos el botón que elimina los usuarios seleccionados
        WebElement deleteSubmitButton = driver.findElement(By.id("delete-users"));
        //Hacemos click sobre el boton de eliminar
        deleteSubmitButton.click();
        //Volvemos a obtener la primera fila de la tabla para comprobar que se ha eliminado correctamente
        firstRowWithCheckbox = driver.findElement(By.xpath("//table//tr[td/input[@type='checkbox']][1]"));
        //Obtenemos la primera celda
        emailOfFirstRow = firstRowWithCheckbox.findElement(By.xpath(".//td[1]"));
        //Guardamos el texto para confirmar que se ha eliminado correctamente
        String actualText = emailOfFirstRow.getText();
        //Comprobamos que los textos son diferentes
        Assertions.assertNotEquals(checkTextAfterDelete,actualText);
        PO_PrivateView.logout(driver);
        reiniciarDatos();
    }

    /**
     * PR13. Ir a la lista de usuarios, borrar el último usuario de la lista, comprobar que la lista se actualiza
     * y dicho usuario desaparece.
     * Realizada por: Israel
     */
    @Test
    @Order(13)
    public void PR13(){
        // Iniciamos sesión como administrador
        PO_PrivateView.login(driver, "admin@email.com", "admin");
        //Seleccionamos el dropdown de gestion de usuarios
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]", 0);
        //Seleccionamos el enlace de gestión de usuarios
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@class, 'dropdown-item')]", 0);
        //Obtenemos el ultimo elemento de la tabla
        WebElement lastRow = PO_PrivateView.goLastPageGetElement(driver);

        //Obtenemos la celda del checkbox
        WebElement checkBoxCell = lastRow.findElement(By.xpath(".//input[@type='checkbox']"));
        //Clickamos el checkbox
        checkBoxCell.click();
        //Obtenemos la primera celda de la tabla
        WebElement email = lastRow.findElement(By.xpath(".//td[1]"));
        //Guardamos el texto para confirmar que se ha eliminado correctamente
        String checkTextBeforeDelete = email.getText();

        //Obtenemos el botón que elimina los usuarios seleccionados
        WebElement deleteSubmitButton = driver.findElement(By.id("delete-users"));
        //Hacemos click sobre el boton de eliminar
        deleteSubmitButton.click();
        //Obtenemos la ultima fila
        lastRow = PO_PrivateView.goLastPageGetElement(driver);
        //Obtenemos el correo de la ultima fila
        email = lastRow.findElement(By.xpath(".//td[1]"));
        //Guardamos el texto para confirmar que se ha eliminado correctamente
        String checkTextAfterDelete = email.getText();

        Assertions.assertNotEquals(checkTextBeforeDelete,checkTextAfterDelete);
        PO_PrivateView.logout(driver);
        reiniciarDatos();
    }

    /**
     * PR14. Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la lista se actualiza y dichos
     * usuarios desaparecen.
     * Realizada por: Israel
     */
    @Test
    @Order(14)
    public void PR14(){
        // Iniciamos sesión como administrador
        PO_PrivateView.login(driver, "admin@email.com", "admin");
        //Seleccionamos el dropdown de gestion de usuarios
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]", 0);
        //Seleccionamos el enlace de gestión de usuarios
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@class, 'dropdown-item')]", 0);

        //Obtenemos las 3 primeras filas de la tabla que contengan checkbox (menos la del admin)
        List<String> textsBeforeDelete = PO_PrivateView.clickAndGetFirstCellsOfTable(driver,3);

        //Obtenemos el botón que elimina los usuarios seleccionados
        WebElement deleteSubmitButton = driver.findElement(By.id("delete-users"));
        //Hacemos click sobre el boton de eliminar
        deleteSubmitButton.click();

        //Obtenemos las 3 primeras filas de la tabla que contengan checkbox (menos la del admin)
        List<String> textsAfterDelete = PO_PrivateView.clickAndGetFirstCellsOfTable(driver,3);
        //Comprobamos que se han eliminado correctamente
        for(int i=0;i<3;i++){
            Assertions.assertNotEquals(textsBeforeDelete.get(i),textsAfterDelete.get(i));
        }
        PO_PrivateView.logout(driver);
        reiniciarDatos();
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
        reiniciarDatos();
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
        reiniciarDatos();
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
        reiniciarDatos();
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

    /**
     * PR22. Sobre una búsqueda determinada (a elección del desarrollador),
     * comprar una oferta que deja un saldo positivo en el contador del comprador.
     * Comprobar que el contador se actualiza correctamente en la vista del comprador.
     * Realizada por: Álvaro
     */
    @Test
    @Order(22)
    public void PR22() {
        //Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user13@email.com", "user13");
        //Pinchamos en la opción de menú de ofertas: //li[contains(@id, 'offers-menu')]/a
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]/a", 0);
        //Esperamos a que aparezca la opción de mostrar oferta: //a[contains(@href, 'offer/searchList')]
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/searchList')]", 0);
        //Hacemos una busqueda
        PO_PrivateView.makeSearch(driver,"117");
        //Compramos la oferta
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//button[contains(text(), 'Comprar')]");
        elements.get(0).click();
        //Lo comparamos con el precio restado
        elements = PO_View.checkElementBy(driver, "free", "//span[contains(@class, 'badge badge-secondary')]");
        double result= Double.parseDouble(elements.get(0).getText());
        Assertions.assertEquals(result,30.31);

        //Cierro sesion
        PO_PrivateView.logout(driver);
    }

    /**
     * PR23. Sobre una búsqueda determinada (a elección del desarrollador),
     * comprar una oferta que deja un saldo 0 en el contador del comprador.
     * Comprobar que el contador se actualiza correctamente en la vista del comprador.
     * Realizada por: Álvaro
     */
    @Test
    @Order(23)
    public void PR23() {
        //Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user11@email.com", "user11");
        //Pinchamos en la opción de menú de ofertas: //li[contains(@id, 'offers-menu')]/a
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]/a", 0);
        //Esperamos a que aparezca la opción de mostrar oferta: //a[contains(@href, 'offer/searchList')]
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/searchList')]", 0);
        //Hacemos una busqueda
        PO_PrivateView.makeSearch(driver,"130");
        //Compramos la oferta
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//button[contains(text(), 'Comprar')]");
        elements.get(0).click();
        //Lo comparamos con el precio restado
        elements = PO_View.checkElementBy(driver, "free", "//span[contains(@class, 'badge badge-secondary')]");
        double result= Double.parseDouble(elements.get(0).getText());
        Assertions.assertEquals(result,0.00);

        //Cierro sesion
        PO_PrivateView.logout(driver);
    }

    /**
     * PR24. Sobre una búsqueda determinada (a elección del desarrollador),
     * intentar comprar una oferta que esté por encima de saldo disponible del comprador.
     * Y comprobar que se muestra el mensaje de saldo no suficiente
     * Realizada por: Álvaro
     */
    @Test
    @Order(24)
    public void PR24() {
        //Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user13@email.com", "user13");
        //Pinchamos en la opción de menú de ofertas: //li[contains(@id, 'offers-menu')]/a
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]/a", 0);
        //Esperamos a que aparezca la opción de mostrar oferta: //a[contains(@href, 'offer/searchList')]
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/searchList')]", 0);
        //Hacemos una busqueda
        PO_PrivateView.makeSearch(driver,"25");
        //Compramos la oferta
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//button[contains(text(), 'Comprar')]");
        elements.get(0).click();
        //Se muestra el mensaje de error
        List<WebElement> result = PO_View.checkElementByKey(driver, "error.amount",
                PO_Properties.getSPANISH());
        String checkText = PO_HomeView.getP().getString("error.amount",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
        //Cierro sesion
        PO_PrivateView.logout(driver);
    }
    /**
     * PR25. Ir a la opción de ofertas compradas del usuario y mostrar la lista.
     * Comprobar que aparecen las ofertas que deben aparecer
     * Realizada por: Álvaro
     */
    @Test
    @Order(25)
    public void PR25() {
        //Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user12@email.com", "user12");
        //Pinchamos en la opción de menú de ofertas: //li[contains(@id, 'offers-menu')]/a
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]/a", 0);
        //Esperamos a que aparezca la opción de mostrar oferta: //a[contains(@href, 'offer/searchList')]
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/searchList')]", 0);
        //Hacemos una busqueda
        PO_PrivateView.makeSearch(driver,"20");
        //Compramos la oferta
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//button[contains(text(), 'Comprar')]");
        elements.get(0).click();
        //Vamos a la pestaña de ofertas compradas
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]/a", 0);
        //Esperamos a que aparezca la opción de ofertas compradas: //a[contains(@href, 'offer/searchList')]
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/purchasedList')]", 0);
        List<WebElement> offerList = driver.findElements(By.xpath("//div[contains(@class, 'card border-dark mb-3')]"));
        // Comprobamos que se encuentren todas las ofertas
        Assertions.assertEquals(1, offerList.size());
        //Cierro sesion
        PO_PrivateView.logout(driver);
    }
    /**
     * PR26. Sobre una búsqueda determinada de ofertas (a elección de desarrollador), enviar un mensaje
     * a una oferta concreta. Se abriría dicha conversación por primera vez. Comprobar que el mensaje aparece
     * en la conversación.
     * Realizada por: Israel
     */
    @Test
    @Order(26)
    public void PR26(){
        // Iniciamos sesión como un usuario estandar
        PO_PrivateView.login(driver, "user05@email.com", "user05");
        //Seleccionamos el dropdown de ofertas
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]", 0);
        //Seleccionamos el enlace de ver todas las ofertas
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@class, 'dropdown-item')]", 3);
        //Comprobamos que esta el mensaje de buscar ofertas
        PO_PrivateView.checkElement(driver,PO_HomeView.getP().getString("msg.nav.search", PO_Properties.getSPANISH()));
        //Obtenemos el input de busqueda
        WebElement searchText = driver.findElement(By.xpath("//input[@class='form-control']"));
        //Lo seleccionamos
        searchText.click();
        //Borramos su contenido
        searchText.clear();
        //Escribimos el producto que queremos buscar
        searchText.sendKeys("Producto 100");
        //Confirmamos la busqueda
        searchText.sendKeys(Keys.ENTER);
        //Obtenemos los resultados de la busqueda (esperamos ya que a veces puede continuar la ejecucion antes de que se actualice)
        List<WebElement> cards = SeleniumUtils.waitLoadElementsBy(driver, "class", "card-body",10);
        //Comprobamos que solo hay un producto
        Assertions.assertEquals(1, cards.size());
        //Seleccionamos la opcion para establecer una conversacion
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'conversation/')]",1);
        //Comprobamos que estamos en el chat
        PO_PrivateView.checkElement(driver,PO_HomeView.getP().getString("msg.conversation.header", PO_Properties.getSPANISH()));
        //Obtenemos el input para escribir un mensaje
        WebElement messageInput = driver.findElement(By.xpath("//input[@class='form-control']"));
        //Escribimos el mensaje
        messageInput.click();
        messageInput.clear();
        messageInput.sendKeys("Hola");
        //Obtenemos el boton para enviar el mensaje
        WebElement sendButton = driver.findElement(By.xpath("//button[@type='submit']"));
        //Enviamos el mensaje
        sendButton.click();
        //Obtenemos los mensajes de la conversacion
        List<WebElement> messages = driver.findElements(By.xpath("//p[@class='small p-2 me-3 mb-3 text-white rounded-3 bg-dark']"));
        //Comprobamos que el mensaje se ha enviado
        Assertions.assertEquals(1,messages.size());

        PO_PrivateView.logout(driver);

        reiniciarDatos();
    }
    /**
     * PR27. Enviar un mensaje a una conversación ya existente accediendo desde el botón/enlace
     * “Conversación”. Comprobar que el mensaje aparece en la conversación
     * Realizada por: Israel
     */
    @Test
    @Order(27)
    public void PR27(){
        // Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user05@email.com", "user05");
        //Accedemos a la pestaña de conversaciones
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'conversation/')]",0);
        //Comprobamos que hemos accedido correctamente
        PO_PrivateView.checkElement(driver,PO_HomeView.getP().getString("msg.conversation.list.info", PO_Properties.getSPANISH()));
        //Comprobamos que el titulo de la oferta es el correcto
        PO_PrivateView.checkElement(driver,"Producto 139");
        //Obtenemos los enlaces a las conversaciones
        List<WebElement> conversationLinks = driver.findElements(By.xpath("//a[contains(@href, 'conversation/')]"));
        //Seleccionamos la primera que aparece en la tabla
        conversationLinks.get(1).click();
        //Comprobamos que hemos accedido a la conversacion
        PO_PrivateView.checkElement(driver,PO_HomeView.getP().getString("msg.conversation.header", PO_Properties.getSPANISH()));
        //Obtenemos el input para enviar un mensaje
        WebElement messageInput = driver.findElement(By.xpath("//input[@class='form-control']"));
        //Lo seleccionamos y escribimos un mensaje
        messageInput.click();
        messageInput.clear();
        messageInput.sendKeys("Hola");
        //Obtenemos el boton para enviar el mensaje
        WebElement sendButton = driver.findElement(By.xpath("//button[@type='submit']"));
        //Lo enviamos
        sendButton.click();
        //Obtenemos los mensajes actuales
        List<WebElement> messages = driver.findElements(By.xpath("//p[@class='small p-2 me-3 mb-3 text-white rounded-3 bg-dark']"));
        //Comprobamos que el texto del mensaje es el correcto
        Assertions.assertEquals(messages.get(0).getText(),"Hola");
        //Comprobamos que el numero de mensajes es el correcto
        Assertions.assertEquals(1,messages.size());

        PO_PrivateView.logout(driver);
        reiniciarDatos();
    }

    /**
     * PR28. Mostrar el listado de conversaciones ya abiertas. Comprobar que el listado contiene la
     * cantidad correcta de conversaciones.
     * Realizada por: Israel
     */
    @Test
    @Order(28)
    public void PR28(){
        // Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user05@email.com", "user05");
        //Accedemos a la pestaña de conversaciones
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'conversation/')]",0);
        //Comprobamos que hemos accedido correctamente
        PO_PrivateView.checkElement(driver,PO_HomeView.getP().getString("msg.conversation.list.info", PO_Properties.getSPANISH()));
        //Obtenemos el numero de conversaciones del usuario
        List<WebElement> tableRows = driver.findElements(By.xpath("//table[@id='tableConversations']/tbody/tr"));
        //Comprobamos que el numero es el correcto
        Assertions.assertEquals(1,tableRows.size());
    }

    /**
     * PR37. Al crear una oferta, marcar dicha oferta como destacada y a continuación comprobar:
     * Comprobar que aparecen las ofertas que deben aparecer
     * que aparece en el listado de ofertas destacadas para los usuarios
     * y que el saldo del usuario se actualiza adecuadamente en la vista del ofertante
     * Realizada por: Álvaro
     */
    @Test
    @Order(37)
    public void PR37() {
        // Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user08@email.com", "user08");

        //Pinchamos en la opción de menú de ofertas: //li[contains(@id, 'offers-menu')]/a
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]/a", 0);
        //Esperamos a que aparezca la opción de añadir oferta: //a[contains(@href, 'offer/add')]
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/add')]", 0);

        // Rellenamos el formulario de alta de oferta con datos validos
        PO_PrivateView.fillFormAddOfferFeatured(driver, "Prueba37", "PruebaDescripcion37", "0.37");

        // Comprobamos que la oferta recien añadida sale en la lista de ofertas propias
        // del usuario
        //PO_PrivateView.checkElement(driver, "Prueba37");
        //PO_PrivateView.checkElement(driver, "PruebaDescripcion37");
        //PO_PrivateView.checkElement(driver, "0.37 EUR");

        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//span[contains(@class, 'badge badge-secondary')]");
        double result= Double.parseDouble(elements.get(0).getText());
        Assertions.assertEquals(result,80.00);
        // Hacemos logout
        PO_PrivateView.logout(driver);
        reiniciarDatos();
    }

    /**
     * PR38.Sobre el listado de ofertas de un usuario con 20 euros (o más) de saldo, pinchar en el enlace
     * Destacada y a continuación comprobar: que aparece en el listado de ofertas destacadas para los usuarios
     * y que el saldo del usuario se actualiza adecuadamente en la vista del ofertante (-20).
     * Realizada por: Álvaro
     */
    @Test
    @Order(38)
    public void PR38() {
        // Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user01@email.com", "user01");

        //Pinchamos en la opción de menú de ofertas: //li[contains(@id, 'offers-menu')]/a
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]/a", 0);
        //Esperamos a que aparezca la opción de añadir oferta: //a[contains(@href, 'offer/add')]
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/ownedList')]", 0);

        //Destacamos la oferta
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//button[contains(text(), 'Destacar')]");
        elements.get(0).click();

        //Comprobamos que está en el listado de destacadas
        //Destacamos la oferta
        //elements=PO_View.checkElementBy(driver, "free", "//td[contains(text(), 'Producto 0')]");
        //Assertions.assertEquals("Producto 0", elements.get(0).getText());

        //Comprobamos si se actualizo el saldo
        elements = PO_View.checkElementBy(driver, "free", "//span[contains(@class, 'badge badge-secondary')]");
        double result= Double.parseDouble(elements.get(0).getText());
        Assertions.assertEquals(result,80.00);
        // Hacemos logout
        PO_PrivateView.logout(driver);
    }

    /**
     * PR39. Sobre el listado de ofertas de un usuario con menos de 20 euros de saldo, pinchar en el
     * enlace Destacada  y a continuación comprobar que se muestra el mensaje de saldo insuficiente.
     * Realizada por: Álvaro
     */
    @Test
    @Order(39)
    public void PR39() {
        // Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user04@email.com", "user04");

        //Pinchamos en la opción de menú de ofertas: //li[contains(@id, 'offers-menu')]/a
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]/a", 0);
        //Esperamos a que aparezca la opción de añadir oferta: //a[contains(@href, 'offer/add')]
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/ownedList')]", 0);

        //Destacamos la oferta
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//button[contains(text(), 'Destacar')]");
        elements.get(0).click();

        //Comprobamos que salta un error
        List<WebElement> result = PO_View.checkElementByKey(driver, "error.amount",
                PO_Properties.getSPANISH());
        String checkText = PO_HomeView.getP().getString("error.amount",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
        // Hacemos logout
        PO_PrivateView.logout(driver);
    }

    /**
     * PR40. Desde el formulario de dar de alta ofertas, crear una oferta con datos válidos y una imagen
     * adjunta. Comprobar que en el listado de ofertas propias aparece la imagen adjunta junto al resto de datos
     * de la oferta.
     * Realizada por: Omar
     */
    @Test
    @Order(40)
    public void PR40(){
        // Iniciamos sesión como usuario estandar
        PO_PrivateView.login(driver, "user15@email.com", "user15");

        //Pinchamos en la opción de menú de ofertas: //li[contains(@id, 'offers-menu')]/a
        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@class, 'nav-item dropdown')]/a", 0);
        //Esperamos a que aparezca la opción de añadir oferta: //a[contains(@href, 'offer/add')]
        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/add')]", 0);

        // Rellenamos el formulario de alta de oferta con datos validos, en este adjuntamos una imagen
        String absolutePath = FileSystems.getDefault().getPath("src\\test\\java\\rtx4080.png").normalize().toAbsolutePath().toString();;
        PO_PrivateView.fillFormAddOffer(driver, "PruebaTitulo", absolutePath, "PruebaDescripcion", "0.21");

        // Comprobamos que la oferta recien añadida sale en la lista de ofertas propias
        // del usuario
        PO_PrivateView.checkElement(driver, "PruebaTitulo");
        // Obtenemos el elemento que contiene la imagen
        List<WebElement> image = driver.findElements(By.xpath("//div[contains(@class, 'card-img-top')]"));
        PO_PrivateView.checkElement(driver, "PruebaDescripcion");
        PO_PrivateView.checkElement(driver, "0.21 EUR");

        // Comprobamos que no está vacío
        Assertions.assertFalse(image.isEmpty());
        // Hacemos logout
        PO_PrivateView.logout(driver);
        reiniciarDatos();
    }

    /**
     * PR41. Crear una oferta con datos válidos y sin una imagen adjunta. Comprobar que la oferta se ha
     * creado con éxito, ya que la imagen no es obligatoria
     * Realizada por: Omar
     */
    @Test
    @Order(41)
    public void PR41(){
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
        //Intentamos obtener el elemento que contendria a la imagen
        List<WebElement> image = driver.findElements(By.xpath("//div[contains(@class, 'card-img-top')]"));
        PO_PrivateView.checkElement(driver, "PruebaDescripcion");
        PO_PrivateView.checkElement(driver, "0.21 EUR");

        //Comprobamos que no tiene imagen
        Assertions.assertTrue(image.isEmpty());

        // Hacemos logout
        PO_PrivateView.logout(driver);
        reiniciarDatos();
    }

}


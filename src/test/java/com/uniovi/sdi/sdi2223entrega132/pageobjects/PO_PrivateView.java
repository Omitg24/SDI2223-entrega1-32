package com.uniovi.sdi.sdi2223entrega132.pageobjects;

import com.uniovi.sdi.sdi2223entrega132.util.SeleniumUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class PO_PrivateView extends PO_NavView {

    /**
     * Método para rellenar el formulario de añadir ofertas
     * @param driver driver
     * @param titlep titulo de la oferta
     * @param descriptionp descripcion de la oferta
     * @param pricep precio de la oferta
     */
    static public void fillFormAddOffer(WebDriver driver, String titlep, String descriptionp, String pricep) {
        //Esperamos 5 segundo a que carge el DOM porque en algunos equipos falla
        SeleniumUtils.waitSeconds(driver, 5);
        // Rellenamos el formulario con los datos recibidos como paramteros.
        WebElement title = driver.findElement(By.name("title"));
        title.click();
        title.clear();
        title.sendKeys(titlep);
        WebElement description = driver.findElement(By.name("description"));
        description.click();
        description.clear();
        description.sendKeys(descriptionp);
        WebElement price = driver.findElement(By.name("price"));
        price.click();
        price.clear();
        price.sendKeys(pricep);

        // Pulsamos el botón para enviar el formulario.
        By boton = By.className("btn");
        driver.findElement(boton).click();
    }

    /**
     * Método para realizar el login de un usuario
     * @param driver driver
     * @param dni dni del usuario
     * @param password contraseña del usuario
     */
    static public void login(WebDriver driver, String dni, String password) {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-dark");
        PO_LoginView.fillLoginForm(driver, dni, password);
        PO_View.checkElementBy(driver, "text", dni);
    }

    /**
     * Método para desconectar a un usuario
     * @param driver driver
     */
    static public void logout(WebDriver driver) {
        String loginText = PO_HomeView.getP().getString("msg.signup", PO_Properties.getSPANISH());
        PO_PrivateView.clickOption(driver, "logout", "text", loginText);
    }

    /**
     * Método que comprueba que un texto se encuentra en la vista
     * @param driver driver
     * @param checkText texto a buscar
     */
    static public void checkElement(WebDriver driver, String checkText) {
        List<WebElement> elements = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, elements.get(0).getText());
    }

    /**
     * Método que compruba si un elemento se encuentra en la vista y realiza un click
     * @param driver driver
     * @param type tipo del elemento
     * @param text Xpath del elemento
     * @param index indice del objeto que coincide con los datos pasados
     */
    static public void checkViewAndClick(WebDriver driver,String type,String text,int index){
        List<WebElement> elements = checkElementBy(driver, type, text);
        elements.get(index).click();
    }

    /**
     * Método que rellena el buscador de ofertas con el texto y realiza la busqueda
     * @param driver driver
     * @param searchText texto a buscar
     */
    static public void makeSearch(WebDriver driver, String searchText) {
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//*[@id=\"searchTextForm\"]/div/div/input");
        elements.get(0).click();
        elements.get(0).sendKeys(searchText);
        elements = PO_View.checkElementBy(driver, "free", "//*[@id=\"searchTextForm\"]/div/button");
        elements.get(0).click();
    }

    /**
     * Metodo que accede a la ultima pagina de una tabla y devuelve la ultima fila
     * @param driver
     * @return la ultima tabla
     */
    public static WebElement goLastPageGetElement(WebDriver driver) {
        //Obtenemos el enlace de la ultima pagina
        WebElement lastPageLink = driver.findElement(By.xpath("(//li[@class='page-item'])[last()]"));
        //Vamos a la ultima pagina
        lastPageLink.click();
        //Obtenemos la última fila de la tabla
        WebElement lastRow = driver.findElement(By.xpath("//table//tbody//tr[last()]"));
        return lastRow;
    }

    /**
     * Metodo que obtiene el texto de la primera celda de las n primeras filas (que contengan un checkbox) pasadas por parametro
     * @param driver
     * @param n las filas de las que queremos obtener la primera celda
     * @return las primeras celdas de las n primeras filas
     */
    public static List<String> clickAndGetFirstCellsOfTable(WebDriver driver,int n) {
        List<WebElement> rows = driver.findElements(By.xpath("//table//tr[td/input[@type='checkbox']]"));

        List<String> textsBeforeDelete = new ArrayList<String>();
        for(int i=0;i<n;i++){
            //Obtenemos la celda del checkbox
            WebElement checkBoxCell = rows.get(i).findElement(By.xpath(".//input[@type='checkbox']"));
            //Clickamos el checkbox
            checkBoxCell.click();
            //Obtenemos la primera celda de la tabla
            WebElement email = rows.get(i).findElement(By.xpath(".//td[1]"));
            //Lo añadimos a la lista para comprobar que se han borrado correctamente
            textsBeforeDelete.add(email.getText());
        }
        return textsBeforeDelete;
    }
}

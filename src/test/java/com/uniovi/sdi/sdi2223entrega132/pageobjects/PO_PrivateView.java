package com.uniovi.sdi.sdi2223entrega132.pageobjects;

import com.uniovi.sdi.sdi2223entrega132.util.SeleniumUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class PO_PrivateView extends PO_NavView {
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

    static public void login(WebDriver driver, String dni, String password) {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-dark");
        PO_LoginView.fillLoginForm(driver, dni, password);
        PO_View.checkElementBy(driver, "text", dni);
    }
    static public void logout(WebDriver driver) {
        String loginText = PO_HomeView.getP().getString("msg.login", PO_Properties.getSPANISH());
        PO_PrivateView.clickOption(driver, "logout", "text", loginText);
    }

    static public void checkElement(WebDriver driver, String checkText) {
        List<WebElement> elements = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, elements.get(0).getText());
    }

    static public void checkViewAndClick(WebDriver driver,String type,String text,int index){
        List<WebElement> elements = checkElementBy(driver, type, text);
        elements.get(index).click();
    }
}

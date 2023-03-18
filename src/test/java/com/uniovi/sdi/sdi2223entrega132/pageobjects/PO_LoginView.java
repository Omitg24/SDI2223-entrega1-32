package com.uniovi.sdi.sdi2223entrega132.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_LoginView extends PO_NavView {
    /**
     * Método que rellena el formulario de logging
     *
     * @param driver    driver
     * @param usernamep usuario
     * @param passwordp contraseña del usuario
     */
    static public void fillLoginForm(WebDriver driver, String usernamep, String passwordp) {
        WebElement username = driver.findElement(By.name("username"));
        username.click();
        username.clear();
        username.sendKeys(usernamep);
        WebElement password = driver.findElement(By.name("password"));
        password.click();
        password.clear();
        password.sendKeys(passwordp);
        By boton = By.className("btn");
        driver.findElement(boton).click();
    }

}

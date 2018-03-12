package com.listoplan.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.listoplan.controllers.UsuariosController;
import com.listoplan.utils.Statics;

@SpringBootApplication
@ComponentScan(basePackageClasses=UsuariosController.class)
public class App {

    public static void main(String[] args) {
    		Statics.configFile=args[0];
        SpringApplication.run(App.class, args);
    }
}

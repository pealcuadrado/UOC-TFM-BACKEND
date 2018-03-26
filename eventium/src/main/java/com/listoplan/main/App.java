package com.listoplan.main;


import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.listoplan.controllers.GruposController;
import com.listoplan.controllers.ListasController;
import com.listoplan.controllers.NotasController;
import com.listoplan.controllers.UsuariosController;
import com.listoplan.mysqlcontroller.MysqlManager;
import com.listoplan.utils.Statics;



@SpringBootApplication
@ComponentScan(basePackageClasses=UsuariosController.class)
@ComponentScan(basePackageClasses=GruposController.class)
@ComponentScan(basePackageClasses=NotasController.class)
@ComponentScan(basePackageClasses=ListasController.class)
public class App {

    public static void main(String[] args) {
    		Logger logger = Logger.getLogger(App.class);
    		logger.info("Aplicación arrancada");
    		Statics.configFile=args[0];
    		MysqlManager.getInstance().connect();
        SpringApplication.run(App.class, args);
    }
}

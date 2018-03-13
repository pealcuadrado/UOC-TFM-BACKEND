package com.listoplan.utils;

import java.io.File;


import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ConfigurationManager {
	private static ConfigurationManager configurationManager=null;
	private Configuration config;
	
	private ConfigurationManager(){
		Configurations conf= new Configurations();
		try
		{
		    this.config = conf.properties(new File(Statics.configFile));
		    System.out.println("Fichero de propiedades accesible correctamente");
		}
		catch (ConfigurationException cex)
		{
		    System.out.println("Error al leer el fichero de propiedades");
		}
	}
	
	public static ConfigurationManager getInstance(){
		if (configurationManager==null){
			configurationManager= new ConfigurationManager();
		}
		return configurationManager;
	}
	
	public Configuration getCM(){
		return this.config;
	}
}

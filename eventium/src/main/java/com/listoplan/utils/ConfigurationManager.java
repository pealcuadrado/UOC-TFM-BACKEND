package com.listoplan.utils;

import java.io.File;


import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.jboss.logging.Logger;

public class ConfigurationManager {
	private static Logger logger=Logger.getLogger(ConfigurationManager.class);
	private static ConfigurationManager configurationManager=null;
	private Configuration config;
	
	private ConfigurationManager(){
		Configurations conf= new Configurations();
		try
		{
		    this.config = conf.properties(new File(Statics.configFile));
		    logger.info("Fichero de propiedades accesible correctamente");
		}
		catch (ConfigurationException cex)
		{
		    logger.error("Error al leer el fichero de propiedades",cex);
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

package com.qualitas.portal.accountfraudesapi.configuracion.permitido;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class HostsPermitidos {

    public static List<String> obtenerListaHostPermitidos(){

        //return Arrays.asList("/*");

        Properties properties = new Properties();

        try{
            InputStream hostsStream = HostsPermitidos.class.getClassLoader().getResourceAsStream("config/hosts.properties");

            if(hostsStream == null)
                throw new RuntimeException("Ocurrio un problema con la conexion");

            properties.load(hostsStream);

            return properties.stringPropertyNames().stream().map(properties::getProperty).collect(Collectors.toList());

        }catch(IOException ignore){
            throw new RuntimeException("Ocurrio un problema con la conexion");
        }
    }

}

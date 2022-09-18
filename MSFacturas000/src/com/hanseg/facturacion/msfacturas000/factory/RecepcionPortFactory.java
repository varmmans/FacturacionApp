package com.hanseg.facturacion.msfacturas000.factory;

import com.hanseg.facturacion.msfacturas000.util.ResourceUtil;

import ec.gob.sri.ws.recepcion.RecepcionComprobantesOffline;

import ec.gob.sri.ws.recepcion.RecepcionComprobantesOfflineService;

import java.net.MalformedURLException;

import java.util.Objects;

import java.net.URL;

import javax.validation.constraints.NotNull;

public class RecepcionPortFactory {

    private static RecepcionPortFactory factory = null;
    private static RecepcionComprobantesOfflineService recepcionService;
    private static RecepcionComprobantesOffline recepcionPort;

    public RecepcionPortFactory() {
    }

    public static synchronized RecepcionPortFactory getInstance() {
        if (Objects.isNull(factory))
            factory = new RecepcionPortFactory();
        return factory;
    }

    public RecepcionComprobantesOffline createRecepcionPort(@NotNull String env) throws MalformedURLException {
        if (env.equalsIgnoreCase("TEST")) {
            String wsdl = ResourceUtil.getResource("wsdl.sri.recepcion.test");
            URL url = new URL(wsdl);
            recepcionService = new RecepcionComprobantesOfflineService(url);
            recepcionPort = recepcionService.getRecepcionComprobantesOfflinePort();
            return recepcionPort;
        }
        return null;
    }
}

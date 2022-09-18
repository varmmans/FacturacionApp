package com.hanseg.facturacion.msfacturas000.factory;

import com.hanseg.facturacion.msfacturas000.util.ResourceUtil;

import ec.gob.sri.ws.autorizacion.AutorizacionComprobantesOffline;
import ec.gob.sri.ws.autorizacion.AutorizacionComprobantesOfflineService;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Objects;

import javax.validation.constraints.NotNull;

public class AutorizacionPortFactory {

    private static AutorizacionPortFactory factory = null;
    private static AutorizacionComprobantesOfflineService autorizacionService;
    private static AutorizacionComprobantesOffline autorizacionPort;

    public AutorizacionPortFactory() {
    }

    public static synchronized AutorizacionPortFactory getInstance() {
        if (Objects.isNull(factory))
            factory = new AutorizacionPortFactory();
        return factory;
    }

    public AutorizacionComprobantesOffline createAutorizacionPort(@NotNull String env) throws MalformedURLException {
        if (env.equalsIgnoreCase("TEST")) {
            String wsdl = ResourceUtil.getResource("wsdl.sri.autorizacion.test");
            URL url = new URL(wsdl);
            autorizacionService = new AutorizacionComprobantesOfflineService(url);
            autorizacionPort = autorizacionService.getAutorizacionComprobantesOfflinePort();
            return autorizacionPort;
        }
        return null;
    }
}

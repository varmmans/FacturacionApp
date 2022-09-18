package com.hanseg.facturacion.msfacturas001.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceUtil {
    private static String resourceBundle = "MSFacturas001Bundle";

    public ResourceUtil() {
    }

    public ResourceUtil(String bundle) {
        resourceBundle = bundle;
    }

    public static String getResource(String resource) {
        ResourceBundle resources = ResourceBundle.getBundle(resourceBundle, Locale.ROOT);
        return resources.getString(resource);
    }
}

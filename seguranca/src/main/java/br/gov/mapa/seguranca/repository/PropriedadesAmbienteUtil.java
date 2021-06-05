package br.gov.mapa.seguranca.repository;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public final class PropriedadesAmbienteUtil {

    private static final Properties MAPA_CONFIG_PROPERTIES = new Properties();
    private static final String PATH_MAPA_DESENV_HOME;
    private static final boolean CACHE_PROPERTIES;

    static {
        PATH_MAPA_DESENV_HOME = System.getenv("MAPA_DESENV_HOME");
        loadProperties();
        CACHE_PROPERTIES = isFuncaoHabilitada("cache.properties");
    }

    private PropriedadesAmbienteUtil() {
    }

    private static void loadProperties() {
        try {
            if (PATH_MAPA_DESENV_HOME != null && !CACHE_PROPERTIES) {
                MAPA_CONFIG_PROPERTIES.clear();
                MAPA_CONFIG_PROPERTIES.load(new FileReader(PATH_MAPA_DESENV_HOME + File.separator + "mapa-config.properties"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValor(String propKey) {
        loadProperties();
        return MAPA_CONFIG_PROPERTIES.getProperty(propKey);
    }

    public static boolean isFuncaoHabilitada(String propKey) {
        return Boolean.parseBoolean(getValor(propKey));
    }

}

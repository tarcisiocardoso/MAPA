package br.gov.mapa.seguranca.jaas;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.mapa.seguranca.util.Constantes;
import br.gov.mapa.seguranca.util.HashUtil;

/**
 * Classe responsável por obter a foto do usuário
 * 
 */
public class MediatorFotoAD {

	public static final String BARRA = File.separator;
    public static final String DIR_IMG = "img_usuarios";
    public static final String FOTO_DEFAULT = BARRA + DIR_IMG + BARRA + "default.png";
    public static final String JPG = "jpg";
    public static final String PONTO_JPG = "." + JPG;
    private static final String DIR_APACHE = "/u09/pub/imagens";
    private static final String DOCUMENTO_ANONIMO = "00000000000";

	private static final Logger LOGGER = LoggerFactory.getLogger(MediatorFotoAD.class);


    private MediatorFotoAD() {
        //constutor privado
    }

    /**
     * Retorna o caminho da foto do usuário no servidor Caso a foto não esteja no diretório de
     * imagens, é feita uma consulta no AD e convertida a foto em um arquivo JPG; É feito uma
     * atualização de foto por dia.
     * 
     * @return foto
     */
    public static String obterFotoUsuario(String documento, String username) {
        LOGGER.info("Iniciando verificação para obter foto...");
        String retorno = FOTO_DEFAULT;
        if (documento != null && documento.length() == 11 && !DOCUMENTO_ANONIMO.equals(documento)) {
            LOGGER.debug("Iniciando procedimento para obter foto...");
            try {
                String hashDocumento = HashUtil.getHashMD5(documento);
                String subDiretorio = getSubdiretorioFoto(hashDocumento);
                File foto = new File(subDiretorio + hashDocumento + PONTO_JPG);
                if (!foto.exists()) {
                    LOGGER.debug("Foto do usuário " + username + " não está em disco!");
                    obterFotoDoAD(foto, documento);
                } else {
//                    LocalDate dataArquivo = new LocalDate(foto.lastModified(), DataUtil.DATETIME_ZONE);
//                    if (DataUtil.isMenor(dataArquivo.toString(DataUtil.PADRAO_FORMATACAO_DATA),
//                        DataUtil.obterDataAtual().toString(DataUtil.PADRAO_FORMATACAO_DATA), DataUtil.PADRAO_FORMATACAO_DATA)) {
                	Date dt1 = new Date(foto.lastModified());
                	Date dt2 = new Date();
                	//TODO rever comparação da data
                	if( dt1.compareTo(dt2) < 0 ) {
                        LOGGER.debug("atualizando foto do usuário...");
                        obterFotoDoAD(foto, documento);
                    }
                }
                if (foto.exists()) {
                    retorno = getCaminhoFoto(foto);
                    LOGGER.debug("url da foto: " + retorno);
                }
            } catch (Exception e) {
                LOGGER.error(Constantes.MSG_ERRO_OBTER_FOTO_AD + username, e);
            }
        }
        return retorno;
    }

    public static String getCaminhoFoto(File foto) {
        return BARRA + DIR_IMG + BARRA + foto.getParentFile().getName() + BARRA + foto.getName();
    }

    public static String getSubdiretorioFoto(String hashDocumento) {
        String subDiretorio = DIR_APACHE + File.separator + DIR_IMG + File.separator + hashDocumento.substring(0, 2) + File.separator;
        return subDiretorio;
    }

    private static void obterFotoDoAD(File foto, String documento) throws Exception {
        LOGGER.debug("Obtendo foto do usuário de documento " + documento + " no AD...");
        
//        ADContextBO adContextBO = new ADContextBO();
//        NamingEnumeration<SearchResult> enumeration;
//        enumeration = adContextBO.consultarRegistroPorDocumento(documento);
//        if (enumeration.hasMore()) {
//            SearchResult result = enumeration.next();
//            Attributes attribs = result.getAttributes();
//            Attribute attributeFoto = attribs.get(PropriedadeADEnum.FOTO.getDescricao());
//            if (attributeFoto != null && attributeFoto.get() != null) {
//                byte[] fotoAD = (byte[]) attributeFoto.get();
//                if (fotoAD != null && fotoAD.length > 0) {
//                    if (!foto.getParentFile().exists()) {
//                        foto.getParentFile().mkdir();
//                        LOGGER.debug("Subdiretório criado: " + foto.getParentFile().getName());
//                    }
//                    InputStream in = new ByteArrayInputStream(fotoAD);
//                    BufferedImage bImageFromConvert = ImageIO.read(in);
//                    ImageIO.write(bImageFromConvert, JPG, foto);
//                    LOGGER.debug("foto do usuário armazenada em disco!");
//                }
//            } else {
//                LOGGER.warn("Usuário de cpf " + documento + " não possui foto no AD");
//            }
        }
}


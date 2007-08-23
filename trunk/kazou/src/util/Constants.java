package util;

public interface Constants {

	/** GUI Constants */
	/* Screen dimesions */
	public static final int SPLASH_SCREEN_WIDTH = 300;
	public static final int SPLASH_SCREEN_HEIGHT = 300;
	
	public static final int CHOOSEARCH_SCREEN_WIDTH = 300;
	public static final int CHOOSEARCH_SCREEN_HEIGHT = 200;
	
	public static final int SETUP_SCREEN_WIDTH = 300;
	public static final int SETUP_SCREEN_HEIGHT = 200;
	
	public static final int SEARCH_SCREEN_WIDTH = 300;
	public static final int SEARCH_SCREEN_HEIGHT = 300;

	public static final int TRANSFER_SCREEN_WIDTH = 500;
	public static final int TRANSFER_SCREEN_HEIGHT = 500;
	
	public static final int SPLASH_TIME=1500;
	
	/* Screen IDs */
	public static final int SPLASH_SCREEN=-1;
	public static final int CHOOSEARCH_SCREEN=0;
	public static final int SETUP_SCREEN=1;
	public static final int MANAGEMENT_SCREEN=2;
//	public static final int SEARCH_SCREEN=1;
//	public static final int TRANSFER_SCREEN=2;
	
	/* General screen labels */
	public static final String OK_LABEL = "OK";
	public static final String APPLY_LABEL = "Aplicar";
	public static final String FIELD_NOT_FILLED_LABEL = "Campo obrigatório não preenchido";
	public static final String WINDOW_ICON = "window_ico.png";
	public static final String WINDOW_SPLASH = "SplashImage.jpg";
	public static final String WARNING_LABEL = "Aviso";
	public static final String ERROR_LABEL = "Erro";
	
	/* Screen labels */
	public static final String SPLASH_SCREEN_TITLE = "U Share";
	public static final String CHOOSEARCH_SCREEN_TITLE = "Escolha sua arquitetura";
	public static final String SETUP_SCREEN_TITLE = "Configurações";
	public static final String SETUP_SCREEN_REP_LABEL= "Repositório";
	public static final String SETUP_SCREEN_NAME_LABEL= "Nome Local";
	public static final String SETUP_SCREEN_NAME_SERVER_LABEL= "Servidor";
	public static final String SETUP_SCREEN_SUPERNODE_LABEL= "Supernós";
	public static final String SETUP_SCREEN_BROWSE_LABEL = "Escolher";
	public static final String SETUP_SCREEN_CANCEL_LABEL = "Cancel";
	
	public static final String MANAGEMENT_SCREEN_TITLE = "U Share";
	
	public static final String SEARCH_SCREEN_TITLE = "Busca de arquivos";
	public static final String SEARCH_SCREEN_SEARCH_LABEL= "Consulta";
	
	public static final String TRANSFER_SCREEN_TITLE = "Transferências";
	
	public static final String[] SEARCH_TABLE_HEADER = {"Nome do Arquivo",
            "Tamanho", "ID"};
	public static final String[] DOWNLOAD_TABLE_HEADER = {"Nome do Arquivo",
        "Taxa","Tamanho","Completo"};
	
	/** Application Constants */
	public static final int NODE_ARCHITECTURE =0;
	public static final int SUPERNODE_ARCHITECTURE =1;

	
}

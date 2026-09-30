package lommie.onlycraftonce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

    public static final String MOD_ID = "only_craft_once";
    public static final String MOD_NAME = "OnlyCraftOnce";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static final String YACL_MODID = "yet_another_config_lib_v3";

    // all of this should go into a lang file
    public static final String CONFIG_OPTION_NAME = "Max craftable";
    public static final String CONFIG_OPTION_DESCRIPTION = "The maximum amount of times an item is allowed to be crafted";
    public static final String CONFIG_ERROR_OUT_OF_GAME = "You are currently not in a server (singleplayer is also a server). \nTo fix, either join a server with op or view_config and change_config permissions or join a singleplayer world to edit config for all singleplayer worlds";
    public static final String CONFIG_ERROR = MOD_NAME + " had an error";
    public static final String NO_PERMISSIONS_VIEW_CONFIG = "You don't have permission to view the "+MOD_NAME+" configuration!";
    public static final String NO_PERMISSIONS_CHANGE_CONFIG = "You do not have permission to change the "+MOD_NAME+" configuration!";
    public static final String CONFIG_ERROR_TIMEOUT = "Receiving the server's config took too long. Check chat for an \"You don't have permission...\" error. If you don't see any error, then close this screen and wait a bit before reopening";

    public static final int GET_CONFIG_TRYS = 3;
    public static final int GET_CONFIG_WAIT = 300;
}

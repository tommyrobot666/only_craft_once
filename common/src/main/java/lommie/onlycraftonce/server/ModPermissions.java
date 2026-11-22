package lommie.onlycraftonce.server;

import lommie.onlycraftonce.Constants;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permission;

public class ModPermissions {
    public static final Permission VIEW_CONFIG = Permission.Atom.create(Identifier.fromNamespaceAndPath(Constants.MOD_ID,"view_config"));
    public static final Permission CHANGE_CONFIG = Permission.Atom.create(Identifier.fromNamespaceAndPath(Constants.MOD_ID,"change_config"));
}

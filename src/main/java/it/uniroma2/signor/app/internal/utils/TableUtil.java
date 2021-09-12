package it.uniroma2.signor.app.internal.utils;

import org.cytoscape.model.*;

import java.util.*;


public class TableUtil {


    public static class NullAndNonNullEdges {
        public final Set<CyEdge> nonNullEdges = new HashSet<>();
        public final Set<CyEdge> nullEdges = new HashSet<>();
    }

    public static void createColumnIfNeeded(CyTable table, Class<?> clazz, String columnName) {
        if (table.getColumn(columnName) != null)
            return;
        table.createColumn(columnName, clazz, false);
    }
    public static void createColumnIfNeeded(CyTable table, Class<?> clazz, String columnName, String namespace) {
        if (table.getColumn(namespace, columnName) != null)
            return;
        table.createColumn(namespace, columnName, clazz, false);
    }
    public static <T> void createColumnIfNeeded(CyTable table, Class<T> clazz, String columnName, T defaultValue) {
        if (table.getColumn(columnName) != null)
            return;
        table.createColumn(columnName, clazz, false, defaultValue);
    }
    public static boolean ifColumnIfExist(CyTable table, String namespace, String columnName) {
        if (table.getColumn(namespace, columnName) != null)
            return true;
        else return false;
    }
    
//    public static void replaceColumnIfNeeded(CyTable table, Class<?> clazz, String columnName) {
//        if (table.getColumn(columnName) != null)
//            table.deleteColumn(columnName);
//
//        table.createColumn(columnName, clazz, false);
//    }
//
//    public static void createListColumnIfNeeded(CyTable table, Class<?> clazz, String columnName) {
//        if (table.getColumn(columnName) != null)
//            return;
//
//        table.createListColumn(columnName, clazz, false);
//    }
//
//    public static void replaceListColumnIfNeeded(CyTable table, Class<?> clazz, String columnName) {
//        if (table.getColumn(columnName) != null)
//            table.deleteColumn(columnName);
//
//        table.createListColumn(columnName, clazz, false);
//    }
//
//    public static void deleteColumnIfExisting(CyTable table, String columnName) {
//        if (table.getColumn(columnName) != null)
//            table.deleteColumn(columnName);
//    }

//    public static String getName(CyNetwork network, CyIdentifiable ident) {
//        return getString(network, ident, CyNetwork.NAME);
//    }
//
//    public static String getString(CyNetwork network, CyIdentifiable ident, String column) {
//        if (network.getRow(ident, CyNetwork.DEFAULT_ATTRS) != null)
//            return network.getRow(ident, CyNetwork.DEFAULT_ATTRS).get(column, String.class);
//        return null;
//    }

}

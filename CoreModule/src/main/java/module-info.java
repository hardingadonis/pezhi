module pezhi.core {
    requires static lombok;

    requires com.google.gson;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;
    requires org.xerial.sqlitejdbc;

    exports dao;
    exports model;
    exports utils;

    opens utils to com.google.gson;
}
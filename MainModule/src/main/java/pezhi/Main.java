package pezhi;

import application.PeZhiApplication;
import utils.ConfigManager;

public class Main {
    public static void main(String[] args) {
        ConfigManager.getInstance().load();

        PeZhiApplication.main(args);

        ConfigManager.getInstance().save();
    }
}

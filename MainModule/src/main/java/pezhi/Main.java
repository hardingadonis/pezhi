package pezhi;

import utils.ConfigManager;

public class Main {
    public static void main(String[] args) {
        ConfigManager.getInstance().load();

        ConfigManager.getInstance().save();
    }
}

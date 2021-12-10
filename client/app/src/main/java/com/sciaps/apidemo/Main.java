package com.sciaps.apidemo;

import javax.swing.*;

import com.sciaps.InstrumentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Main {

    public static Logger LOGGER = LoggerFactory.getLogger("ApiDemo");

    public static void main(String[] args) {

        boolean isUsingCommandLine = args.length >= 2;
        String ipAddress = isUsingCommandLine ? args[0] : JOptionPane.showInputDialog("Enter Analyzer IP Address", "192.168.42.129:8080");
        if (ipAddress == null || ipAddress.trim().isEmpty()) {
            LOGGER.error("Must specify Analyzer IP Address");
            return;
        }

        SciApsClient client = new SciApsClient(ipAddress);
        InstrumentConfig config = client.getInstrumentConfig();

        LOGGER.info("Found instrument {} at {}", config.id, ipAddress);
        LOGGER.info("Running version {}", config.swVersion);
        LOGGER.info("Available apps: {}", Arrays.toString(config.apps));
/*
        String modeAndModelNameStr = isUsingCommandLine ? args[1] : JOptionPane.showInputDialog("Enter Mode and Model Name", "Mining");
        if (modeAndModelNameStr == null) {
            return;
        }
 */
    }
}
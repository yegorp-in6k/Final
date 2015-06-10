package com.company;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Scanner;
/**
 *
 * @author Odmin
 */
public class Main {
    private static final String INPUT_FILE_PATH = "G:\\Xelopes6.5_openSource\\data\\custom-transact.arff";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        switch (chooseMethod()) {
            case 1: runSequantial();
            case 2: runAssociation();1
            case 3: runCustomerSequential();
        }

    }

    private static int chooseMethod() {
        System.out.println("Choose Mining Model to run:");
        System.out.println("    1. SequentialMining;");
        System.out.println("    2. AssociationRulesMining;");
        System.out.println("    3. CustomerSequentialMining;");
        Scanner in = new Scanner(System.in);
        return in.nextInt();
    }

    private static void runSequantial() throws Exception {
        runBuild("com.prudsys.pdm.Examples.DataMining.SequentialBuild");
        SequentialMining sequentialMining = new SequentialMining();
    }

    private static void runAssociation() throws Exception {
        runBuild("com.prudsys.pdm.Examples.DataMining.AssociationRulesBuild");
        AssociationRulesMining associationRulesMining = new AssociationRulesMining();
    }

    private static void runCustomerSequential() throws Exception {
        runBuild("com.prudsys.pdm.Examples.DataMining.CustomerSequentialBuild");
        CustomerSequentialMining customerSequentialMining = new CustomerSequentialMining();
    }



    private static void runBuild(String mothodClassName) {
        try {
            System.out.println("Loading and running: " + mothodClassName);
            Class exClass       = Class.forName(mothodClassName);
            Constructor exConst = exClass.getConstructor();
            Object exObject     = exConst.newInstance();
            Method classMethod  = exClass.getMethod("runExample");
            classMethod.invoke(exObject);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

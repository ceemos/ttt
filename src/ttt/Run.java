/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ttt;

import java.io.IOException;

/**
 *
 * @author marcel
 */
public class Run {
    
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Running ant...");
        String command = "ant -f andbuild.xml debug install";
        Process p = Runtime.getRuntime().exec(command.split(" "));
        p.waitFor();
        System.out.println("Starting...");
        command = "/opt/android-sdk/platform-tools/adb shell "
                + "am start -a android.intent.action.MAIN -n ceemos.ttt/ceemos.ttt.TttActivity";
        Runtime.getRuntime().exec(command.split(" "));
    }
    
}

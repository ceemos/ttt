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
        fs.run(command.split(" "));
        System.out.println("Starting...");
        command = "/opt/android-sdk/platform-tools/adb shell "
                + "am start -e debug true -a android.intent.action.MAIN -n ceemos.ttt/ceemos.ttt.TttActivity";
        fs.run(command.split(" "));
        Process p = Runtime.getRuntime().exec(new String[]{"/opt/android-sdk/platform-tools/adb", "jdwp"});
        byte[] buffer = new byte[1024];
        p.getInputStream().read(buffer, 0, 1024);
        String[] pids = new String(buffer).split("\n");
        String pid = pids[pids.length - 2];
        fs.run("/opt/android-sdk/platform-tools/adb", "forward", "tcp:29882", "jdwp:" + pid);
    }
    
}

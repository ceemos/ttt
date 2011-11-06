
package ttt;

import java.io.IOException;

/**
 *
 * @author marcel
 */
public class Logcat {
    
    public static void main(String[] args) throws IOException{
        String[] emucmd = {"emulator", "-avd", "andro22"};
        Process emu = Runtime.getRuntime().exec(emucmd);
        fs.run("/opt/android-sdk/platform-tools/adb", "logcat");
    }
    
}

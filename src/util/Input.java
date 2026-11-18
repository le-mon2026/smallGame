package util;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * 全工程统一的输入入口。
 * 1. 整个程序只创建一个 Scanner，避免多个 Scanner 抢 System.in 造成输入丢失；
 * 2. 输入流结束时（IDEA 里按 Ctrl+Z 回车、管道读完、文件结尾）统一提示并退出，
 *    而不是抛出 NoSuchElementException 崩掉，或者像以前那样死循环。
 */
public final class Input {

    private static final Scanner SCANNER = new Scanner(System.in);

    private Input() {
    }

    /**
     * 读一行并去掉首尾空白；输入流结束则结束程序。
     */
    public static String readLine() {
        try {
            if (!SCANNER.hasNextLine()) {
                exitOnEof();
            }
            return SCANNER.nextLine().trim();
        } catch (NoSuchElementException | IllegalStateException e) {
            // 读的瞬间流被关掉了（比如以后有人调了 close()）
            exitOnEof();
            return ""; // 到不了这里，只为让编译器通过
        }
    }

    /**
     * 带提示地读一行。
     */
    public static String readLine(String prompt) {
        System.out.print(prompt);
        return readLine();
    }

    /**
     * 读一个整数；不是数字就提示并重读，输入流结束则结束程序。
     */
    public static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(readLine());
            } catch (NumberFormatException e) { // 只接住"不是数字"这一种
                System.out.print("请输入数字：");
            }
        }
    }

    /**
     * 带提示地读一个整数。
     */
    public static int readInt(String prompt) {
        System.out.print(prompt);
        return readInt();
    }

    //输入流结束的统一出口
    private static void exitOnEof() {
        System.out.println("\n输入已结束，程序退出。");
        System.exit(0);
    }
}
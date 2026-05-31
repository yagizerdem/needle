package Needle;

public class Main {
    public static void main(String[] args) {

        try {
            Core needle = new Core();
            boolean flag = needle.contains("(a|b)",  "yabiz");
            System.out.println(flag);

        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
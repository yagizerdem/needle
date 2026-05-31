package Needle;

public class Main {
    public static void main(String[] args) {

        try {
            Core needle = new Core();
            boolean flag = needle.isExactMatch("(a|b)*c+123?",  "abbc12");
            System.out.println(flag);

        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
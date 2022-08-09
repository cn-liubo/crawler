import org.junit.Test;

public class test {
    @Test
    public void test1() {
        String s = "class in java.util";
        int i = s.lastIndexOf(" ");
        System.out.println(s.substring(i));
    }
}

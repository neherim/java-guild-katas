import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Puzzlers {

    /**
     * 1. true false
     * 2. false true
     * 3. true true
     * 4. Neither of these
     * <p>
     * ANSWER: 1
     */
    @Test
    void arrays() {
        var stringArray = new String[]{"one", "two", "three"};
        var stringList = Arrays.asList(stringArray);

        var intArray = new int[]{1, 2, 3};
        var intList = Arrays.asList(intArray);

        System.out.println(stringList.contains("one") + " " + intList.contains(1));
    }

    /**
     * 1. [1] [1, 2, 3]
     * 2. [1, 4, 5, 6] [1, 2, 3]
     * 3. Unsupported Operation Exception
     * 4. None of these
     * <p>
     * ANSWER: 4 ( [1, 4, 5, 6] [1, 2, 3] )
     */
    @Test
    void sublist() {
        var ints = new ArrayList<>(List.of(1, 2, 3));
        var sublist = ints.subList(0, 1);
        sublist.addAll(List.of(4, 5, 6));

        System.out.println(sublist + " " + ints);
    }

    /**
     * 1. 4 3
     * 2. 4 4
     * 3. 3 4
     * 4. None of these
     * <p>
     * ANSWER: 4 NullPointerException
     */
    @Test
    void nulls() {
        String[] strings = {"a", "b", "c", null};
        var list1 = Stream.of(strings).toList();
        System.out.print(list1.size() + " ");

        var list2 = List.of(strings);
        System.out.println(list2.size());
    }

    /**
     * 1. XX
     * 2. 0X
     * 3. X0
     * 4. X88
     * <p>
     * ANSWER: 4
     * <p>
     * Binary numeric promotion is applied to the operands types, and the type of the conditional
     * expression is the promoted type of the second and third operands.
     */
    @Test
    void tenary() {
        char x = 'X';
        int i = 0;
        System.out.print(true ? x : 0);
        System.out.print(false ? i : x);
    }

    /**
     * Look at \ units in the comments. These characters begin with a backslash (\) followed by the letter u,
     * which denotes the start of a Unicode escape. These characters are not followed by four hexadecimal digits,
     * so the Unicode escape is ill-formed.
     */

    //1. Hello World!
    //2. Hello
    //3. Don't compile

    /**
     * File c:\\projects\puzzlers\src\test\java\ utils\PuzzlersTest.java
     */
    @Test
    void output() {
        System.out.print("Hello ");
        System.out.println("World!");
    }


    /**
     * 1. server.http.port
     * 2. IllegalArgumentException
     * 3. server_http_port
     * 4. Something else
     * <p>
     * ANSWER: ________________
     */
    @Test
    void replaceAll() {
        var param = "server.http.port".replaceAll(".", "_");
        System.out.println(param);
    }
}

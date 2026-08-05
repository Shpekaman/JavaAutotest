import org.example.Methods;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Assertions {
    private static final Random random = new Random();


    @BeforeEach
    void setUp(){
        System.out.println("=================");
        System.out.println("Test method start");
    }

    @AfterEach
    void tearDown(){
        System.out.println("Test method end");
        System.out.println("=================");
    }

    //Assertion проверки

    //boolean isEven(int n)
    @Tag("smoke")
    @RepeatedTest(10)
    void testIsEven() {
        int number = random.nextInt(0, 101);
        boolean expected = number % 2 == 0;
        boolean actual = Methods.isEven(number);
        System.out.print("isEven(" + number + ") -> " + actual + " \n");
        assertThat(actual)
                .as("isEven("+number+") should be "+expected)
                .isEqualTo(expected);
    }

    //String checkAccess(int age)
    @Tag("smoke")
    @RepeatedTest(10)
    void testCheckAccess() {
        for (int i = 0; i < 20; i++) {
            int age = random.nextInt(100);
            String expected;
            if (age < 0) expected = "Сначала надо родится";
            else if (age > 18) expected = "Allowed";
            else expected = "Denied";
            String actual = Methods.checkAccess(age);
            System.out.print("checkAccess(" + age + ") -> " + actual + " \n");
            assertThat(actual)
                    .as("checkAccess(%d) should be %s", age, expected)
                    .isEqualTo(expected);
        }
    }

    //String getGrade(int score) — запустить метод в параметризованных тестах с массивом случайных чисел от 0 до 100.
    @Tag("smoke")
    @ParameterizedTest
    @MethodSource("random")
    void testGetGrade(int score) {
        String actual = Methods.getGrade(score);
        String expected = computeGrade(score);
        System.out.print("getGrade(" + score + ") -> " + actual + " \n");
        assertThat(actual)
                .as("getGrade("+score+") should be "+expected)
                .isEqualTo(expected);

    }
    //Это для GetGrade
    static IntStream random(){
        return IntStream.generate(()->random.nextInt(101))
                .limit(10);
    }
    private String computeGrade(int score) {
        if (score < 0 || score > 100) return "from 0 to 100";
        if (score >= 81) return "A";
        if (score >= 61) return "B";
        if (score >= 41) return "C";
        if (score >= 21) return "D";
        return "E";
    }
    //
    @Tag("smoke")
    @ParameterizedTest
    @CsvSource({"-4","-3","-2","-1","0","1","2","3","4","5"})
    void testIsPositive(){
        int number = random.nextInt(-100,101);
        boolean actual = Methods.isPositive(number);
        boolean expected = number >= 0;
        System.out.print("isPositive(" + number + ") -> " + actual + " \n");
        assertThat(actual)
                .as("isPositive("+number+") should be "+expected)
                .isEqualTo(expected);
    }

    @RepeatedTest(10)
    void testBlastOff(){
        int number = random.nextInt(-5,5);
        String expected;
        if(number<0){
            expected="Только положительные";
        }
        else if(number==0){
            expected="Поехали!";
        }else {
            StringBuilder result = new StringBuilder();
            for (int i = number; i >= 1; i--) {
                result.append(i);
                if (i > 1) {
                    result.append(" ");
                }
            }
            result.append(" Поехали!");
            expected=result.toString();
        }
        String actual = Methods.blastOff(number);
        System.out.print("blastOff(" + number + ") -> " + actual + " \n");
        assertThat(actual)
                .as("blastOff("+number+") should be "+expected)
                .isEqualTo(expected);
    }

    @RepeatedTest(10)
    void testSumToN(){
        int number = random.nextInt(-3,5);
        System.out.println(number);
        int expected;
        if(number<0){
            expected=0;
        }
        else {
            int result = 0;
            for (int i = 1; i <= number; i++) {
                result += i;
            }
            expected = result;
            System.out.println(expected);
        }
        int actual = Methods.sumToN(number);
        System.out.print("sumToN(" + number + ") -> " + actual + " \n");
        assertThat(actual)
                .as("sumToN("+number+") should be "+expected)
                .isEqualTo(expected);
    }

    @RepeatedTest(10)
    void testHasBug(){
        String[]messages= generateRandomMessages(10);
        boolean actual = Methods.hasBug(messages);
        boolean expected = containsBug(messages);
        String description = "hasBug(" + Arrays.toString(messages) + ") should be " + expected;
        System.out.println(description);   // всегда печатается
        assertThat(actual)
                .as("hasBug("+messages+") should be "+expected)
                .isEqualTo(expected);
    }

    //Генерация для testHasBug и testReverse
    public static String[] generateRandomMessages(int size){
        String[]possibleMessages = {"Bug","Fix","Feature","Hello","Test","Bububu","Tututu"};

        String[] messages = new String[size];

        for (int i =0;i<size;i++){
            messages[i] = possibleMessages[random.nextInt(possibleMessages.length)];
        }
        return messages;
    }
    //проверка на слово Bug
    private boolean containsBug(String[] messages){
        for(String msg:messages){
            if("Bug".equals(msg)) return true;
        }
        return false;
    }

    @RepeatedTest(10)
    void testGetEvenInRange(){
        int number1 = random.nextInt(0,101);
        int number2 = random.nextInt(0,101);
        String actual = Methods.getEvenInRange(number1,number2);
        String expected = buildEvenRangeExpected(number1, number2);
        System.out.print("getEvenInRange(" + number1 + ", " + number2 + ") -> " + actual + " \n");
        assertThat(actual)
                .as("getEvenInRange(" + number1 + ", " + number2 + ") should be "+expected)
                .isEqualTo(expected);
    }
    //для testGetEvenInRange
    private String buildEvenRangeExpected(int start, int end) {
        if (start > end) return "Что то не то";
        StringBuilder sb = new StringBuilder();
        for (int i = start; i <= end; i++) {
            if (i % 2 == 0) {
                if (sb.length() > 0) sb.append(' ');
                sb.append(i);
            }
        }
        return sb.toString();
    }

    @RepeatedTest(10)
    void testFindMax(){
        int[] arr = generateRandomArray(10);
        int expected = calculateMax(arr);
        int actual = Methods.findMax(arr);
        System.out.println("findMax(" + Arrays.toString(arr) + ") returned " + actual + ", expected " + expected);
        assertThat(actual).isEqualTo(expected);
    }

    //генерация массива testFindMax
    private int[] generateRandomArray(int size){

        int[] arr = new int[size];
        for(int i=0;i<size;i++){
            arr[i]=random.nextInt(100);
        }
        return arr;
    }
    //поиск Max в Find Max
    private int calculateMax(int []arr){
        int max = arr[0];
        for(int i=1;i< arr.length;i++){
            if(arr[i]>max) max = arr[i];
        }
        return max;
    }


    @RepeatedTest(10)
    void testReverse(){
        String[]input=generateRandomMessages(5);
        String[]expected = reverse(input);
        String[] actual = Methods.reverse(input);
        boolean passed = Arrays.equals(expected, actual);
        System.out.print("reverse(" + Arrays.toString(input) + ") -> " + Arrays.toString(actual) + " \n");
        assertThat(actual)
                .as("reverse(" + Arrays.toString(input) + ") should be "+expected)
                .isEqualTo(expected);
    }


    //Генерация для expected в testReverse
    private String[] reverse(String[] arr){
        String[]result = new String[arr.length];
        for (int i=0;i<arr.length;i++){
            result[i]= arr[arr.length-1-i];
        }
        return result;
    }

    @RepeatedTest(10)
    void testCalcAverage(){
        List<Integer> list = generateRandomList(10);
        int expected = calcAverageManually(list);
        int actual = Methods.calcAverage(list);
        System.out.println("calcAverage(" + list + ") = " + actual + ", expected " + expected);
        assertThat(actual).isEqualTo(expected);
    }

    //генерация для calcAverage
    private List<Integer> generateRandomList(int size){
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i<size;i++){
            list.add(random.nextInt(100));
        }
        return list;
    }
    //считаем среднее
    private int calcAverageManually(List<Integer> list){
        int sum = 0;
        for (int num:list){
            sum+=num;
        }
        return sum/list.size();
    }

    @ParameterizedTest
    @MethodSource("removeSpecificNameData")
    void testRemoveSpecificName(List<String> list, String toRemove, List<String> expected){
        List<String> actual = Methods.removeSpecificName(list, toRemove);
        boolean passed = expected.equals(actual);
        System.out.print("removeSpecificName(" + list + ", \"" + toRemove + "\") -> " + actual + " \n");
        assertThat(actual)
                .isEqualTo(expected);

    }
    static Stream<Arguments> removeSpecificNameData() {
        return Stream.of(
                Arguments.of(asList("Alice", "Bob", "Charlie"), "Bob", asList("Alice", "Charlie")),
                Arguments.of(asList("Alice", "Alice", "Bob"), "Alice", asList("Bob")),
                Arguments.of(singletonList("Anna"), "Anna", List.of()),
                Arguments.of(asList("Marry", "Bob", "Charlie"), "Marry", asList("Bob", "Charlie")),
                Arguments.of(asList("Alice", "Michael", "Bob"), "Alice", asList("Michael", "Bob")),
                Arguments.of(asList("John", "Jane", "Jack"), "Jane", asList("John", "Jack")),
                Arguments.of(asList("Mike", "Mike", "Sarah"), "Mike", asList("Sarah")),
                Arguments.of(asList("Elena", "Ivan", "Elena", "Petr"), "Elena", asList("Ivan", "Petr")),
                Arguments.of(asList("Olga", "Olga", "Olga"), "Olga", List.of()),
                Arguments.of(asList("Alex", "Maria", "Alex", "Maria"), "Alex", asList("Maria", "Maria"))
                        );
    }


}

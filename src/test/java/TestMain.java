import org.example.Methods;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.extension.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@ExtendWith(TestLoggerExtension.class)

//Тестовый класс который запускает все методы и тесты
public class TestMain {
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
    //Тесты из первой части Задачи №2
    //boolean isEven(int n) — запустить метод один раз со случайным числом от 1 до 100;
    @Test
    void testIsEven(){
        int number = random.nextInt(0,101);
        boolean expected = number % 2 == 0;
        boolean actual = Methods.isEven(number);
        System.out.print("isEven(" + number + ") -> " + actual + " ");
        if (expected == actual) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED (expected: " + expected + ")");
        }
    }
    //String checkAccess(int age) — запустить метод 20 раз со случайными числами от 0 до 99;
    @RepeatedTest(20)
    void testCheckAccess(){
        for(int i=0;i<20;i++){
            int age = random.nextInt(100);
            String expected;
            if (age < 0) expected = "Сначала надо родится";
            else if (age > 18) expected = "Allowed";
            else expected = "Denied";
            String actual = Methods.checkAccess(age);
            System.out.print("checkAccess(" + age + ") -> " + actual + " ");
            if (expected.equals(actual)) {
                System.out.println("TEST PASSED");
            } else {
                System.out.println("TEST FAILED (expected: " + expected + ")");
            }
        }
    }
    //String getGrade(int score) — запустить метод в параметризованных тестах с массивом случайных чисел от 0 до 100.
    @ParameterizedTest
    @MethodSource("random")
    void testGetGrade(int score) {
        String actual = Methods.getGrade(score);
        String expected = computeGrade(score);
        System.out.print("getGrade(" + score + ") -> " + actual + " ");
        if (expected.equals(actual)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED (expected: " + expected + ")");
        }

    }
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

    //Остальные тесты из второй задачи
    @ParameterizedTest
    @CsvSource({"-4","-3","-2","-1","0","1","2","3","4","5"})
    void testIsPositive(){
        int number = random.nextInt(-100,101);
        boolean actual = Methods.isPositive(number);
        boolean expected = number % 2 == 0;
        System.out.print("isPositive(" + number + ") -> " + actual + " ");
        if (expected == actual) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED (expected: " + expected + ")");
        }
    }

    @RepeatedTest(5)
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
        System.out.print("blastOff(" + number + ") -> " + actual + " ");
        if (expected.equals(actual)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED (expected: " + expected + ")");
        }

    }

    @RepeatedTest(5)
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
        System.out.print("sumToN(" + number + ") -> " + actual + " ");
        if (expected == actual) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED (expected: " + expected + ")");
        }
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

    @RepeatedTest(5)
    void testHasBug(){
        String[]messages= generateRandomMessages(10);
        boolean actual = Methods.hasBug(messages);
        boolean expected = containsBug(messages);

        if(expected == actual){
            System.out.println("TEST PASSED: "+ Arrays.toString(messages)+"-> hasBug = "+actual);
        }else{
            System.out.println("TEST FAILED: "+ Arrays.toString(messages) +"-> expected"+ expected);

        }
    }

    @Test
    void testGetEvenInRange(){
        int number1 = random.nextInt(0,101);
        int number2 = random.nextInt(0,101);
        String actual = Methods.getEvenInRange(number1,number2);
        String expected = buildEvenRangeExpected(number1, number2);
        System.out.print("getEvenInRange(" + number1 + ", " + number2 + ") -> " + actual + " ");
        if (expected.equals(actual)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED (expected: " + expected + ")");
        }
    }

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
    @Test
    void testFindMax(){
        int[] arr = generateRandomArray(10);
        int expected = calculateMax(arr);
        int actual = Methods.findMax(arr);
        if(expected == actual){
            System.out.println("TEST PASSED: findMax("+ Arrays.toString(arr)+")="+actual);
        }else{
            System.out.println("TEST FAILED: expected"+ expected +", got"+ actual);

        }
    }
    //Генерация для expected в testReverse
    private String[] reverse(String[] arr){
            String[]result = new String[arr.length];
        for (int i=0;i<arr.length;i++){
            result[i]= arr[arr.length-1-i];
        }
        return result;
    }
    @Test
    void testReverse(){
        String[]input=generateRandomMessages(5);
        String[]expected = reverse(input);
        String[] actual = Methods.reverse(input);
        boolean passed = Arrays.equals(expected, actual);
        System.out.print("reverse(" + Arrays.toString(input) + ") -> " + Arrays.toString(actual) + " ");
        if (passed) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED (expected: " + Arrays.toString(expected) + ")");
        }


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
    @Test
    void testCalcAverage(){
        List<Integer> list = generateRandomList(10);
        int expected = calcAverageManually(list);
        int actual = Methods.calcAverage(list);
        if(expected == actual){
            System.out.println("TEST PASSED: "+ list +"-> average"+actual);
        }else{
            System.out.println("TEST FAILED: "+ list +"-> expected"+expected + ", got"+actual);

        }
    }

    @ParameterizedTest
    @MethodSource("removeSpecificNameData")
    void testRemoveSpecificName(List<String> list, String toRemove, List<String> expected){
        List<String> actual = Methods.removeSpecificName(list, toRemove);
        boolean passed = expected.equals(actual);
        System.out.print("removeSpecificName(" + list + ", \"" + toRemove + "\") -> " + actual + " ");
        if (passed) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED (expected: " + expected + ")");
        }
    }
    static Stream<Arguments> removeSpecificNameData() {
        return Stream.of(
                Arguments.of(asList("Alice", "Bob", "Charlie"), "Bob", asList("Alice", "Charlie")),
                Arguments.of(asList("Alice", "Alice", "Bob"), "Alice", singletonList("Bob")),
                Arguments.of(singletonList("Anna"), "Anna", List.of())
        );
    }

}

import org.example.Main;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.extension.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(TestLoggerExtension.class)

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

    @Test
    void testIsEven(){
        int number = random.nextInt(0,101);
        boolean result = Main.isEven(number);
        System.out.println("isEven("+ number + ")=" + result);
    }

    @Test
    void testCheckAccess(){
        for(int i=0;i<20;i++){
            int age = random.nextInt(100);
            String result = Main.checkAccess(age);
            System.out.println("checkAccess("+age+")="+ result);

            if (age<18) {
                assertEquals("Denied", result);
            } else {
                assertEquals("Allowed", result);
            }
        }
    }

    @Test
    void testIsPositive(){
        int number = random.nextInt(-100,101);
        boolean result = Main.isPositive(number);
        System.out.println("isPositive("+ number + ")=" + result);
    }

    @ParameterizedTest
    @MethodSource("random")
    void testGetGrade(int score) {
        String result = Main.getGrade(score);
        System.out.println("getGrade(" + score + ")="+ result);

        assertTrue(score>=0 && score<=100);
        if(score>=81){
            assertEquals("A",result);
        }
        else if(score>=61) {
            assertEquals("B",result);
        }
        else if(score>=41) {
            assertEquals("C",result);
        }
        else if(score>=21) {
            assertEquals("D",result);
        }
        else {
            assertEquals("E",result);
        }
    }
    static IntStream random(){
        return IntStream.generate(()->random.nextInt(101))
        .limit(10);
    }

    @RepeatedTest(5)
    void testBlastOff(){
        int number = random.nextInt(-5,5);
        String expected;
        if(number<0){
            expected="Мы принимаем только положительные числа";
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
        String actual = Main.blastOff(number);
        assertEquals(expected,actual);
        System.out.println(actual);
        System.out.println(expected);

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
        Integer actual = Main.sumToN(number);
        assertEquals(expected,actual);
    }

    //Для рандома в testHasBug и для testReverse
    public static String[] generateRandomMessages(int size){
        String[]possibleMessages = {"Bug","Fix","Feature","Hello","Test","Bububu","Tututu"};

        String[] messages = new String[size];

        for (int i =0;i<size;i++){
            messages[i] = possibleMessages[random.nextInt(possibleMessages.length)];
        }
        return messages;
    }
    //Для проверки на слово Bug в рандоме
    private boolean containsBug(String[] messages){
        for(String msg:messages){
            if("Bug".equals(msg)) return true;
        }
        return false;
    }

    @RepeatedTest(5)
    void testHasBug(){
        String[]messages= generateRandomMessages(10);
        boolean actual = Main.hasBug(messages);
        boolean expected = containsBug(messages);

        if(expected == actual){
            System.out.println("TEST PASSED: "+ Arrays.toString(messages)+"-> hasBug = "+actual);
        }else{
            System.out.println("TEST FAILED: "+ Arrays.toString(messages) +"-> expected"+ expected);
            fail("HasBug returned wrong value");
        }
    }

    @Test
    void testGetEvenInRange(){
        int number1 = random.nextInt(0,101);
        int number2 = random.nextInt(0,101);
        String result = Main.getEvenInRange(number1,number2);
        System.out.println("getEvenInRange " + "first number: "+number1+" second number: "+number2);
        System.out.println(result);
    }

    //Для рандома testFindMax
    private int[] generateRandomArray(int size){

        int[] arr = new int[size];
        for(int i=0;i<size;i++){
            arr[i]=random.nextInt(100);
        }
        return arr;
    }
    //Для вычисления Max в Find Max
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
        int actual = Main.findMax(arr);
        if(expected == actual){
            System.out.println("TEST PASSED: findMax("+ Arrays.toString(arr)+")="+actual);
        }else{
            System.out.println("TEST FAILED: expected"+ expected +", got"+ actual);
            fail("Max mismatch");
        }
    }
    //Для того чтобы вычислить expected в testReverse
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

        try {
            String[]actual= Main.reverse(input);
            assertArrayEquals(expected,actual);
            System.out.println("TEST PASSED "+Arrays.toString(input)+"-> reversed correctly");
        }catch (Exception e){
            System.out.println("TEST FAILED "+e.getClass().getSimpleName()+"-"+e.getMessage());
            fail("Не получилось");
        }
    }
    //генерация для теста calcAverage
    private List<Integer> generateRandomList(int size){
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i<size;i++){
            list.add(random.nextInt(100));
        }
        return list;
    }
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
        int actual = Main.calcAverage(list);
        if(expected == actual){
            System.out.println("TEST PASSED: "+ list +"-> average"+actual);
        }else{
            System.out.println("TEST FAILED: "+ list +"-> expected"+expected + ", got"+actual);
            fail("WRONG!");
        }
    }

    @Test
    void testRemoveSpecificName(){
        List<String>input = Arrays.asList("Alice","Bob","Michael");
        List<String>result= Main.removeSpecificName(input,"Alice");
        assertEquals(Arrays.asList("Bob","Michael"), result);
    }


}

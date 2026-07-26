package org.example;

import java.util.ArrayList;
import java.util.List;

public class TestMethods {
        public static boolean isEven(int n){
            return n % 2 ==0;
        }

    public static String checkAccess(int age){
        if(age<0){
            return ("Сначала надо родится");
        }
        if(age>=18){
            return "Allowed";
        }else{
            return "Denied";
        }
    }

    public static boolean isPositive(int n){
        return n >= 0;
    }

    public static String getGrade(int score){
        if(score<0||score>100){
            return "from 0 to 100";
        }
        if(score>=81) return "A";
        if(score>=61) return "B";
        if(score>=41) return "C";
        if(score>=21) return "D";
        return "E";
    }

    //методы
    public static String blastOff(int start){
        if(start<0){
            return ("Только положительные");
        }
        if(start==0){
            return "Поехали!";
        }
        StringBuilder result = new StringBuilder();
        for(int i=start; i>=1; i--){
            result.append(i);
            if(i>1){
                result.append(" ");
            }
        }
        result.append(" Поехали!");
        return result.toString();


    }

    public static int sumToN(int n){
        if(n<0){
            System.out.println("Только положительные");
        }
        int result= 0;
        for(int i = 1; i<=n; i++){
            result+=i;
        }
        return result;
    }

    public static boolean hasBug(String[] messages){
        for (int i = messages.length-1; i >= 0 ; i--){
            if("Bug".equals(messages[i])){
                return true;
            }
        }
        return false;
    }

    public static String getEvenInRange(int start, int end){
        if(start>end){
            return "Что то не то";
        }
        StringBuilder result = new StringBuilder();
        for (int i = start; i<=end;i++){
            if(i%2==0){
                result.append(i).append(" ");
            }
        }
        return result.toString();
    }

    //массивы
    public static int findMax(int[] arr){
        int max = arr[0];
        for(int i = 1;i< arr.length;i++){
            if(arr[i]>max){
                max=arr[i];
            }
        }
        return max;
    }

    public static String[] reverse(String[] arr){
        String[]result = new String[arr.length];
        for (int i=0;i<arr.length;i++){
            result[i]= arr[arr.length-1-i];
        }
        return result;
    }

    public static int calcAverage(List<Integer> list){
        int sum = 0;
        for (int num:list){
            sum+=num;
        }
        return sum/list.size();
    }

    public static List<String> removeSpecificName(List<String>list, String nameToRemove){
        List<String> result = new ArrayList<>();
        for (String name:list){
            if(!name.equals(nameToRemove)){
                result.add(name);
            }
        }
        return result;
    }
}

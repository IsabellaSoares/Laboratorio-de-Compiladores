/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_FIB {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public int fib (int n) {
         A a;
         int left;
         int right;

         if (n < 2) {
            return n;
         }
      }
   }

   private static class Program {
      public void run () {
         A a;
         System.out.println("");
         System.out.println("Ok-fib");
         System.out.println("The output should be :");
         System.out.println("34");
         a = new A();
         System.out.println(a.fib(10));
      }
   }

}

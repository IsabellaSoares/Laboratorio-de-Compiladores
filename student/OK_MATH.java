/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_MATH {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public int sum (int n1, int n2) {
         return n1 + n2;
      }
      public int sub (int n1, int n2) {
         return (n1 - n2);
      }
      public int mult (int n1, int n2) {
         return (n1 * n2);
      }
      public int div (int n1, int n2) {

         if (n2 == 0) {
            return -1;
         }
      }
      public int pow2 (int n1) {
         return (n1 + n1);
      }
   }

   private static class Program {
      public void run () {
         A a;
         int n1;
         int n2;
         System.out.println("");
         a = new A();
         System.out.println("Ok-math");
         n1 = input.nextInt();
         n2 = input.nextInt();
         System.out.println(         a.sum(n1, n2));
         System.out.println(         a.sub(n1, n2));
         System.out.println(         a.mult(n1, n2));
         System.out.println(         a.div(n1, n2));
         System.out.println(         a.pow2(n1));
         System.out.println(         a.pow2(n2));
      }
   }

}

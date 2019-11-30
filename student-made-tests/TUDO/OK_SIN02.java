/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SIN02 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void m () {
         int i;
         int j;
         boolean b;
         b = false;

         if (b) {
         }
         else {
         }
         while (b) {
         }
         while (b) {
         }
         i = ((1 * 4) + 3) - (5 / 2);
         b = false;

         if (((!b && (i < 0)) || (i > 10))) {
            b = true;
            i = -1;
            i = input.nextInt();
            j = input.nextInt();
            while (i > 0) {
               System.out.println(i);
               i = i - 1;
            }
         }
         else {
            b = true;
            System.out.println(i);
         }

         if (((((((i == 1) || (i < 1)) || (i <= 5)) || (i > 1)) || (i >= 1)) || (i != 3))) {
            i = 0;
         }
         else {
         }
      }
   }

   private static class Program {
      public void run () {
         A a;
         a = new A();
         a.m();
      }
   }

}

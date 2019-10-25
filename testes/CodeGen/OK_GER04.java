/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_GER04 {
   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void m () {
         int i;
         boolean b;
         System.out.print(6 + " ");
         i = 1;
         while (i <= 5) {
            System.out.print(i + " ");
            i = i + 1;
         }
         b = false;
         while (b != true) {
            System.out.print(6 + " ");
            b = !b;
         }
      }
   }

   private static class Program {
      public void run () {
         A a;
         System.out.println("6 1 2 3 4 5 6");
         a = new A();
         a.m();
      }
   }

}

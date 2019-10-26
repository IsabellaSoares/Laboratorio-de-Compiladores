/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_GER02 {
   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void m () {
         System.out.print(6 + " ");
         System.out.print(1 + " ");
         System.out.print(1 + 1 + " ");
         System.out.print(4 - 1 + " ");
         System.out.print((6 - 3) + 1 + " ");
         System.out.print(10 / 2 + " ");
         System.out.print(2 * 3 + " ");
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

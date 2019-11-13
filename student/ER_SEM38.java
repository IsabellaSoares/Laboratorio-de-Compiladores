/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class ER_SEM38 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
   }

   private static class B extends A {
      public void run () {
         A a;
         A b;
         a = new A();
         b = a;
      }
   }

   private static class Program {
      public void run () {
      }
   }

}

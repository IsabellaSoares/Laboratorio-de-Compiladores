/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class ER_SEM35 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void m () {
         return 0;
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

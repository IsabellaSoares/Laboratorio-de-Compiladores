/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class ER_SEM54 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public int zero () {
         return 0;
      }
   }

   private static class B extends A {
      public int zero () {
         return 1;
      }
   }

   private static class Program {
      public void run () {
      }
   }

}

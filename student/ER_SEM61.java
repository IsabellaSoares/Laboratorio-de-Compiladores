/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class ER_SEM61 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void m () {
      }
   }

   private static class B extends A {
      public void p () {
      }
   }

   private static class C extends B {
      public void r () {
      }
   }

}

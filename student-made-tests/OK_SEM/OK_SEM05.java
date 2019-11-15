/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SEM05 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      private       int n;
      public void set (int pn) {
         this.n = pn;
      }
      public int get () {
         return this.n;
      }
   }

   private static class B extends A {
      @Override void set (int pn) {
         System.out.print(pn);
         super.set(pn);
      }
   }

}

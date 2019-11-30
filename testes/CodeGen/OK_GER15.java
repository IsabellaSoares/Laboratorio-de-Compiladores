/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_GER15 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      int i;
      int j;
      private void p () {
         System.out.print(this.i + " ");
      }
      private void q () {
         System.out.print(this.j + " ");
      }
      public void init_A () {
         this.i = 1;
         this.j = 2;
      }
      public void call_p () {
         this.p();
      }
      public void call_q () {
         this.q();
      }
      public void r () {
         System.out.print(this.i + " ");
      }
      public void s () {
         System.out.print(this.j + " ");
      }
   }

}

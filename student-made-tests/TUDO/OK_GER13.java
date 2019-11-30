/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_GER13 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      int n;
      private void p1 () {
         System.out.print("999 ");
      }
      public void set (int pn) {
         System.out.print(1 + " ");
         this.n = pn;
      }
      private void p2 () {
         System.out.print("888 ");
      }
      public int get () {
         return this.n;
      }
      public void print () {
         System.out.print("A ");
      }
   }

   private static class B extends A {
      private void p2 () {
      }
      @Override public void set (int pn) {
         System.out.print(pn + " ");
         super.set(pn);
      }
      public void p1 () {
         System.out.print(2 + " ");
      }
      @Override public void print () {
         System.out.print("B ");
      }
   }

}

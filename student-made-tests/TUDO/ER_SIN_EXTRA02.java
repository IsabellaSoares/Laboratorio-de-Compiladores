/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class ER_SIN_EXTRA02 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      int num1;
      int num2;
      int result;
      public int mult () {
         return this.num1 * this.num2;
      }
      public void div (int num3, int num4) {
         this.result = num3 / num4;
      }
   }

   private static class B {
      String name;
      public void setName (String name) {
         this.name = name;
      }
      public String getName () {
         return this.name;
      }
   }

}

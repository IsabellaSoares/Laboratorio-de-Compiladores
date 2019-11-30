/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SEM03 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class Program {
      public void run () {
         String r, s;
         r = "Ola";
         s = "Tudo bem?";

         if (((r == s) && (r != s) && (r == null) && (null == r) && (r == "Ola") && ("Ola" == r) && (r != null) && (r != "Ola") && ("Ola" != r) && ("nil" == null))) {
            System.out.println("impossivel");
         }
         else {
         }
      }
   }

}

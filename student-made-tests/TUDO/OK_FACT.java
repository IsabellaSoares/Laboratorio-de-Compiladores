/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_FACT {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public int fact (int n) {
         A a;
         int ans;

         if ((n == 0)) {
            return 1;
         }
         else {
            a = new A();
            ans = a.fact(n - 1);
            ans = ans * n;
            return ans;
         }
      }
   }

   private static class Program {
      public void run () {
         A a;
         System.out.println("");
         System.out.println("Ok-fact");
         System.out.println("The output should be :");
         System.out.println("120");
         a = new A();
         System.out.println(a.fact(5));
      }
   }

}

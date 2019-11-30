/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_GER05 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void m () {
         int a, b, c, d, e, f;
         a = input.nextInt();
         b = input.nextInt();
         c = input.nextInt();
         d = input.nextInt();
         e = input.nextInt();
         f = input.nextInt();
         System.out.print(a);
         System.out.print(b);
         System.out.print(c);
         System.out.print(d);
         System.out.print(e);
         System.out.print(f);
      }
   }

   private static class Program {
      public void run () {
         A a;
         System.out.println("");
         System.out.println("Ok-ger05");
         System.out.println("The output should be what you give as input.");
         System.out.println("Type in six numbers");
         a = new A();
         a.m();
      }
   }

}

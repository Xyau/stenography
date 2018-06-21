/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criptografia;

/**
 *
 * @author Acer
 */
public class Criptografia {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (int i=0; i<20; i++)System.out.println((int)(Math.random()));
        //System.outt.println()
    }
    
    
    public static void Vigenere (String mensaje, String clave){
        char [] chars=mensaje.toCharArray();
        char [] cl = clave.toCharArray();
        for (int i=0; i<mensaje.length(); i++)
        {
            char c = chars[i];
            char d = cl[i%clave.length()]; 
            if (chars[i]=='Ñ')
                chars[i]='N';
            chars[i]=(char) (c + d -'A' + 1);
            if (chars[i]>'Z')
                chars[i]=(char)(chars[i]-26);
            if (chars[i]=='O')
                chars[i]='Ñ';
        }
        System.out.println(new String(chars));
    }
            
    
    public static void critograma () {
        String cadena = "VKXYKBKXGKSGWAKQQGYIUYGYWAKXKGQRKSZKJKYKKYIUSYMAÑX";
        char [] chars=cadena.toCharArray();
        for(int j=0; j<=26; j++)
        {
            for (int i=0; i<cadena.length(); i++)
            {
                char c = chars[i] ;
                if(c=='Z') 
                    chars[i]='A';
                else
                    chars[i]=(char) (c + 1);
            }
            System.out.println(new String(chars));
        }
    }
    
}

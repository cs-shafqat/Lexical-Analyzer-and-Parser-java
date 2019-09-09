/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package part1;
import java.io.*;
/**
 *
 * @author Shafqat Dogar
 * BCS02123339
 * Sec-(A)
 */
public class Lexical_Analyser {
    static boolean iskeyword(String str)
    {
        String keyword[] ={"int","float","if","while", "else", "return", "break", "continue" ,"void"};
        if(!Character.isLowerCase(str.charAt(0)))
        {
           return false;
        }
        for(int i=0;i<9;i++)
        {
            if(str.matches(keyword[i]))
            {     
               return true;
            }
        }
        return false;
    }
   // static int count=0;
   static void tokenize(String str) throws IOException{
       FileWriter fileWriter =new FileWriter("output.txt",true);
        try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            String lexeme="";
           // bufferedWriter.write(++count);
            for(int i=0;i<str.length();i++){
                if(Character.isLetter(str.charAt(i))){
                    lexeme+=str.charAt(i++);
                    while(i<str.length()&&(Character.isLetterOrDigit(str.charAt(i))||str.charAt(i)=='_')){
                        lexeme+=str.charAt(i++);
                    }
                    i--;
                    if(iskeyword(lexeme)){
                        System.out.println("KeyWord : "+lexeme);
                        bufferedWriter.write(lexeme+" ");
                    }
                    else{
                        System.out.println("Identifier : "+lexeme);
                        bufferedWriter.write("identifier ");
                    }
                    lexeme="";
                }
                else if(Character.isDigit(str.charAt(i))){
                    int flag1=0,flag2=0;
                    lexeme+=str.charAt(i++);
                    while(i<str.length()&&(Character.isDigit(str.charAt(i))||str.charAt(i)=='.'||str.charAt(i)=='E'||str.charAt(i)=='+'||str.charAt(i)=='-')){
                        if(Character.isDigit(str.charAt(i))){
                            lexeme+=str.charAt(i++);
                        }
                        else if(str.charAt(i)=='.' && flag1==0){
                            if(Character.isDigit(str.charAt(i+1)) && flag2==0){
                                flag1=1;
                                lexeme+=str.charAt(i++);
                            }
                            else{
                                break;
                            }
                        }
                        else if(str.charAt(i)=='E' && flag2==0&& i<str.length()-1){
                            if(Character.isDigit(str.charAt(i+1))){
                                flag2=1;
                                lexeme+=str.charAt(i++);    
                            }
                            else if((str.charAt(i+1)=='+' || str.charAt(i+1)=='-') && i<str.length()-2  ){
                                if(Character.isDigit(str.charAt(i+2))){
                                    flag2=1;
                                    lexeme+=str.charAt(i++);
                                }
                            }
                            else{
                                break;
                            }
                        }
                        else if((str.charAt(i)=='+'||str.charAt(i)=='-') && i<str.length()-1){
                            if(str.charAt(i-1)=='E' && Character.isDigit(str.charAt(i+1))){
                                lexeme+=str.charAt(i++);
                            }
                            else{
                                break;
                            }
                        }
                        else{
                            break;
                        }
                        
                    }
                    i--;
                    System.out.println("Number : "+lexeme);
                    bufferedWriter.write("num ");
                    lexeme="";
                }
                else if(str.charAt(i)=='+'||str.charAt(i)=='-'){
                    System.out.println("Addop : "+str.charAt(i));
                    bufferedWriter.write("addOp ");
                }
                else if(str.charAt(i)=='*'||str.charAt(i)=='/'){
                    System.out.println("mulop : "+str.charAt(i));
                    bufferedWriter.write("mulOp ");
                }
                else if(str.charAt(i)=='<'||str.charAt(i)=='>'||str.charAt(i)=='='||str.charAt(i)=='!'){
                    lexeme+=str.charAt(i++);
                    if(i<str.length()&&str.charAt(i)=='='){
                        lexeme+=str.charAt(i++);
                    }
                    i--;
                    if(lexeme.matches("=")){
                        System.out.println("assignop : "+lexeme);
                        bufferedWriter.write("= ");
                    }
                    else if(lexeme.matches("!")){
                        System.out.println("not : "+lexeme);
                        bufferedWriter.write("not ");
                    }
                    else{
                        System.out.println("relop : "+lexeme);
                        bufferedWriter.write("relOp ");
                    }
                    lexeme="";
                }
                else if(i<str.length()-1 &&(str.charAt(i)=='&'&& str.charAt(i+1)=='&')){
                    lexeme+=str.charAt(i++);
                    lexeme+=str.charAt(i);
                    System.out.println("and : "+lexeme);
                    bufferedWriter.write("and ");
                    lexeme="";
                }
                else if(i<str.length()-1 &&(str.charAt(i)=='|'&& str.charAt(i+1)=='|')){
                    lexeme+=str.charAt(i++);
                    lexeme+=str.charAt(i);
                    System.out.println("or : "+lexeme);
                    bufferedWriter.write("or ");
                    lexeme="";
                }
                else if(str.charAt(i)=='('){
                    System.out.println("( : "+str.charAt(i));
                    bufferedWriter.write("( ");
                }
                else if(str.charAt(i)==')'){
                    System.out.println(") : "+str.charAt(i));
                    bufferedWriter.write(") ");
                }
                else if(str.charAt(i)=='{'){
                    System.out.println("{ : "+str.charAt(i));
                    bufferedWriter.write("{ ");
                }
                else if(str.charAt(i)=='}'){
                    System.out.println("} : "+str.charAt(i));
                    bufferedWriter.write("} ");
                }
                else if(str.charAt(i)=='['){
                    System.out.println("[ : "+str.charAt(i));
                    bufferedWriter.write("[ ");
                }
                else if(str.charAt(i)==']'){
                    System.out.println("] : "+str.charAt(i));
                    bufferedWriter.write("] ");
                }
                else if(str.charAt(i)==';'){
                    System.out.println("; : "+str.charAt(i));
                    bufferedWriter.write("; ");
                }
                else if(str.charAt(i)==','){
                    System.out.println(", : "+str.charAt(i));
                    bufferedWriter.write(", ");
                }
                else if(Character.isWhitespace(str.charAt(i))){
                    
                }
                else{
                    System.out.println("Error : "+str.charAt(i));
                    bufferedWriter.write("error ");
                }
            }
            bufferedWriter.newLine();
        }
   }
  
    //public static void main(String[] args) {
   public static void main(String args) {
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output.txt")))) {}
        catch (IOException e) {}
        String line;
        
        try {
            //FileReader fileReader =new FileReader("input.txt");
            FileReader fileReader =new FileReader(args);
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                int i=1;
                while((line = bufferedReader.readLine()) != null) {
                    System.out.println("Line#"+i++);
                    tokenize(line);
                    System.out.println("    --------------------");
                }
            }
        }
        
        catch(Exception ex) {}
    }
}
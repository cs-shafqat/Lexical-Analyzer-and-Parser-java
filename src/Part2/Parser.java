/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Part2;
import part1.Lexical_Analyser;
import java.io.*;
import java.util.*;
/**
 *
 * @author Shafqat Dogar
 * BCS02123339
 * Sec-(A)
 */

public class Parser {

    HashMap<String,HashMap<String,String>>  parseTable = new HashMap<String,HashMap<String,String>>();  
    HashMap<String,String[]>  grammarMap = new HashMap<String,String[]>();
    String startSymbol = "Program";
    static String [] inputTokens;
    // This function reads input tokens from a file and stores it in a global string array called inputTokens
    void readInput(File f)
    {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        try {
            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            int count=0;
            String s="";
            while (dis.available() != 0) {
                s += dis.readLine()+" ";
            }
            s+="$";  // Insert $ at end of input for matching wit $ in symbol in stack
            inputTokens = s.split(" ");
            for(int j=0;j<inputTokens.length;j++){
                inputTokens[j]  = inputTokens[j].replaceAll("\\u00a0", "");
            }
            fis.close();
            bis.close();
            dis.close();
        }
        catch (Exception ex) {}
    }
    // This function reads parse table from a file. The productions should be tab separated 
    //		and each symbol within a production should be space separated		
    void readParseTable(File f)
    {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        try {
            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            int line = 0; 
            String [] tokens = {};
            while (dis.available() != 0) {
                String s = dis.readLine();
                line ++;
                String [] str = s.split("\t");
                if(line == 1)
                {
                    tokens = new String[str.length-1]; 
                    for(int i=1;i<str.length;i++)
                    {
                        tokens[i-1] = str[i];
                    }
                }
                else
                {
                    String nonTerminal = str[0].replaceAll("\\u00a0", "");
                    HashMap production = new HashMap();
                    for(int i=1;i<str.length;i++)
                    {
                        production.put(tokens[i-1],str[i]);
                    }

                    for(int i=str.length-1;i<tokens.length;i++)
                    {
                        production.put(tokens[i],"");
                    }
                    parseTable.put(nonTerminal,production);
                }
            }
            fis.close();
            bis.close();
            dis.close();
        } 
        catch (Exception ex) {}
    }
    // This function reads grammar from a file
    void readGrammar(File f){
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        try {
            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            int line = 0; 
            String [] tokens = {};
            while (dis.available() != 0) {
                String s = dis.readLine();
                if(s.length() > 0){
                    String [] str = s.split("\\.");
                    String num =  str[0];
                    String [] grammar = str[1].split("-->");
                    String [] rhs = grammar[1].split("\\|");
                    String [] production = rhs[0].split(" ");
                    for(int j=0;j<production.length;j++)
                    {
                        production[j]  = production[j].replaceAll("\\u00a0", "");
                    }
                    grammarMap.put(num,production);
                }
            }
            fis.close();
            bis.close();
            dis.close();
        }
        catch (Exception ex) {}
    }
    boolean isTerminal(String sym)
    {
        return !Character.isUpperCase(sym.charAt(0));
    }
    void parse( ){
        Stack stk=new Stack();
        stk.push("$");
        stk.push(startSymbol);
        int i=0,count=1;
        String top;
        Parser parser = new Parser();
        System.out.println("\n               *****Parsing*****");
	while(!stk.isEmpty() && i<inputTokens.length ){
            if("".equals(inputTokens[i])){
                count++;
                i++;
                continue;
            }
            if("error".contentEquals(inputTokens[i])){
                break;
            }
            top=(String) stk.pop();
            if("".equals(top)){
                continue;
            }
            if(parser.isTerminal(top)){
                if( top.contentEquals(inputTokens[i])){
                    i++;             
                }
                else{
                    break;
                }
            }
            else{
                String production=parseTable.get(top).get(inputTokens[i]);
                if(production.isEmpty()){
                    break;
                }
                else if(production.equalsIgnoreCase("epsilon")){}
                else{
                   String [] rhs=grammarMap.get(production);
                   for(int j=rhs.length-1;j>0;j--){
                       stk.push(rhs[j]);
                   }
                }
            }
        }
        if(!stk.isEmpty() || i<inputTokens.length){
            System.out.println("\tparse error  :: At Line#"+count);
        }
        else{
            System.out.println("\tsuccessfully parsed input");
        }
    }
    public static void main(String[] args) {
        try{
            Parser parser = new Parser();
            Lexical_Analyser.main("input.txt");
            parser.readInput(new File("output.txt"));
            parser.readParseTable(new File("parseTableCformatted.txt"));
            parser.readGrammar(new File("grammarC++.txt"));
            parser.parse();
        } 
        catch (Exception ex) {}
    }
}
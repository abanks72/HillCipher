package hillcipher;

import java.util.*;
import java.io.*;


public class HillCipher {
    
    String text;
    String[] sgrams;
    char[][] cgrams;
    int[][] ngrams;
    int gramsize;
    
    HillCipher(File file, int gramsize) throws FileNotFoundException{
        
        Scanner s = new Scanner(file);
        this.gramsize = gramsize;
        this.text = new String(s.next().toLowerCase());
        
        while(s.hasNext()){
            text+=s.next().toLowerCase();
        }
        System.out.println(text + "\n");
        text = text.replaceAll("[^A-Za-z]+", "");
    }
    
    void createGrams(){ 
        Scanner s = new Scanner(text);
        s.useDelimiter("");
        
        sgrams = new String[((text.length())/gramsize)+1];
        cgrams = new char[(text.length()/gramsize)+1][gramsize];
        
        for(int i = 0; i<((text.length())/gramsize)+1; i++){
            sgrams[i] = "";
            for(int j=0; j<gramsize; j++){
                if(!s.hasNext())
                    sgrams[i] += "x";
                else
                    sgrams[i] += s.next();
            }
        }
        for(int i = 0; i<((text.length())/gramsize)+1; i++){
            cgrams[i] = sgrams[i].toCharArray();
        }
    }
    
    void getNumValues(){
        ngrams = new int[(text.length()/gramsize)+1][gramsize];
        for(int i = 0; i<((text.length())/gramsize)+1; i++){
            for(int j=0; j<gramsize; j++){
                ngrams[i][j] = (int)cgrams[i][j] - 97;
            }
        }
    }
    
    public int[][] getNGrams(){
        return ngrams;
    }
    public int getLength(){
        return (text.length()/gramsize)+1;
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        
        String answer = new String("");
        File file1 = new File(args[0]);
        File file2 = new File(args[1]);
        Matrix m = new Matrix(file1);
        HillCipher cipher = new HillCipher(file2, m.getGramSize());
        
        cipher.createGrams();
        cipher.getNumValues();
        
        answer = m.multMatrix(cipher.getNGrams(), cipher.getLength());
        char[] ans = new char[answer.length()+1];
        ans = answer.toCharArray();
        
        for(int i=0; i<answer.length(); i++){
            if(i%80==0){
                System.out.println("");
                System.out.print(ans[i]);
            }else{
                System.out.print(ans[i]);
            }
        }
    }
}

class Matrix{
    
    int gramsize;
    int[][] matrix;
    
    Matrix(File file) throws FileNotFoundException{
        Scanner s = new Scanner(file);
        gramsize = s.nextInt();
        matrix = new int[gramsize][gramsize];
        
        for(int i=0; i<gramsize; i++){
            for(int j=0; j<gramsize; j++){
                matrix[i][j] = s.nextInt();
            }
        }
        
        for(int i=0; i<gramsize; i++){
            for(int j=0; j<gramsize; j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("");
        }
    }
    
    public String multMatrix(int[][] ngrams, int length){
        
        String answer = new String("");
        int[] colres = new int[gramsize];
        char[] temp = new char[1];
        
        for(int i=0; i<length; i++){
            for(int j=0; j<gramsize; j++){
                for(int k=0; k<gramsize; k++){
                    colres[j] += matrix[j][k]*ngrams[i][k];
                }
                colres[j] = colres[j]%26;
                temp[0] = (char)(colres[j]+97);
                answer+= Arrays.toString(temp);
            }
        }
        answer = answer.replaceAll("[^A-Za-z]+", "");
        return answer;
    }
    public int getGramSize(){
        return this.gramsize;
    }
}

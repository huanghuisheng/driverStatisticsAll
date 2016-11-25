package huang.statistics.util;

public class Singleton  
{  
    private Singleton(){ }  
      
    public static Singleton getInstance()  
    {  
        return Nested.instance;       
    }  
      
    //在第一次被引用时被加载  
    static class Nested  
    {  
        private static Singleton instance = new Singleton();  
    }  
    public static void main(String args[])  
    {  
        Singleton instance = Singleton.getInstance();  
        Singleton instance2 = Singleton.getInstance();       
        System.out.println(instance);      
        System.out.println(instance2);  
    }  
} 
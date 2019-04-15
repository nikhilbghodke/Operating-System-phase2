package com.company;
import java.io.*;
import java.util.Random;

public class OS {


    //Core data structures for Operating System

    private char [][]memory= new char[300][4];
    private char []buffer= new char[40];
    private char []R=new char[4];
    private char []IR=new char[4];
    private int IC;
    private int T;
    private int SI;
    private int TI;
    private int PI;
    private int ptr;
    private boolean validbit;
    private PCB pcb;


    //Non core data structures
    private int memory_used;
    private int pageTable_used;
    private int data_card_skip=0;
    private String input_file;
    private String output_file;
    private FileReader input;
    private BufferedReader fread;
    public  FileWriter output;
    private BufferedWriter fwrite;
    private Random rand;
    private int tline;

    public OS(String file,String output){
        this.input_file=file;
        this.SI=0;
        try {
            this.input = new FileReader(file);
            this.fread= new BufferedReader(input);
            this.output=new FileWriter(output);
            //this.fwrite= new BufferedWriter(this.output);
            //fwrite.write("Svsndjd");
            //fwrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LOAD()
    {
        int flag=0;
        String line;
        try {

            while((line=fread.readLine()) != null)
            {
                buffer=line.toCharArray();
                if(buffer[0]=='$'&& buffer[1]=='A'&&buffer[2]=='M'&& buffer[3]=='J') {
                    flag=0;
                    System.out.println("Program card detected");
                    init();
                    int linel=Integer.parseInt(line.substring(8,12));
                    int time=Integer.parseInt(line.substring(12,16));
                    //System.out.println(linel+"chal");
                    //System.out.println(time);
                    pcb=new PCB(linel,time);
                    continue;
                }
                else if(buffer[0]=='$'&& buffer[1]=='D'&&buffer[2]=='T'&& buffer[3]=='A')
                {
                    System.out.println("Data card detected");
                    execute();
                    flag=2;
                    continue;
                }
                else if(buffer[0]=='$'&& buffer[1]=='E'&&buffer[2]=='N'&& buffer[3]=='D')
                {
                    System.out.println("END card detected");

                    output.write("\n\n\n");
                    print_memory();
                    continue;
                }
                if(memory_used==100)
                {   //abort;
                    System.out.println("Abort due to exceed memory usage");
                }
                //System.out.println(line.length());
                System.out.println(line);

                //System.out.println("Mai load hora");
                if(flag!=2) {

                    System.out.println("ur program starts here");
                    int a = allocate();
                    char[] chars = ("" + a/10).toCharArray();
                    ++pageTable_used;
                    memory[ptr*10+pageTable_used][0]=chars[0];
                    //System.out.println(memory[ptr*10+pageTable_used][0]);
                    //System.out.println(pageTable_used+" "+ptr+" a is "+a);
                    if(chars.length>1)
                    memory[ptr*10+pageTable_used][1]=chars[1];
                    for (int i = 0; i < line.length(); ) {
                        //System.out.println(buffer[i]);
                        memory[a][i % 4] = buffer[i];
                        i++;
                        if (i % 4 == 0)
                            a++;
                    }
                    //print_memory();

                }

            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(memory_used);
        //print_memory();
    }
    private void execute(){

//print_memory();
        while(1<2)
        {
           // System.out.println(IC);
            int ic=map(IC);
            System.out.println(IC +" is this" +ic);
            //print_memory();
            IR[0]=memory[ic][0];
            IR[1]=memory[ic][1];
            IR[2]=memory[ic][2];
            IR[3]=memory[ic][3];
            if(IR[0]!='H')
            try {
                String line = new String(IR);
                int num = Integer.parseInt(line.substring(2));
            }
            catch(Exception e )
            {
                System.out.println("Operand error");

                try {
                    output.write("Program aborted abnormally");
                    output.write("\n");
                    output.write("Operand error\n");
                    output.write("IC :"+IC+"\t");
                    output.write("IR :"+ new String(IR)+"\t");
                    output.write("TTL :"+pcb.TTL+"\t");
                    output.write("TLL :"+pcb.TLL+"\t");
                    output.write("TTC :"+pcb.TTC+"\t");
                    output.write("TLC :"+pcb.TLC+"\n\n\n");

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            }
            pcb.TTC++;
            if(pcb.TTC>pcb.TTL)
            {
                System.out.println("Total time exceeded");
                try {
                    output.write("Program terminated abnormally\n");
                    output.write("Time limit exceeded\n");
                    try {
                        output.write("IC :"+IC+"\t");
                        output.write("IR :"+ new String(IR)+"\t");
                        output.write("TTL :"+pcb.TTL+"\t");
                        output.write("TLL :"+pcb.TLL+"\t");
                        output.write("TTC :"+pcb.TTC+"\t");
                        output.write("TLC :"+pcb.TLC+"\n\n\n");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            System.out.println(new String(IR));
            if(IC==100)
                break;
            IC++;
            if(IR[0]=='G' && IR[1]=='D')
            {
                pcb.TTC++;

                String line = new String(IR);
                int num=Integer.parseInt(line.substring(2));
                //System.out.println(num +"Sahi hai");;
                validbit=true;
                ic=map(num);
                //System.out.println(PI +"Kya hai");
                if(ic==-1) {
                    //System.out.println("\n\n\n\n");
                    masterMode();
                }
               // print_memory();
                SI=1;
                masterMode();
                //print_memory();
            }
            else if(IR[0]=='P'&&IR[1]=='D')
            {
                String line = new String(IR);
                int num=Integer.parseInt(line.substring(2));
                ic=map(num);
                if(ic==-1)
                {
                    System.out.println("Invalid page fault");
                    try {
                        output.write("Program terminated abnormally\n");
                        output.write("Invalid page fault\n");
                        try {
                            output.write("IC :"+IC+"\t");
                            output.write("IR :"+ new String(IR)+"\t");
                            output.write("TTL :"+pcb.TTL+"\t");
                            output.write("TLL :"+pcb.TLL+"\t");
                            output.write("TTC :"+pcb.TTC+"\t");
                            output.write("TLC :"+pcb.TLC+"\n\n\n");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                SI=2;
                masterMode();
            }
            else if(IR[0]=='L' && IR[1]=='R')
            {
                String line = new String(IR);

                //System.out.println(line);
                //System.out.println(line.substring(2));
                int num=Integer.parseInt(line.substring(2));
                ic=map(num);
                if(ic==-1)
                {
                    System.out.println("Invalid page fault");
                    try {
                        output.write("Program terminated abnormally\n");
                        output.write("Invalid page fault");
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                R[0]=memory[ic][0];
                R[1]=memory[ic][1];
                R[2]=memory[ic][2];
                R[3]=memory[ic][3];
            }else if(IR[0]=='S' && IR[1]=='R')
            {
                String line = new String(IR);
                int num=Integer.parseInt(line.substring(2));
                ic=map(num);
                if(ic==-1)
                    masterMode();
                ic=map(num);
                memory[ic][0]=R[0];
                memory[ic][1]=R[1];
                memory[ic][2]=R[2];
                memory[ic][3]=R[3];
            }
            else if(IR[0]=='C' && IR[1]=='R')
            {
                String line = new String(IR);
                int num=Integer.parseInt(line.substring(2));
                ic=map(num);
                if(ic==-1)
                {
                    System.out.println("Invalid page fault");
                    try {
                        output.write("Program terminated abnormally\n");
                        output.write("Invalid page fault");
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                if(memory[ic][0]==R[0]&& memory[ic][1]==R[1]&& memory[ic][2]==R[2]&& memory[ic][3]==R[3])
                {
                    T=1;
                }
                else
                {
                    T=0;

                }
            }
            else if(IR[0]=='B' && IR[1]=='T')
            {
                if(T==1)
                {
                    String line = new String(IR);
                    int num=Integer.parseInt(line.substring(2));
                    IC=Integer.parseInt(line.substring(2));
                    T=0;
                }
            }
            else if(IR[0]=='H')
            {
                SI=3;
                //System.out.println("Gel ka");
                print_memory();
                masterMode();

                //init();
                break;
            }
            else
            {
                try {
                    output.write("Program terminated abnormally\n");
                    output.write("Opcode error detected\n");
                    try {
                        output.write("IC :"+IC+"\t");
                        output.write("IR :"+ new String(IR)+"\t");
                        output.write("TTL :"+pcb.TTL+"\t");
                        output.write("TLL :"+pcb.TLL+"\t");
                        output.write("TTC :"+pcb.TTC+"\t");
                        output.write("TLC :"+pcb.TLC);
                        output.write("\n\n\n");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void masterMode() {
        int i=this.SI;
        if(PI==3 && TI==0 && validbit)
        {
            IC--;
            int a=allocate();
            //memory[ptr+(++pageTable_used)][]
            String line=String.valueOf(a/10);
            //line="10";
            //System.out.println(a +"is allocated");
            //System.out.println(String.valueOf(a/10).toCharArray()[0]+" aasgthndnsf uads d uasn m");
            String line1 = new String(IR);
            int num=Integer.parseInt(line1.substring(2));
            int z=ptr*10+num/10;
            System.out.println(a+"is alocated and maped to "+z);
            memory[z][0]=line.toCharArray()[0];
            if(line.length()>1)
            memory[z][1]=line.toCharArray()[1];
            PI=0;
            //System.out.println(a +"is new allocated");
        }
        if(SI==1 && TI==0)
        {
            Read();
            IC++;
        }
        else if(SI==2)
        {
            Write();
        }
        else if(i==3)
        {

        }
        SI=0;
    }

    private int Write() {
        pcb.TLC++;
        if(pcb.TLC>pcb.TLL)
        {
                System.out.println("Total line limit execeded");
            try {
                output.write("Program terminated abnormally\n");
                output.write("LineLimit exceeded\n");
                try {
                    output.write("IC :"+IC+"\t");
                    output.write("IR :"+ new String(IR)+"\t");
                    output.write("TTL :"+pcb.TTL+"\t");
                    output.write("TLL :"+pcb.TLL+"\t");
                    output.write("TTC :"+pcb.TTC+"\t");
                    output.write("TLC :"+pcb.TLC+"\n\n\n");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return -1;
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.exit(0);

        }
        String line = new String(IR);
        int num=Integer.parseInt(line.substring(2));
        //num=num*10;
        num=map(num);
        //System.out.println(num +" sahi haia sx dujx dhdn ");
        String t,total="";
        for(int i=0;i<10;i++)
        {
            t=new String(memory[num+i]);
            total=total.concat(t);
        }
        System.out.println(total+"In write");
        try {
            output.write(total);
            output.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
return 1;
    }

    private int Read() {
        int flag=0;
        //IR[3]='0';
        String line = new String(IR);

        int num=Integer.parseInt(line.substring(2));
            //num=num*10;
        System.out.println("number"+num+" converted in read"+ map(num));
        num=map(num);
        try {
            line=fread.readLine();


            //OUT of data card error here
            if(line.contains("$END"))
            {
                System.out.println("Out of Data Card error");
                output.write("Program terminated abnormally\n");
                output.write("Out of Data card error\n");
                try {
                    output.write("IC :"+IC+"\t");
                    output.write("IR :"+ new String(IR)+"\t");
                    output.write("TTL :"+pcb.TTL+"\t");
                    output.write("TLL :"+pcb.TLL+"\t");
                    output.write("TTC :"+pcb.TTC+"\t");
                    output.write("TLC :"+pcb.TLC+"\n\n\n");

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return -1;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer=line.toCharArray();
        for (int i = 0; i < line.length();) {
            //System.out.println(buffer[i]);
            memory[num][(i%4)]=buffer[i];
            i++;
            if(i%4==0)
                num++;
        }
        return 1;
    }

    public void init(){
        memory_used=0;
        memory=null;
        memory= new char[300][4];
        data_card_skip=0;
        T=0;
        TI=0;
        pageTable_used=-1;
        IC=0;
        tline=0;
        ptr=(int)(Math.random()*30);
    }
    public void print_memory(){
        for(int i=0;i<300;i++) {
            System.out.println("memory["+i+"] "+new String(memory[i]));
        }
        System.out.println("Your Ptr is "+ptr);
        System.out.println(pcb.TTC+" as "+ pcb.TLL);
    }

    private int allocate(){
        int a;
        while(1<2)
        {
            a=(int)(Math.random()*30);
            if(memory[a*10][0]=='\0' && a!=ptr)
                break;
        }
        //System.out.println("ptr is "+ptr+"pagetable used is"+pageTable_used);
        memory[ptr][0]=(char)(a/10);
        memory[ptr][1]=(char)(a%10);
        //pageTable_used++;
        //System.out.println(a*10+"is allocated");
        return a*10;
    }

    private int map(int va){
        if(va>100)
        {
            PI=2;
            return -1;
        }
        int pte=ptr*10+va/10;
        int fNo;
        String line = new String(memory[pte]);
        //System.out.println(line +"This is line");
        //System.out.println(line.length());
        if(memory[pte][0]=='\0') {
            TI=0;
            PI = 3;
            //System.out.println(pte+"pte is");
            return -1;
        }

        else if(memory[pte][1]!='\0') {
           // System.out.println(line.length()+"kya bo;ra tu");
            fNo = Integer.parseInt(line.substring(0, 2));
        }
        else
            fNo=Integer.parseInt(line.substring(0,1));

        int ra=fNo*10+va%10;

        return ra;
    }
}

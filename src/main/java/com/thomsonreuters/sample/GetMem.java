
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;

package com.thomsonreuters.sample;

/**
 * Program to test Java memory/thread allocation.
 * 
 */
public class GetMem implements Runnable {
   
    private static final int KB = 1024;
    
    
    public void run() {
        synchronized( this ) {
            try { wait(); } catch( Exception e ) {}
        }
    }
    
    
    public static String currMem() {
        Runtime r = Runtime.getRuntime();
        long mm = r.maxMemory() / KB;
        long tm = r.totalMemory() / KB;
        long um = tm - ( r.freeMemory() / KB );
        return " (MaxKB=" + mm + ", TotalKB=" + tm + ", UsedKB=" + um + ")";
    }
    

    public static void main(String[] args) throws Exception {
        
        ArrayList threads = new ArrayList();
        ArrayList bytes = new ArrayList();
        long size = 0;
        int count;
        String input, command;
        String[] inputs;
        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println( "Commands: T[hread] Number | B[ytes] Count | E[xit]\n" );
        while( true ) {
            try {
                System.out.print( "--> " );
                input = in.readLine();
                if( input.startsWith( "E" ) ) break;
                
                inputs = input.split( " ", 2 );
                command = inputs[0];
                try { count = Integer.parseInt( inputs[1] ); }
                catch( Exception e ) { count = 0; }
                if( count <= 0 ) {
                    System.out.println( "--> invalid count: " + count + currMem() );
                    continue;
                }
                
                if( command.startsWith( "T" ) ) {
                    for( int i=0; i<count; i++ ) {
                        Thread t = new Thread( new GetMem() );
                        t.start();
                        threads.add( t );
                    }
                    System.out.println( "--> total threads: " + threads.size() + currMem() );
                }
                else if( command.startsWith( "B" ) ) {
                    bytes.add( new byte[count] );
                    size += count;
                    System.out.println( "--> total bytes: " + size + currMem() );
                }
                else if( command.startsWith( "K" ) ) {
                    count = count * KB;
                    bytes.add( new byte[count] );
                    size += count;
                    System.out.println( "--> total bytes: " + size + currMem() );
                }
                else if( command.startsWith( "M" ) ) {
                    count = count * KB * KB;
                    bytes.add( new byte[count] );
                    size += count;
                    System.out.println( "--> total bytes: " + size + currMem() );
                }
                else {
                    System.out.println( "--> unknown command: " + command + currMem() );
                }
            }
            catch( Throwable e ) {
                System.out.println( "--> " + e );
                e.printStackTrace( System.out );
            }
        }
        System.out.println( "Done." );
        System.exit( 0 );
    }
}

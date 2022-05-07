package com.SpotifyButHalfAsGood;

import com.rabbitmq.client.UnblockedCallback;

/**
 * Hello world!
 *
 */
public class App 
{

    public void pp(){
        System.out.println(123312);
    }

    public static void main( String[] args )
    {
        App p=new App();

        final UnblockedCallback pp = p::pp;
        p.pp();

    }
}

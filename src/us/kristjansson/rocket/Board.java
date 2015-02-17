//
package us.kristjansson.rocket;
//
import java.lang.Thread;
//
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
//import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
//import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
//import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
//import com.pi4j.io.gpio.event.GpioPinListenerDigital;


public class Board
{

	
	static void Blink()
	{
        System.out.println("Blink leds...");
        
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        // provision gpio pins #01 and #03 as an output pin and blink
        final GpioPinDigitalOutput led1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
        //final GpioPinDigitalOutput led2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);

        // continuously blink the led every  
        led1.blink(250);
        //led2.blink(250);
        
        // Wait for 3 secs
        for( int i=3; i > -1; i--) 
        {
            try 
            {
				Thread.sleep(1000);
			} 
            catch (InterruptedException e) 
            {
				e.printStackTrace();
			}
        }
        
        // Stop blinking
        led1.clearProperties();
        //led2.clearProperties();

        // stop all GPIO activity/threads
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller

	}

	// Fires the rocket using GPIO on the RaspBerry 	
	static void Fire()
	{
		System.out.println("Fire rocket !");
	}
	
	
}

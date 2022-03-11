/*
A program that controls an addressable led strip based on i2c commands 
the program exapects a single byte over i2c representing an integer value 
and ignores the registor 

on startup the strip will perform a small startup animation

*/

// might want to diable i2c for some patterns like the startup ones

#include <FastLED.h>
#include <Wire.h>

#define NUM_LEDS_UPPERS 120
#define NUM_LEDS_UNDERGLOW 135

#define DATA_PIN_UPPERS 5
#define DATA_PIN_UNDERGLOW 4

#define UPPER 1
#define UNDERGLOW 2

#define FRONT_UPPER_START 0
#define FRONT_UPPER_END 59

#define BACK_UPPER_START 60
#define BACK_UPPER_END 119

#define ON_BRIGHTNESS 30
#define LED_TYPE WS2812B
#define COLOR_ORDER GRB

#define UPDATES_PER_SECOND 100

#define FORWARD_COLOR Blue
#define FORWARD_COLOR_RED_ALLIANCE Red
#define FORWARD_COLOR_BLUE_ALLIANCE Blue

CRGB ledsUpper[NUM_LEDS_UPPERS];
CRGB ledsUnderglow[NUM_LEDS_UNDERGLOW];

#define I2C_ADDRESS 0x44
volatile int new_i2c_data = 0;
volatile int i2c_data = 0;
volatile int i2c_register = 0;

#define patternNum 13
// first set up the parameters to use in the pattern calling
// how often each pattern updates
unsigned long patternInterval[] = {
	// example patterns
	10, 10, 10, 200, 50,
	// all-lights patterns
	50, 50, 50, 50,
	// underglow-lights patterns
	50, 30, 50,
	// upper-lights patterns
	50
};
// for millis() when last update occurred
unsigned long lastUpdate[patternNum];
// should the pattern be called at all
boolean patternEnabled[patternNum] = {0};
// state machine variable for patterns - this initialises them to zero
byte patternState[patternNum];
int meteorStartLED[] = {0, 60};
int meteorStopLED[] = {59, 119};

// the array of pointers to each pattern
void (*patternPtrs[patternNum])(int index,byte state);


// ----------------------------------------------------------
// Execution and I2C


void setup() {
	Serial.begin(9600);

	FastLED.addLeds<LED_TYPE, DATA_PIN_UPPERS, COLOR_ORDER>(ledsUpper, NUM_LEDS_UPPERS).setCorrection(TypicalLEDStrip);
	FastLED.addLeds<LED_TYPE, DATA_PIN_UNDERGLOW, COLOR_ORDER>(ledsUnderglow, NUM_LEDS_UNDERGLOW).setCorrection(TypicalLEDStrip);
	FastLED.setBrightness(ON_BRIGHTNESS);

	// example patterns
	patternPtrs[0] = meteorRainRight;
	patternPtrs[1] = meteorRainRight;
	patternPtrs[2] = wave;
	patternPtrs[3] = solid;
	patternPtrs[4] = fadeAllStrips;

	// patterns for all the lights
	patternPtrs[5] = allFastRGBCycle;
	patternPtrs[6] = allSlowRGBCycle;
	patternPtrs[7] = allGreen;
	patternPtrs[8] = allOff;

	// patterns for the underglow lights
	patternPtrs[9] = underglowRed;
	patternPtrs[10] = underglowBlue;
	patternPtrs[11] = underglowOff;

	// patterns for the upper lights
	patternPtrs[12] = upperOff;

	startupEffect();
}


void loop() {
	for (int i=0; i<patternNum; i++) { // go through all the patterns and see if it is time to call one
		if (patternEnabled[i] && millis() - lastUpdate[i] > patternInterval[i]) {
			lastUpdate[i] = millis();
			callPatterns(i, patternState[i]);
		}
	}
}


void i2cReceiveEvent(int bytesReceived) {  //The first byte is the register and the rest of the bytes are data
	i2c_register = (int) Wire.read();
	i2c_data = (int) Wire.read(); 
	new_i2c_data= 1;

	if (bytesReceived > 2) {   //Throw away all the rest of the data past the first 2 bytes
		for (uint8_t a = 2; a < bytesReceived; a++) {  
			Wire.read();
		}
	}

	patternEnabled[i2c_data] = true;
}


// ----------------------------------------------------------
// Common pattern and lights functions


void callPatterns(int index, byte state) {
	(*patternPtrs[index])(index,state); //calls the pattern at the index of `index` in the array
}


void setPixel(int Pixel, byte red, byte green, byte blue, int strip) {
	if (strip == UPPER) {
		ledsUpper[Pixel].r = red;
		ledsUpper[Pixel].g = green;
		ledsUpper[Pixel].b = blue;
	}
	if (strip == UNDERGLOW) {
		ledsUnderglow[Pixel].r = red;
		ledsUnderglow[Pixel].g = green;
		ledsUnderglow[Pixel].b = blue;
	}
}


void setAll(byte red, byte green, byte blue, int strip) {
	if (strip == UPPER) {
		for (int i = 0; i < NUM_LEDS_UPPERS; i++ ) {
			setPixel(i, red, green, blue, strip);
		}
	}
	if (strip == UNDERGLOW) {
		for (int i = 0; i < NUM_LEDS_UNDERGLOW; i++ ) {
			setPixel(i, red, green, blue, strip);
		}
	}
	FastLED.show();
}


void setAll(byte red, byte green, byte blue) {
	setAll(red, green, blue, UPPER);
	setAll(red, green, blue, UNDERGLOW);
}


// ----------------------------------------------------------
// Effects


void startupEffect() {
	for (int i=0; i<3; i++) {
		patternEnabled[i] = true;
	}
	for (int i=3; i<5; i++) {
		patternEnabled[i] = false;
	}

	// Uncomment this and change its index to test different patterns
	// patternEnabled[5] = true;
}


// ----------------------------------------------------------
// All-light patterns


void allFastRGBCycle(int index, byte state) {
	allFillRainbowWithSpeed(120);
}


void allSlowRGBCycle(int index, byte state) {
	allFillRainbowWithSpeed(10);
}

void allFillRainbowWithSpeed(uint8_t speed) {
	// last argument is deltaHue (larger value means more color separations [weird, ik])
	fill_rainbow(ledsUpper, NUM_LEDS_UPPERS, beat8(speed, 255), 3);	
	FastLED.show();
}

/*
Reference code

void allSlowRGBCycle(int index, byte state) {
	static CRGBPalette16 currentPalette = RainbowColors_p;
	static CRGBPalette16 targetPalette;
	static TBlendType currentBlending = LINEARBLEND;
	// Serial.println("idle");
	uint8_t maxChanges = 24;
	// AWESOME palette blending capability.
	nblendPaletteTowardPalette(currentPalette, targetPalette, maxChanges);

	// That's the same as beatsin8(9);
	uint8_t wave1 = beatsin8(1, 0, 255);
	uint8_t wave2 = beatsin8(2, 0, 255);
	uint8_t wave3 = beatsin8(3, 0, 255);
	uint8_t wave4 = beatsin8(5, 0, 255);

	for (int i=0; i<NUM_LEDS_UPPERS; i++) {
		ledsUpper[i] = ColorFromPalette(currentPalette, i+wave1+wave2+wave3+wave4, 255, currentBlending); 
	}
	for (int i=0; i<NUM_LEDS_UNDERGLOW; i++) {
		ledsUnderglow[i] = ColorFromPalette(currentPalette, i+wave1+wave2+wave3+wave4, 255, currentBlending);
	}

	FastLED.show();

	EVERY_N_SECONDS(6) {
		targetPalette = CRGBPalette16(
		CHSV(random8(), 255, random8(128,255)),
		CHSV(random8(), 255, random8(128,255)),
		CHSV(random8(), 192, random8(128,255)),
		CHSV(random8(), 255, random8(128,255)));
	}
}
*/


void allGreen(int index, byte state) {
	setAll(0, 255, 0);
}


void allIdleOn(int index, byte state) {
	ledsUnderglow[beatsin16(19, 0, NUM_LEDS_UNDERGLOW - 1, 0, 0)] = CRGB::Green;
	ledsUnderglow[beatsin16(23, 0, NUM_LEDS_UNDERGLOW - 1, 0, 85)] = CRGB::Red;
	ledsUnderglow[beatsin16(31, 0, NUM_LEDS_UNDERGLOW - 1, 0, 170)] = CRGB::Yellow;

	ledsUpper[beatsin16(20, 0, NUM_LEDS_UPPERS - 1, 0, 0)] = CRGB::Green;
	ledsUpper[beatsin16(25, 0, NUM_LEDS_UPPERS - 1, 0, 85)] = CRGB::Red;
	ledsUpper[beatsin16(30, 0, NUM_LEDS_UPPERS - 1, 0, 170)] = CRGB::Yellow;

	// fadeToBlackBy(ledsUnderglow, NUM_LEDS_UNDERGLOW, 8);
	// fadeToBlackBy(ledsUpper, NUM_LEDS_UPPERS, 8);
	for (int i = 0; i < 5; i++) {
		blur1d(ledsUpper, NUM_LEDS_UPPERS, 50);
		blur1d(ledsUnderglow, NUM_LEDS_UNDERGLOW, 50);
	}

		FastLED.show();
}


void allOff(int index, byte state) {
	setAll(0, 0, 0, UPPER);
	setAll(0, 0, 0, UNDERGLOW);
}


// ----------------------------------------------------------
// Underglow-lights patterns


void underglowRed(int index, byte state) {
	patternEnabled[8] = false;
	setAll(255, 0, 0, UNDERGLOW);
	FastLED.show();
	patternEnabled[index] = false;
}


void underglowBlue(int index, byte state) {
	patternEnabled[7] = false;
	setAll(0, 0, 255, UNDERGLOW);
	FastLED.show();
	patternEnabled[index] = false;
}


void underglowOff(int index, byte state) {
	patternEnabled[7] = false;
	patternEnabled[8] = false;
	patternEnabled[index] = false;
	setAll(0, 0, 0, UNDERGLOW);
	FastLED.show();
}


// ----------------------------------------------------------
// Upper-lights patterns


void frontUpperOn(int index, byte state) {
	for (int j = FRONT_UPPER_START; j <= FRONT_UPPER_END; j++) {
		setPixel(j, 0, 0, 255, UPPER);
	}
	FastLED.show();
	patternEnabled[index] = false;
}


void backUpperOn(int index, byte state) {
	for (int j = BACK_UPPER_START; j <= BACK_UPPER_END; j++) {
		setPixel(j, 0, 0, 255, UPPER);
	}
	FastLED.show();
	patternEnabled[index] = false;
}


void upperOff(int index, byte state) {

}


// ----------------------------------------------------------
// Example patterns


void fadeAllStrips(int index, byte state) {
	static int i = 0;
	if (i <= 128) {
		fadeToBlackStrip(UPPER, NUM_LEDS_UPPERS, 10);
		fadeToBlackStrip(UNDERGLOW, NUM_LEDS_UNDERGLOW, 10);
		FastLED.show();
		i++;
	// once the for loop condition is met change to the next state
	} else {
		// patternState[index] = 0;
		i = 0;
		setAll(0,0,0,UPPER);
		setAll(0,0,0,UNDERGLOW);
		patternEnabled[4] = false;
	}
}


void solid(int index, byte state) {
	static int strip = UNDERGLOW; 
	static int startLED = 0;
	static int stopLED = NUM_LEDS_UNDERGLOW;
	for (int j=startLED; j < stopLED; j++) {
		setPixel(j, 255, 255, 255, strip);
	}
	FastLED.show();
}


void wave(int index, byte state) {
	static int strip = UNDERGLOW;
	static int value = 0;
	static float loopcount = 0;
	static float translate_speed = 2;

	for (int i=0; i < NUM_LEDS_UNDERGLOW; i++) {
		//sin8 take 0-256 and gives 0-256
		if (sin8((i * 10) - 0.8 * pow(loopcount,translate_speed))>128) {
			value = 1;
		} else {
			value = 0;
		}

		setPixel(i, 255*value, 255*value, 255*value, strip);
	}
	loopcount++;
	FastLED.show();
	if (loopcount >= 200) {
	patternEnabled[index] = false;
	}
}


void meteorRainRight(int index, byte state) { 
	static byte red = 0xff;
	static byte green = 0xff;
	static byte blue = 0xff;
	static byte meteorSize = 3;
	static byte meteorTrailDecay = 80;
	static boolean meteorRandomDecay = true;
	static int strip = UPPER;

	int startLED = meteorStartLED[index];
	int stopLED = meteorStopLED[index];
	static int localNUM_LEDS[2];  
	localNUM_LEDS[index] = abs(startLED - stopLED);

	if (state == 0) { //set to all black
		for (int j = startLED; j <= startLED + localNUM_LEDS[index]; j++) {
			setPixel(j, 0, 0, 0, strip);
		}
		FastLED.show();
		patternState[index] = 1;
		return;
	}

	if (state == 1){  //draw the meteors 
		static int i[] = {meteorStartLED[0], meteorStartLED[1]};
		if (i[index] < stopLED + 30) {  //if the for loop statement is true do the stuff in it
			for (int j = startLED; j <= stopLED; j++) {    // fade brightness all LEDs one step
				if ((!meteorRandomDecay) || (random(10)>5)) {
					fadeToBlack(j, meteorTrailDecay, strip);        
				}
			}

			for (int j = 0; j < meteorSize; j++) {    // draw meteor
				if ((i[index] - j <= stopLED) && (i[index] - j >= startLED)) {
					setPixel(i[index]-j, red, green, blue, strip);
				}
			}
			FastLED.show();

			i[index] = i[index] + 1;
		// once the for loop condition is met change to the next state
		} else {
			patternState[index] = 2;
			i[index] = startLED;
			patternEnabled[3] = true;
			patternEnabled[2] = false;

		}
		return;
	}

	if (state == 2) { //fill the strip with the color starting from the end
		static int i[] = {meteorStopLED[0], meteorStopLED[1]};
		static int skips = 8;

		if (i[index] > startLED) {
			for (int j = 0; j < skips; j++) {
				if (i[index] - j <= stopLED && i[index] - j >= startLED) {
					setPixel(i[index]-j, red, green, blue, strip);
				}
			}
			FastLED.show();

			i[index] = i[index] - skips;
		// once the for loop condition is met change to the next state
		} else {
			patternState[index] = 0;
			patternEnabled[4] = true;
			patternEnabled[3] = false;
			patternEnabled[0] = false;
			patternEnabled[1] = false;
			i[index] = stopLED;
		}
		return;
	}
}


void fadeToBlackStrip( int strip, int num, byte fadeValue) {
	if (strip == UPPER) {
		fadeToBlackBy(ledsUpper, num, fadeValue);
	}
	if (strip == UNDERGLOW) {
		fadeToBlackBy(ledsUnderglow, num, fadeValue);
	}
}


void fadeToBlack(int ledNo, byte fadeValue, int strip) {
	if (strip == UPPER) {
		ledsUpper[ledNo].fadeToBlackBy(fadeValue);
	}
	if (strip == UNDERGLOW) {
		ledsUnderglow[ledNo].fadeToBlackBy(fadeValue);
	}
}


// These are the pattern functions written as a state machine

// this is the Blink program in FastLED's example folder
void underglowBlinkOne(int index,byte state) {
	if (state == 0) {
		ledsUnderglow[3] = CRGB::Blue;
		FastLED.show();
		patternState[index] = 1; // move on the state machine for the next call
	}
	if (state == 1) {
		ledsUnderglow[3] = CRGB::Black;
		FastLED.show();
		patternState[index] = 0;
	}
}

// this is the Cylon program in FastLED's example folder
// we will use LEDs 8 to 15 to show this
void underGlowCylon(int index,byte state) {
	static int i = 8; // replaces the loop index
	if (state == 0) {
		ledsUnderglow[i] = CRGB::Red;
		FastLED.show();
		patternState[index] = 1; // move on the state machine for the next call
	}
	if (state == 1) {
		// now that we've shown the leds, reset the i'th led to black
		ledsUnderglow[i] = CRGB::Black;
		i++; // increment what was the loop variable
		if (i >= 16) { // we have finished one direction
			patternState[index] = 2;
			i--;
		} else {
			patternState[index] = 0;
		}
	}
		// Now go in the other direction only green
	if (state == 2) {
		ledsUnderglow[i] = CRGB::Green;
		FastLED.show();
		patternState[index] = 3; // move on the state machine for the next call
	}
	if (state == 3) {
		// now that we've shown the leds, reset the i'th led to black
		ledsUnderglow[i] = CRGB::Black;
		i--; // decrement what was the loop variable
		if (i < 8) { // we have finished the return, go back to the start
			patternState[index] = 0;
			i= 8; // ready to start again
		} else {
			patternState[index] = 2;
		} 
		// note that this could be better implemented but it has been written like this to keep it close to the original example
		// so you can see what changes have been made 
	}
}


//colorWipe  modified from Adafruit example to make it a state machine
// uses LEDs 32 to 40
void underglowColorWipe(int index,byte state) {
	static int i =0; // used as state variable
	static byte firstLED = 32;
	static byte numLEDs = 8;

	ledsUnderglow[i+firstLED] = CRGB::Yellow;
	FastLED.show();
	i++;
	if (i >= numLEDs) {
		i = 0;
		for (int j;j<numLEDs; j++) {
			// blank out strip
			ledsUnderglow[j+firstLED] = CRGB::Black;
		}
	}
}

// rainbowCycle modified from Adafruit example to make it a state machine
// uses LEDs 48 to 64
void underglowRainbowCycle(int index,byte state) {
	static uint16_t j=0; // used as state variable
	static byte firstLED = 48;
	static byte numLEDs = 16;

	for (int i=0; i< numLEDs; i++) {
		ledsUnderglow[i+firstLED].setHSV( (((i * 256 / numLEDs) + j) & 255), 255, 255);
	}
	
	FastLED.show();
	j++;

	if (j >= 256*5) {
		j=0;
	}
}
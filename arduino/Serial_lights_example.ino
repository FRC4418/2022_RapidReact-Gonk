#include <FastLED.h>
#include <Wire.h>

#define NUM_LEDS 120
#define DATA_PIN 10
#define BRIGHTNESS  30
#define LED_TYPE    WS2812B
#define COLOR_ORDER GRB
#define UPDATES_PER_SECOND 100

CRGB leds[NUM_LEDS];


void setup() {
  delay(1000); // power-up safety delay
  Serial.begin(9600);
  FastLED.addLeds<LED_TYPE, DATA_PIN, COLOR_ORDER>(leds, NUM_LEDS).setCorrection( TypicalLEDStrip );
  startupEffect();
  FastLED.setBrightness(BRIGHTNESS);
}


void loop() {
  if(Serial.available()) {
    int value = Serial.parseInt(); //read as integer
    while(Serial.available() != 0){Serial.read();}   //clear out anything else from the buffer
    Serial.println(value);
      
    switch(value){
      case 5:
        Serial.println("FUn effect");
        meteorRainFill(0xff,0xff,0xff,8, 100, true, .5, 1);
        break;
        
       case 4:
        Serial.println("robot turned on");
        for(int i = 0; i < NUM_LEDS; i++) {
          leds[i] = CHSV(160, 255, 128);
         }
        break;
         
      case 3:
        Serial.println("lime light");
        for(int i = 0; i < NUM_LEDS; i++) {
          leds[i] = CRGB::Green;
         }
         break;
         
      case 2:
        Serial.println("arm moving");
        for(int i = 0; i < NUM_LEDS; i++){
          leds[i] = CRGB::Red;
         }
         break;
         
      default:
        Serial.println("error");
        for(int i = 0; i < NUM_LEDS; i++){
          leds[i] = CRGB::Black;
         }
        break;
    }
  }
  FastLED.show();
}

void startupEffect(){
  Wire.end();
  meteorRainHalfFill(0xff,0xff,0xff,8, 100, true, .5, 1);
  for(int j=255; j > 0; j--){
    for(int i = 0; i < NUM_LEDS; i++ ) {
      leds[i].r = j;
      leds[i].g = j;
      leds[i].b = j;
    }
    FastLED.show();
    delay(2);
  }
  Wire.begin(I2C_ADDRESS);
  Wire.onReceive(i2cReceiveEvent);
}

void meteorRainHalfFill(byte red, byte green, byte blue, byte meteorSize, byte meteorTrailDecay, boolean meteorRandomDecay, int SpeedDelay, int FillDelay) {  
  setAll(0,0,0);
 
  for(int i = 0; i < NUM_LEDS/2 + 30; i++) {
   // start in center at num/2 spread out i from center each time 
   
    // fade brightness all LEDs one step
    for(int j=0; j<NUM_LEDS; j++) {
      if( (!meteorRandomDecay) || (random(10)>5) ) {
        fadeToBlack(j, meteorTrailDecay );        
      }
    }
   
    // draw meteors
    for(int j = 0; j < meteorSize; j++) {
      if( ( i-j <(NUM_LEDS/2)) && (i-j>=0) ) {
        setPixel((NUM_LEDS/2) - (i-j), red, green, blue);
      }
    }
    for(int j = 0; j < meteorSize; j++) {
      if( ( i-j <(NUM_LEDS/2)) && (i-j>=0) ) {
        setPixel((NUM_LEDS/2) + (i-j), red, green, blue);
      }
    }
   
    FastLED.show();
    delay(SpeedDelay);
  }
  //fill the strip with the color starting from the ends
  for(int i = 0; i <= NUM_LEDS/2; i+=2) {
    setPixel(i, red, green, blue);
    setPixel(NUM_LEDS - i, red, green, blue);
    setPixel(i+1, red, green, blue);
    setPixel(NUM_LEDS - i - 1, red, green, blue);
    FastLED.show();
    delay(FillDelay);
  }
}


void meteorRain(byte red, byte green, byte blue, byte meteorSize, byte meteorTrailDecay, boolean meteorRandomDecay, int SpeedDelay) {  
  setAll(0,0,0);
 
  for(int i = 0; i < NUM_LEDS * 1.5; i++) {
   
   
    // fade brightness all LEDs one step
    for(int j=0; j<NUM_LEDS; j++) {
      if( (!meteorRandomDecay) || (random(10)>5) ) {
        fadeToBlack(j, meteorTrailDecay );        
      }
    }
   
    // draw meteor
    for(int j = 0; j < meteorSize; j++) {
      if( ( i-j <NUM_LEDS) && (i-j>=0) ) {
        setPixel(i-j, red, green, blue);
      }
    }
   
    FastLED.show();
    delay(SpeedDelay);
  }
}

void meteorRainFill(byte red, byte green, byte blue, byte meteorSize, byte meteorTrailDecay, boolean meteorRandomDecay, int SpeedDelay, int FillDelay) {  
  setAll(0,0,0);
 
  for(int i = 0; i < NUM_LEDS; i++) {
   
   
    // fade brightness all LEDs one step
    for(int j=0; j<NUM_LEDS; j++) {
      if( (!meteorRandomDecay) || (random(10)>5) ) {
        fadeToBlack(j, meteorTrailDecay );        
      }
    }
   
    // draw meteor
    for(int j = 0; j < meteorSize; j++) {
      if( ( i-j <NUM_LEDS) && (i-j>=0) ) {
        setPixel(i-j, red, green, blue);
      }
    }
   
    FastLED.show();
    delay(SpeedDelay);
  }
  //fill the strip with the color starting from the end with the color 
  for(int i = NUM_LEDS; i >= 0; i-=3) {
    setPixel(i, red, green, blue);
    setPixel(i-1, red, green, blue);
    setPixel(i-2, red, green, blue);
    FastLED.show();
    delay(FillDelay);
  }
}

void fadeToBlack(int ledNo, byte fadeValue) {
  leds[ledNo].fadeToBlackBy( fadeValue ); 
}

void setPixel(int Pixel, byte red, byte green, byte blue) {
  leds[Pixel].r = red;
  leds[Pixel].g = green;
  leds[Pixel].b = blue;
}

void setAll(byte red, byte green, byte blue) {
  for(int i = 0; i < NUM_LEDS; i++ ) {
    setPixel(i, red, green, blue);
  }
  FastLED.show();
}

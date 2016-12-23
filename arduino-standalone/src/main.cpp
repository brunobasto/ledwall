#include <FAB_LED.h>
#include <font8x8_basic.h>
#include <Arduino.h>

#define SCROLL_LEFT 0
#define SCROLL_RIGHT 1
#define SCROLL_UP 2
#define SCROLL_DOWN 3

/// @brief This parameter says how many LEDs will be lit up in your strip.
const uint8_t numPixels = 64;

/// @brief This says how bright LEDs will be (max is 255)
const uint8_t maxBrightness = 5;

/// @brief Declare the protocol for your LED strip. Data is wired to port D6,
/// and the clock to port D5, for APA102 only.
//ws2812b<D,6>  LEDstrip;
ws2812<D,6>  LEDstrip;
//sk6812<D,6>   LEDstrip;
//sk6812b<D,5>  LEDstrip;
//apa104<D,6>   LEDstrip;
//apa106<D,6>   LEDstrip;
//apa102<D,6,D,5>  LEDstrip;

/// @brief Array holding the pixel data.
/// Note you have multiple formats available, to support any LED type. If you
/// use the wrong type, FAB_LED will do the conversion transparently for you.
//hbgr  pixels[numPixels] = {};
//grbw  pixels[numPixels] = {};

grb  pixels[numPixels] = {};

/**
 * orward declarations
 */

void clearPixels();
void test();
void printPoint(int x, int y, int set, uint8_t r, uint8_t g, uint8_t b);

void setup()
{
  Serial.begin(9600);
  randomSeed(analogRead(0));
  test();
}

uint8_t getPos(int x, int y) {
    if (x < 0) { x = 0; };
    if (y < 0) { y = 0; };
    if (x > 7) { x = 7; };
    if (y > 7) { y = 7; };

    return y * 8 + x;
}

void printPoint(int x, int y, int set, uint8_t r, uint8_t g, uint8_t b) {
    uint8_t pos =  getPos(x, y);
    pixels[pos].g = set * g;
    pixels[pos].b = set * b;
    pixels[pos].r = set * r;
}

/**
 * Cursor is one place "ahead" of the (x,y) coordinates
 * This signs where the next char will be written
 * @param x          [description]
 * @param y          [description]
 * @param brightNess [description]
 */
void printCursor(int x, int y, uint8_t brightNess) {
    x++;
    if (x > 7) {x = 0; y++; };
    if (y < 8) {
        printPoint(x, y, 1,
            random(1, brightNess),
            random(1, brightNess),
            random(1, brightNess));
    }
}


/**
 * Writes one char onto the LED matrix
 * @param c [description]
 */
void printCharWithPrintCursor(char c) {
    char *bitmap = font8x8_basic[c];
    int set = 0;
    for (int y=0; y < 8; y++) {
        for (int x=0; x < 8; x++) {
            printCursor(x, y, 10);

            set = ((bitmap[x] & (1 << y)) > 0) ? 1 : 0;

            printPoint(x, y, set, 10, 10, 10);

            //Serial.print(set ? 'X' : ' ');

            LEDstrip.sendPixels(numPixels,pixels);
            delay(100);
        }
        //Serial.println("");
    }
    LEDstrip.sendPixels(numPixels,pixels);
}

void copyPixels(uint8_t pos, uint8_t epos) {
    pixels[epos].b = pixels[pos].b;
    pixels[epos].g = pixels[pos].g;
    pixels[epos].r = pixels[pos].r;
}

void scrollRight(int y) {
        for (int x = 1; x < 8; x++) {
            copyPixels(getPos(x,y), getPos(x - 1, y));
        }
}

void scrollLeft(int y) {
        for (int x = 6; x >= 0; x--) {
            copyPixels(getPos(x,y), getPos(x + 1, y));
        }
}

void scrollDown(int x) {
        for (int y = 6; y >= 0; y--) {
            copyPixels(getPos(x,y), getPos(x, y + 1));
        }
}

void scrollUp(int x) {
        for (int y = 1; y < 8; y++) {
            copyPixels(getPos(x,y), getPos(x, y - 1));
        }
}

void printCharWithScroll(char c, int direction) {
    char *bitmap = font8x8_basic[c];
    int set = 0;
    uint8_t r = random(0, maxBrightness);
    uint8_t g = random(0, maxBrightness);
    uint8_t b = random(0, maxBrightness);

    for (int bits = 0; bits < 8; bits++) {
        for (int i=0; i < 8; i++) {

            if (direction == SCROLL_LEFT) {
                scrollRight(i);
                set = ((bitmap[i] & (1 << bits)) > 0) ? 1 : 0;
                printPoint(7, i, set, r, g, b);
            }
            if (direction == SCROLL_RIGHT) {
                scrollLeft(i);
                set = ((bitmap[i] & (1 << (7 - bits))) > 0) ? 1 : 0;
                printPoint(0, i, set, r, g, b);
            }
            if (direction == SCROLL_UP) {
                scrollDown(i);
                set = ((bitmap[7 - bits] & (1 << (i))) > 0) ? 1 : 0;
                printPoint(i, 0, set, r, g, b);
            }
            if (direction == SCROLL_DOWN) {
                scrollUp(i);
                set = ((bitmap[bits] & (1 << (i))) > 0) ? 1 : 0;
                printPoint(i, 7, set, r, g, b);
            }


        }

        LEDstrip.sendPixels(numPixels,pixels);

        delay(100);
    }
}

/**
 * Writes a string onto the LED matrix
 * @param s [description]
 */
void printString(char s[]) {
    for (uint8_t i = 0; s[i] != '\0'; i++) {
        printCharWithScroll(s[i], random(4));
        delay(1);
    }
}

void loop() {
    printString("Liferay");
    delay(1000);
}




void clearPixels() {
    for (uint8_t pos = 0; pos < numPixels; pos++) {
      //pixels[pos].h = 0xFF; // hgrb has h field
        pixels[pos].g = 0;
        pixels[pos].b = 0;
        pixels[pos].r = 0;
      //pixels[pos].w = 0; // grbw has w field
    }
}

void test() {
    clearPixels();
    printPoint(0, 0, 1, 10, 0, 0);
    printPoint(0, 7, 1, 0, 10, 0);
    printPoint(7, 0, 1, 0, 0, 10);
    printPoint(7, 7, 1, 10, 10, 10);
    LEDstrip.sendPixels(numPixels,pixels);
    delay(1000);
    clearPixels();
    LEDstrip.sendPixels(numPixels,pixels);
}
